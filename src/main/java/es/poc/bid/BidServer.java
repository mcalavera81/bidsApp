package es.poc.bid;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import es.poc.bid.business.impl.AuctionServiceImpl;
import es.poc.bid.config.AppConfig;
import es.poc.bid.config.Constants;
import es.poc.bid.controller.BidController;
import es.poc.bid.session.impl.SessionIdGeneratorImpl;
import es.poc.bid.session.impl.SessionServiceImpl;
import es.poc.bid.utils.BidLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by mcalavera81 on 11/01/16.
 */
public class BidServer {

    private final static Logger logger = Logger.getLogger(BidServer.class.getName());

    private HttpServer httpServer;
    private int port;

    public BidServer(int port) throws IOException {
        this(port, "/");
    }

    public BidServer(int port, String context) throws IOException {
        this.port = port;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext(context, buildApplication());
        httpServer.setExecutor(Executors.newScheduledThreadPool(Constants.NUMBER_THREADS_SERVER));
    }

    public void start(){
        httpServer.start();
        BidLogger.info(logger, "Server is started and listening on port %s", port);
    }


    public BidController buildApplication(){
        return AppConfig.getAppConfiguration();
    }


}


