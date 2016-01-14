package es.poc.bid.domain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class Bid{

    private static String bidTemplate="{ \"%s\": \"%s\"}";

    public Bid(User user, Item item, double amount) {
        this.user = user;
        this.item = item;
        this.amount = Math.round(amount*100);
    }

    private User user;

    private Item item;

    private Long amount;



    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public double getAmount() {
        return amount.doubleValue()/100;
    }



    public String getJsonFormat(){
        return String.format(bidTemplate, getUser().getUserId(), getAmount());
    }

    public static String getJson(List<Bid> bids){
        return bids.stream().map(Bid::getJsonFormat).collect(Collectors.joining(", ","[ "," ]"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (!user.equals(bid.user)) return false;
        if (!item.equals(bid.item)) return false;
        return amount.equals(bid.amount);

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
}
