package es.poc.bid.session.impl;

import es.poc.bid.config.Constants;
import es.poc.bid.session.Session;
import es.poc.bid.session.SessionIdGenerator;
import es.poc.bid.session.SessionService;
import es.poc.bid.utils.BidLogger;

import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class SessionServiceImpl implements SessionService {
    private final static Logger logger = Logger.getLogger(SessionServiceImpl.class.getName());

    private SessionIdGenerator sessionIdGenerator;

    public SessionServiceImpl() {
        sessionIdGenerator = new SessionIdGeneratorImpl();
    }

    public SessionServiceImpl(SessionIdGenerator sessionIdGenerator) {
        this.sessionIdGenerator = sessionIdGenerator;
    }


    private ScheduledExecutorService sessionTimeoutScheduler=
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private Map<String,ScheduledFuture> sessionTimers= new ConcurrentHashMap<>(100, 0.75f, 10);
    private Map<String,Session> sessions= new ConcurrentHashMap<>(100, 0.75f, 10);


    public Session setUpNewSession(){
        Session session = produceNewSession();
        storeSession(session);
        scheduleAndPersistSessionTimeout(session.getId());

        BidLogger.info(logger, "Session %s created", session.getId());
        return  session;
    }


    public void refreshSession(String sessionId){

        if(!tryToCancelSessionOldTimer(sessionId)) throw  new RuntimeException("Session "+sessionId+" expired!");
        scheduleAndPersistSessionTimeout(sessionId);

        BidLogger.info(logger, "Session %s refreshed", sessionId);
    }



    public void cleanUpSession(String sessionId){
        removeSession(sessionId);
        ScheduledFuture timeoutTask = removeTimeoutForSession(sessionId);
        cancelTimeoutTask(timeoutTask);

        BidLogger.info(logger, "Session %s deleted", sessionId);
    }

    private void cancelTimeoutTask(ScheduledFuture timeoutTask) {
        if(timeoutTask!=null) timeoutTask.cancel(false);
    }

    private void storeSession(Session session) {
        sessions.put(session.getId(), session);
    }

    private Session produceNewSession() {
        return new Session(sessionIdGenerator.generatetSessionId());
    }


    private void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public Session getSession(String sessionId){
        return sessions.get(sessionId);
    }

    private boolean tryToCancelSessionOldTimer(String sessionId) {
        ScheduledFuture timeoutTask = sessionTimers.get(sessionId);
        if(timeoutTask == null) throw new RuntimeException("Session "+sessionId+ "has no timer in place!");

        cancelTimeoutTask(timeoutTask);
        BidLogger.info(logger, "Trying to cancel session %s with remaining delay %d sec", sessionId, timeoutTask.getDelay(TimeUnit.SECONDS));
        return timeoutTask.isCancelled();
    }

    private void scheduleAndPersistSessionTimeout(String sessionId){

        ScheduledFuture<?> timeoutTask = sessionTimeoutScheduler.schedule(
                () -> {
                    cleanUpSession(sessionId);
                },
                Constants.SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);

        storeTimeoutForSession(sessionId, timeoutTask);
        BidLogger.info(logger, "Set off timeout for session %s", sessionId);

    }

    private void storeTimeoutForSession(String sessionId, ScheduledFuture<?> timeoutTask) {
        sessionTimers.put(sessionId, timeoutTask);
    }

    private ScheduledFuture removeTimeoutForSession(String sessionId){
        return sessionTimers.remove(sessionId);
    }

}
