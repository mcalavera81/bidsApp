package es.poc.bid.business;

import es.poc.bid.business.impl.AuctionServiceImpl;
import es.poc.bid.config.Constants;
import es.poc.bid.domain.Bid;
import es.poc.bid.domain.Item;
import es.poc.bid.domain.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mcalavera81 on 14/01/16.
 */
public class AuctionServiceTest {


    private AuctionService auctionService= new AuctionServiceImpl();


    private Item item = new Item("12");
    private User user1 = new User("user1");
    private User user2 = new User("user2");

    private Bid bid1 = new Bid(user1, item, 23.45);
    private Bid bid2 = new Bid(user1, item, 23.46);
    private Bid bid3 = new Bid(user2, item, 23.47);

    @Before
    public void setUp(){
        auctionService.clearBidsForItem(item);
    }

    @Test
    public void testBidForItem(){


        double amount = bid1.getAmount();

        auctionService.bidForItem(bid1);
        List<Bid> bids = auctionService.getBidsForItem(item);
        Assert.assertTrue(bids.size()==1);
        Assert.assertEquals(Math.round(amount*100), Math.round(bids.get(0).getAmount()*100));

    }

    @Test
    public void testHigherBidForItemFromSameUser(){
        Bid lowBid = bid1;
        Bid highBid = bid2;

        setUpLowAndHighBid(lowBid, highBid);
        assertNumberOfBidsAndWinningBidForItem(highBid, 1);

    }

    @Test
    public void testHigherBidForItemFromDifferentUser(){

        Bid lowBid = bid1;
        Bid highBid = bid3;

        setUpLowAndHighBid(lowBid, highBid);
        assertNumberOfBidsAndWinningBidForItem(highBid, 2);

    }

    private void assertNumberOfBidsAndWinningBidForItem(Bid winningBid, int numberOfBids){
        List<Bid> bids = auctionService.getBidsForItem(winningBid.getItem());
        Assert.assertTrue(bids.size()==numberOfBids);
        Assert.assertEquals(winningBid, bids.get(0));
    }

    public void setUpLowAndHighBid(Bid lowBid, Bid highBid){
        auctionService.bidForItem(lowBid);
        auctionService.bidForItem(highBid);
    }


    @Test
    public void testLimitOfNumberOfBidsReturnedPerItem(){

        List<Bid> bids = create100BidsFrom100DifferentUsers();

        Assert.assertEquals(100, bids.size());

        bids.forEach(bid->auctionService.bidForItem(bid));

        Assert.assertEquals(Constants.MAX_BIDS_RETURNED,auctionService.getBidsForItem(item).size());

    }

    private List<Bid> create100BidsFrom100DifferentUsers() {
        return IntStream.range(100, 200)
                    .boxed()
                    .map(amount -> new Bid(new User("userId"+amount), item, amount))
                    .collect(Collectors.toList());
    }

}
