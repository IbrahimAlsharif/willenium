package tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Simple API test that verifies the base URL responds
 * successfully. This demonstrates API testing using the
 * existing TestNG framework.
 */
public class SystemHealthApiTest extends BaseApiTest {

    @Test
    public void baseUrlReturnsSuccess() {
        Response response = RestAssured.given()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Connection", "keep-alive")
                .when()
                .get("/");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
