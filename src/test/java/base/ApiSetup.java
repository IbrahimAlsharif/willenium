package base;

import com.fasterxml.jackson.databind.JsonNode;
import configs.api.ApiContext;
import configs.api.OpenApiContract;
import configs.pipeline.PipelineConfig;
import configs.testRail.APIException;
import configs.testRail.TestRailManager;
import configs.testdata.TestData;
import configs.testdata.TestDataFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Iterator;
import java.util.Map;

public class ApiSetup {
    public static TestData testData;
    public static RequestSpecification apiSpecification;
    public static final TestRailManager testRail = new TestRailManager();

    @BeforeClass(alwaysRun = true)
    @Parameters({"language", "branch"})
    public void setUpApiClient(@Optional("english") String language, @Optional("production") String branch) {
        ApiContext.clear();
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
        startTestRailRunIfEnabled();
        Assert.assertNotNull(apiSpecification, "API request specification should be initialized");
    }

    private void startTestRailRunIfEnabled() {
        if (!PipelineConfig.testRailReport || Go.testRunId != null && !Go.testRunId.isBlank()) {
            return;
        }
        try {
            Go.testRunId = testRail.createTestRun();
        } catch (APIException | java.io.IOException exception) {
            throw new IllegalStateException("Failed to create the TestRail run after API setup", exception);
        }
    }
}
