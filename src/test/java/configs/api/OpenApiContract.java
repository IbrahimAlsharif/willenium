package configs.api;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.filter.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class OpenApiContract {
    private static boolean enabled;
    private static String specificationLocation;
    private static Filter validationFilter;

    private OpenApiContract() {
    }

    public static void configure(JsonNode apiData) {
        reset();

        if (apiData == null || !apiData.has("contractValidation")) {
            return;
        }

        JsonNode contractValidation = apiData.get("contractValidation");
        enabled = contractValidation.path("enabled").asBoolean(false);
        if (!enabled) {
            return;
        }

        JsonNode specificationNode = contractValidation.path("specificationPath");
        String configuredSpecification = specificationNode.isMissingNode() ? "" : specificationNode.asText().trim();
        if (configuredSpecification.isEmpty()) {
            throw new IllegalStateException("API contract validation is enabled but specificationPath is missing from the api.contractValidation test data.");
        }

        specificationLocation = resolveSpecificationLocation(configuredSpecification);
        OpenApiInteractionValidator validator = OpenApiInteractionValidator.createFor(specificationLocation).build();
        validationFilter = new OpenApiValidationFilter(validator);
    }

    public static Filter getValidationFilter() {
        return validationFilter;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static String getSpecificationLocation() {
        return specificationLocation;
    }

    public static void reset() {
        enabled = false;
        specificationLocation = null;
        validationFilter = null;
    }

    private static String resolveSpecificationLocation(String configuredSpecification) {
        if (configuredSpecification.startsWith("http://") || configuredSpecification.startsWith("https://")) {
            return configuredSpecification;
        }

        Path specificationPath = Paths.get(configuredSpecification);
        if (!specificationPath.isAbsolute()) {
            specificationPath = specificationPath.toAbsolutePath();
        }

        if (!Files.exists(specificationPath)) {
            throw new IllegalStateException("OpenAPI specification file was not found: " + specificationPath);
        }

        return specificationPath.toString();
    }
}
