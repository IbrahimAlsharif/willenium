package base;

import com.fasterxml.jackson.databind.JsonNode;
import configs.api.ApiContext;
import configs.api.OpenApiContract;
import configs.testRail.TestRailContext;
import configs.testRail.TestRailConfig;
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
    private static final TestRailConfig TEST_RAIL_CONFIG = TestRailConfig.getInstance();
    public static TestData testData;
    public static RequestSpecification apiSpecification;

    @Test(priority = 1)
    @Parameters({"language", "branch"})
    public void setUpApiClient(String language, String branch) {
        ApiContext.clear();
        TestRailContext.clearCurrentTestRunId();
        TestRailContext.clearCurrentTestCaseId();
        Go.testRunId = null;
        Setup.testCaseId = null;

        String apiSetupCaseId = TEST_RAIL_CONFIG.getApiSetupCaseId();
        if (!apiSetupCaseId.isBlank()) {
            TestRailContext.setCurrentTestCaseId(apiSetupCaseId);
            Setup.testCaseId = apiSetupCaseId;
        }

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

        OpenApiContract.configure(apiData);
        apiSpecification = builder.build();
        ApiClient.initialize(apiSpecification, baseUrl);
        Assert.assertNotNull(apiSpecification, "API request specification should be initialized");
    }
}
