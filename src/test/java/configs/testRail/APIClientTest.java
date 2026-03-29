package configs.testRail;

import org.testng.Assert;
import org.testng.annotations.Test;

public class APIClientTest {

    @Test
    public void verifyExplicitTimeoutsAreStored() {
        APIClient client = new APIClient(
                "https://demo.testrail.io/",
                "index.php?/api/v2/",
                "user@example.com",
                "secret",
                1234,
                5678
        );

        Assert.assertEquals(client.getConfiguredConnectTimeoutMillis(), 1234);
        Assert.assertEquals(client.getConfiguredReadTimeoutMillis(), 5678);
    }

    @Test
    public void verifySingleArgumentConstructorUsesSafeDefaults() {
        APIClient client = new APIClient("https://demo.testrail.io/");

        Assert.assertEquals(client.getConfiguredConnectTimeoutMillis(), 10000);
        Assert.assertEquals(client.getConfiguredReadTimeoutMillis(), 30000);
    }
}
