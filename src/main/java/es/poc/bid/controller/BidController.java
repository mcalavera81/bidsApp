package es.poc.bid.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.poc.bid.business.AuctionService;
import es.poc.bid.business.impl.AuctionServiceImpl;
import es.poc.bid.domain.Bid;
import es.poc.bid.domain.Item;
import es.poc.bid.domain.User;
import es.poc.bid.session.Session;
import es.poc.bid.session.SessionService;
import es.poc.bid.session.impl.SessionServiceImpl;
import es.poc.bid.utils.BidLogger;
import es.poc.bid.utils.RequestParsed;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by mcalavera81 on 11/01/16.
 */
public class BidController implements HttpHandler {

    private final static Logger logger = Logger.getLogger(BidController.class.getName());

    private AuctionService auctionService;

    private SessionService sessionService;

    public BidController() {
        this.sessionService = new SessionServiceImpl();
        this.auctionService = new AuctionServiceImpl();
    }

    public BidController(SessionService sessionService, AuctionService auctionService) {
        this.sessionService = sessionService;
        this.auctionService = auctionService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try{
            RequestParsed requestParsed = new RequestParsed(
                    httpExchange.getRequestMethod(),
                    httpExchange.getRequestURI().getSchemeSpecificPart(),
                    httpExchange.getRequestBody());

            switch(requestParsed.getMatchedRequestPath()){
                case LOGIN_PATH:
                    handleLogin(httpExchange, requestParsed);
                    break;
                case BID_ITEM:
                    handleBidForItem(httpExchange, requestParsed);
                    break;
                case TOP_BIDS:
                    handleTopBids(httpExchange, requestParsed);
                    break;
                default:
                    throw new RuntimeException("Unexpected path!");
            }

        }catch(Exception exception){
            String exceptionDetail = Arrays.stream(exception.getStackTrace()).map(stackEl -> "  " + stackEl).collect(Collectors.joining("\n"));
            writeTextResponse(httpExchange,Optional.of(exception.getMessage()+"\n"+exceptionDetail));
        }

    }


    protected void handleTopBids(HttpExchange httpExchange, RequestParsed requestParsed) {
        List<String> requestParams = requestParsed.getRequestParams();
        String itemId = requestParams.get(0);

        List<Bid> bidsForItem = auctionService.getBidsForItem(new Item(itemId));

        writeTextResponse(httpExchange, Optional.of(Bid.getJson(bidsForItem)) );
    }

    protected void handleLogin(HttpExchange httpExchange, RequestParsed requestParsed){
        String userId = requestParsed.getRequestParams().get(0);
        Session session = sessionService.setUpNewSession();
        session.setAttribute("user", new User(userId));
        writeTextResponse(httpExchange, Optional.of(session.getId()));

    }

    protected void handleBidForItem(HttpExchange httpExchange, RequestParsed requestParsed){

        List<String> requestParams = requestParsed.getRequestParams();
        String itemId = requestParams.get(0);

        String sessionId= requestParams.get(1);
        Session session = sessionService.getSession(sessionId);
        if(session == null) throw new RuntimeException("Invalid session "+ sessionId);

        sessionService.refreshSession(sessionId);

        Bid bid = new Bid(
                (User)session.getAttribute("user"),
                new Item(itemId),
                Double.parseDouble(requestParsed.getBodyContent()));

        auctionService.bidForItem(bid);
        writeTextResponse(httpExchange, Optional.empty());
    }



    public void writeTextResponse(HttpExchange httpExchange, Optional<String> textOpt){
        try {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, textOpt.map(String::getBytes).map(array->array.length).orElse(0));
            OutputStream outputStream = httpExchange.getResponseBody();
            if(textOpt.isPresent()){
                outputStream.write(textOpt.get().getBytes());
            }
            outputStream.close();
        } catch (IOException e) {
            BidLogger.info(logger,"Could not write output stream");
        }

    }



}
