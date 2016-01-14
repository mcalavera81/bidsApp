package es.poc.bid;

import es.poc.bid.config.AppConfig;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by mcalavera81 on 14/01/16.
 */
public class ApplicationBuildingTest {

    @Test
    public void testProductionConfiguration(){
        Assert.assertNotNull(AppConfig.getAppConfiguration());
    }
}
