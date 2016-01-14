package es.poc.bid.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mcalavera81 on 11/01/16.
 */
public class BidLogger {


    public static void initializeLogging(){
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);

        Logger logger = Logger.getLogger("es.poc.bid");
        logger.setLevel(Level.INFO);

        logger.addHandler(handler);
    }

    public static void info(Logger logger, String message, Object ... args){
        logger.log(Level.INFO, String.format(message, args));
    }
}
