package es.poc.bid;




import java.io.IOException;

/**
 * Created by mcalavera81 on 08/01/16.
 */
public class Driver {
    public static void main(String[] args) throws IOException {

        BidServer bidServer = new BidServer(9090);
        bidServer.start();

    }

}