package es.poc.bid.domain;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class Item {

    public Item(String itemId){
        this.itemId = itemId;
    }

    private String itemId;

    public String getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return itemId.equals(item.itemId);

    }

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }
}
