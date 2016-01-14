package es.poc.bid.config;

import es.poc.bid.business.impl.AuctionServiceImpl;
import es.poc.bid.controller.BidController;
import es.poc.bid.session.impl.SessionIdGeneratorImpl;
import es.poc.bid.session.impl.SessionServiceImpl;

/**
 * Created by mcalavera81 on 14/01/16.
 */
public class AppConfig {

    public static BidController getAppConfiguration(){
        return  new BidController(
                new SessionServiceImpl(
                        new SessionIdGeneratorImpl()),
                new AuctionServiceImpl());
    }

}
