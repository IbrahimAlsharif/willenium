package base;

import com.fasterxml.jackson.databind.JsonNode;
import configs.api.ApiContext;
import configs.testdata.TestData;
import configs.testdata.TestDataFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;

public class ApiSetup {
    public static TestData testData;
    public static RequestSpecification apiSpecification;

    @Test(priority = 1)
    @Parameters({"language", "branch"})
    public void setUpApiClient(String language, String branch) {
        ApiContext.clear();
        Setup.testCaseId = null;
        testData = TestDataFactory.getTestData(branch, language);

        JsonNode apiData = testData.getApiData();
        String baseUrl = apiData.get("baseUrl").asText();

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON.toString());

        JsonNode headers = apiData.get("headers");
        if (headers != null && headers.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = headers.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                builder.addHeader(entry.getKey(), entry.getValue().asText());
            }
        }

        apiSpecification = builder.build();
        ApiClient.initialize(apiSpecification, baseUrl);
        Assert.assertNotNull(apiSpecification, "API request specification should be initialized");
    }
}
