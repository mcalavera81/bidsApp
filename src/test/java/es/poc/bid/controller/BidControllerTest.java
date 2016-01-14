package es.poc.bid.controller;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import es.poc.bid.session.impl.SessionIdGeneratorImpl;
import es.poc.bid.session.impl.SessionServiceImpl;
import es.poc.bid.utils.RequestParsed;
import org.junit.Assert;
import org.junit.Test;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Created by mcalavera81 on 13/01/16.
 */
public class BidControllerTest {


    @Test
    public void testRouterForLogin(){
        BidControllerRoutingTest bidController = new BidControllerRoutingTest();

        try {

            bidController.handle(mockHttpExchangeForValidLogin());
            Assert.assertEquals("handleLogin", bidController.methodCalled );

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testRouterForBidForItem(){
        BidControllerRoutingTest bidController = new BidControllerRoutingTest();

        try {

            bidController.handle(mockHttpExchangeForValidBidForItem());
            Assert.assertEquals("handleBidForItem",bidController.methodCalled );

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testRouterForValidTopBids(){
        BidControllerRoutingTest bidController = new BidControllerRoutingTest();

        try {

            bidController.handle(mockHttpExchangeForValidTopBids());
            Assert.assertEquals("handleTopBids",bidController.methodCalled );

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }


    public HttpExchangeTest mockHttpExchangeForValidLogin(){
        return setUpHttpExchange("GET",new ByteArrayInputStream(new byte[]{}),URI.create("/user1/login"));
    }

    public HttpExchangeTest mockHttpExchangeForValidBidForItem(){
        return setUpHttpExchange("POST",new ByteArrayInputStream("32".getBytes()),URI.create("/1/bid?sessionKey=123456"));
    }

    public HttpExchangeTest mockHttpExchangeForValidTopBids(){
        return setUpHttpExchange("GET",new ByteArrayInputStream(new byte[]{}) ,URI.create("/1/topBidList"));
    }

    private HttpExchangeTest setUpHttpExchange(String requestMethod, ByteArrayInputStream inputStream, URI uri){
        HttpExchangeTest httpExchange = new HttpExchangeTest();
        httpExchange.requestMethod = requestMethod;
        httpExchange.requestBody = inputStream;
        httpExchange.uri = uri;

        httpExchange.responseBody = new ByteArrayOutputStream();
        return httpExchange;
    }

    static class HttpExchangeTest extends HttpExchange{

        public String requestMethod;
        public URI uri;
        public InputStream requestBody;
        public OutputStream responseBody;



        @Override
        public Headers getRequestHeaders() { return null;}

        @Override
        public Headers getResponseHeaders() { return null; }

        @Override
        public URI getRequestURI() { return uri; }


        @Override
        public String getRequestMethod() { return requestMethod; }

        @Override
        public HttpContext getHttpContext() { return null; }

        @Override
        public void close() { }

        @Override
        public InputStream getRequestBody() { return requestBody; }

        @Override
        public OutputStream getResponseBody() { return responseBody; }

        @Override
        public void sendResponseHeaders(int i, long l) throws IOException { }

        @Override
        public InetSocketAddress getRemoteAddress() { return null; }

        @Override
        public int getResponseCode() { return 0; }

        @Override
        public InetSocketAddress getLocalAddress() { return null; }

        @Override
        public String getProtocol() { return null; }

        @Override
        public Object getAttribute(String s) { return null; }

        @Override
        public void setAttribute(String s, Object o) { }

        @Override
        public void setStreams(InputStream inputStream, OutputStream outputStream) {}

        @Override
        public HttpPrincipal getPrincipal() { return null; }
    }

    static class BidControllerRoutingTest extends  BidController{

        public BidControllerRoutingTest(){
            super();
        }

        public String methodCalled;

        @Override
        public void handleBidForItem(HttpExchange httpExchange, RequestParsed requestParsed) {
            methodCalled = new Object(){}.getClass().getEnclosingMethod().getName();
        }

        @Override
        public void handleLogin(HttpExchange httpExchange, RequestParsed requestParsed) {
            methodCalled = new Object(){}.getClass().getEnclosingMethod().getName();
        }

        @Override
        protected void handleTopBids(HttpExchange httpExchange, RequestParsed requestParsed) {
            methodCalled = new Object(){}.getClass().getEnclosingMethod().getName();
        }


    }



}
