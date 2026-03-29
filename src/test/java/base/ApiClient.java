package base;

import configs.api.ApiContext;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
    private static RequestSpecification requestSpecification;
    private static String baseUrl;

    public static void initialize(RequestSpecification specification, String configuredBaseUrl) {
        requestSpecification = specification;
        baseUrl = configuredBaseUrl;
    }

    public static Response get(String endpoint, String requestName) {
        return execute("GET", endpoint, null, requestName);
    }

    public static Response post(String endpoint, Object requestBody, String requestName) {
        return execute("POST", endpoint, requestBody, requestName);
    }

    public static Response put(String endpoint, Object requestBody, String requestName) {
        return execute("PUT", endpoint, requestBody, requestName);
    }

    public static Response delete(String endpoint, String requestName) {
        return execute("DELETE", endpoint, null, requestName);
    }

    private static Response execute(String method, String endpoint, Object requestBody, String requestName) {
        ensureInitialized();
        String requestUrl = buildUrl(endpoint);
        ApiContext.recordRequest(requestName, method, requestUrl, requestBody);

        RequestSpecification request = RestAssured.given().spec(requestSpecification);
        if (requestBody != null) {
            request.body(requestBody);
        }

        Response response;
        switch (method) {
            case "POST":
                response = request.when().post(endpoint).then().extract().response();
                break;
            case "PUT":
                response = request.when().put(endpoint).then().extract().response();
                break;
            case "DELETE":
                response = request.when().delete(endpoint).then().extract().response();
                break;
            default:
                response = request.when().get(endpoint).then().extract().response();
                break;
        }

        ApiContext.recordResponse(response);
        return response;
    }

    private static void ensureInitialized() {
        if (requestSpecification == null || baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("API client is not initialized. Run the suite through an API setup XML first.");
        }
    }

    private static String buildUrl(String endpoint) {
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }

        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedEndpoint = endpoint.startsWith("/") ? endpoint : "/" + endpoint;
        return normalizedBaseUrl + normalizedEndpoint;
    }
}
