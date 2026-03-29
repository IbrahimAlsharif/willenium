package tests.examples.wewill.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static base.ApiSetup.testData;
import static base.Setup.testCaseId;

public class WeWillPublicApiTest {

    @Test(priority = 1)
    public void verifyExamplePostContract() {
        testCaseId = "API-0001";
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

    @Test(priority = 2)
    public void verifyExamplePostCreationContract() {
        testCaseId = "API-0002";
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
