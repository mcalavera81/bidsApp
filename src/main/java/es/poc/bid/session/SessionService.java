package es.poc.bid.session;

import es.poc.bid.utils.BidLogger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by mcalavera81 on 13/01/16.
 */
public interface SessionService {

    Session setUpNewSession();
    void cleanUpSession(String sessionId);
    Session getSession(String sessionId);
    void refreshSession(String sessionId);


 }
