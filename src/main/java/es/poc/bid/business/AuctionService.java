package es.poc.bid.business;

import es.poc.bid.domain.Bid;
import es.poc.bid.domain.Item;
import es.poc.bid.domain.User;

import java.util.List;

/**
 * Created by mcalavera81 on 14/01/16.
 */
public interface AuctionService {

    void bidForItem(Bid bid);

    List<Bid> getBidsForItem(Item item);

    void clearBidsForItem(Item item);
}
