package tests.examples.wewill.api;

import base.ApiSetup;
import com.fasterxml.jackson.databind.JsonNode;
import configs.testRail.TestRailCase;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static base.ApiSetup.testData;

public class WeWillPublicApiTest extends ApiSetup {

    @TestRailCase("API-0001")
    @Test(priority = 1)
    public void verifyExamplePostContract() {
        // This checks the read contract still returns the expected example record and metadata.
        JsonNode getOnePostData = testData.getApiData().get("posts").get("getOne");
        Response response = WeWillPublicApi.getExamplePost();

        Assert.assertEquals(
                response.statusCode(),
                getOnePostData.get("expectedStatus").asInt(),
                "Example public API response status should match the configured contract"
        );
        Assert.assertTrue(
                response.getContentType().contains(getOnePostData.get("expectedContentType").asText()),
                "Example public API response should be JSON"
        );
        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                getOnePostData.get("expectedId").asInt(),
                "Example public API response should return the expected post id"
        );
        Assert.assertEquals(
                response.jsonPath().getString("title"),
                getOnePostData.get("expectedTitle").asText(),
                "Example public API response should return the expected post title"
        );
    }

    @TestRailCase("API-0002")
    @Test(priority = 2)
    public void verifyExamplePostCreationContract() {
        // This verifies the create contract echoes the expected fields for a valid request.
        JsonNode createPostData = testData.getApiData().get("posts").get("createOne");
        Response response = WeWillPublicApi.createExamplePost();

        Assert.assertEquals(
                response.statusCode(),
                createPostData.get("expectedStatus").asInt(),
                "Example public API create response status should match the configured contract"
        );
        Assert.assertTrue(
                response.getContentType().contains(createPostData.get("expectedContentType").asText()),
                "Example public API create response should be JSON"
        );
        Assert.assertEquals(
                response.jsonPath().getString("title"),
                createPostData.get("expectedTitle").asText(),
                "Example public API create response should echo the expected title"
        );
        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                createPostData.get("expectedUserId").asInt(),
                "Example public API create response should echo the expected user id"
        );
    }
}
