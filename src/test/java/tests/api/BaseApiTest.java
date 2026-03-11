package tests.api;

import configs.testdata.TestData;
import configs.testdata.TestDataFactory;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

/**
 * Base class for API tests. It initializes RestAssured with the
 * staging Arabic base URL so API tests can share the same
 * configuration as the UI tests.
 */
public class BaseApiTest {
    protected TestData data;

    @BeforeClass
    public void setUpBase() {
        data = TestDataFactory.getTestData("production", "arabic");
//        RestAssured.baseURI = data.getBaseUrl().asText();
    }
}
