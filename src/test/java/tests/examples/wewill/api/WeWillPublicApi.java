package tests.examples.wewill.api;

import base.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;

import static base.ApiSetup.testData;

public class WeWillPublicApi {

    public static Response getExamplePost() {
        JsonNode getOnePostData = testData.getApiData().get("posts").get("getOne");
        return ApiClient.get(
                getOnePostData.get("endpoint").asText(),
                getOnePostData.get("description").asText()
        );
    }

    public static Response createExamplePost() {
        JsonNode createPostData = testData.getApiData().get("posts").get("createOne");
        return ApiClient.post(
                createPostData.get("endpoint").asText(),
                createPostData.get("requestBody"),
                createPostData.get("description").asText()
        );
    }
}
