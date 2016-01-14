package es.poc.bid.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mcalavera81 on 14/01/16.
 */
public class BidFormattingTest {

    private String userId = "user1";
    private String itemId = "1";
    private double  amount1= 23.45;
    private double  amount2= 10.50;


    private Bid bid1;
    private Bid bid2;

    public BidFormattingTest() {
    }

    @Before
    public void setUp(){
        bid1 = new Bid(new User(userId), new Item(itemId), amount1);
        bid2 = new Bid(new User(userId), new Item(itemId), amount2);
    }

    @Test
    public void testBidJsonFormat(){
        assertEqualsIgnoreWhiteSpaces(bid1.getJsonFormat(),
                "{ \""+userId+"\": \""+amount1+"\" }");

    }


    @Test
    public void testBidArrayJsonFormat(){
        assertEqualsIgnoreWhiteSpaces(Bid.getJson(Arrays.asList(bid1,bid2)),
                "["+ "{ \""+userId +"\": \""+amount1+ "\" }" +","+
                        "{ \""+userId +"\": \""+amount2+ "\" }" +"]");

    }

    public void assertEqualsIgnoreWhiteSpaces(String expected, String actual){
        Assert.assertEquals(expected.replaceAll(" ",""), actual.replaceAll(" ",""));
    }
}
