package configs.api;

import io.restassured.response.Response;

public final class ApiContext {
    private static final int MAX_CAPTURE_LENGTH = 4000;
    private static final ThreadLocal<ApiExchange> EXCHANGE = ThreadLocal.withInitial(ApiExchange::new);

    private ApiContext() {
    }

    public static void clear() {
        EXCHANGE.remove();
    }

    public static void recordRequest(String requestName, String method, String url, Object requestBody) {
        ApiExchange exchange = EXCHANGE.get();
        exchange.requestName = requestName;
        exchange.method = method;
        exchange.url = url;
        exchange.requestBody = requestBody == null ? "" : trimToLimit(String.valueOf(requestBody));
    }

    public static void recordResponse(Response response) {
        ApiExchange exchange = EXCHANGE.get();
        exchange.statusCode = response.statusCode();
        exchange.responseBody = trimToLimit(response.getBody().prettyPrint());
    }

    public static void recordContractValidation(boolean contractValidationEnabled, String specificationLocation) {
        ApiExchange exchange = EXCHANGE.get();
        exchange.contractValidationEnabled = contractValidationEnabled;
        exchange.specificationLocation = specificationLocation;
    }

    public static boolean hasExchange() {
        ApiExchange exchange = EXCHANGE.get();
        return exchange.url != null || exchange.responseBody != null;
    }

    public static String buildReportDetails() {
        ApiExchange exchange = EXCHANGE.get();
        StringBuilder builder = new StringBuilder();

        if (exchange.requestName != null && !exchange.requestName.isBlank()) {
            builder.append("API request: ").append(exchange.requestName).append(System.lineSeparator());
        }

        if (exchange.method != null && exchange.url != null) {
            builder.append("Request: ").append(exchange.method).append(" ").append(exchange.url).append(System.lineSeparator());
        }

        if (exchange.contractValidationEnabled && exchange.specificationLocation != null) {
            builder.append("OpenAPI contract: ").append(exchange.specificationLocation).append(System.lineSeparator());
        }

        if (exchange.requestBody != null && !exchange.requestBody.isBlank()) {
            builder.append("Request body:").append(System.lineSeparator()).append(exchange.requestBody).append(System.lineSeparator());
        }

        if (exchange.statusCode != null) {
            builder.append("Response status: ").append(exchange.statusCode).append(System.lineSeparator());
        }

        if (exchange.responseBody != null && !exchange.responseBody.isBlank()) {
            builder.append("Response body:").append(System.lineSeparator()).append(exchange.responseBody);
        }

        return builder.toString().trim();
    }

    private static String trimToLimit(String value) {
        if (value.length() <= MAX_CAPTURE_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_CAPTURE_LENGTH) + System.lineSeparator() + "... truncated ...";
    }

    private static final class ApiExchange {
        private String requestName;
        private String method;
        private String url;
        private String requestBody;
        private boolean contractValidationEnabled;
        private String specificationLocation;
        private Integer statusCode;
        private String responseBody;
    }
}
