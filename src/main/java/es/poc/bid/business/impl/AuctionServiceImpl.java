package es.poc.bid.business.impl;

import es.poc.bid.business.AuctionService;
import es.poc.bid.domain.Bid;
import es.poc.bid.domain.Item;
import es.poc.bid.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class AuctionServiceImpl implements AuctionService {


    private final static Logger logger = Logger.getLogger(AuctionServiceImpl.class.getName());

    private Map<Item, List<Bid>> allBids = new ConcurrentHashMap<>(100, 0.75f, 10);

    @Override
    public void bidForItem(Bid newBid){

        List<Bid> itemBids = this.allBids.get(newBid.getItem());
        if(itemBids==null){
            itemBids = new ArrayList<>();
        }else{
            List<Bid> bidUser = itemBids.stream().filter(b -> b.getUser().equals(newBid.getUser())).collect(Collectors.toList());
            if(bidUser.size()>1) throw new RuntimeException("At most one bid is expected to be alive for each user");
            if(bidUser.size() == 1){
                if(newBid.getAmount() < bidUser.get(0).getAmount()) return;
                else{
                    itemBids = itemBids.stream().filter(bid -> !bid.getUser().equals(newBid.getUser())).collect(Collectors.toList());
                }
            }
        }

        itemBids.add(newBid);
        allBids.put(newBid.getItem(), itemBids);

    }

    @Override
    public List<Bid> getBidsForItem(Item item) {

        return allBids.getOrDefault(item, new ArrayList<>())
                .stream().sorted(Comparator.comparingDouble((Bid bid)->bid.getAmount()).reversed())
                .limit(15)
                .collect(Collectors.toList());

    }

    @Override
    public void clearBidsForItem(Item item) {
        allBids.remove(item);
    }


}
