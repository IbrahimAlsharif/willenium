package configs.api;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.filter.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class OpenApiContract {
    private static final ThreadLocal<ContractState> CONTRACT_STATE = ThreadLocal.withInitial(ContractState::new);

    private OpenApiContract() {
    }

    public static void configure(JsonNode apiData) {
        reset();

        if (apiData == null || !apiData.has("contractValidation")) {
            return;
        }

        JsonNode contractValidation = apiData.get("contractValidation");
        ContractState contractState = CONTRACT_STATE.get();
        contractState.enabled = contractValidation.path("enabled").asBoolean(false);
        if (!contractState.enabled) {
            return;
        }

        JsonNode specificationNode = contractValidation.path("specificationPath");
        String configuredSpecification = specificationNode.isMissingNode() ? "" : specificationNode.asText().trim();
        if (configuredSpecification.isEmpty()) {
            throw new IllegalStateException("API contract validation is enabled but specificationPath is missing from the api.contractValidation test data.");
        }

        contractState.specificationLocation = resolveSpecificationLocation(configuredSpecification);
        OpenApiInteractionValidator validator = OpenApiInteractionValidator.createFor(contractState.specificationLocation).build();
        contractState.validationFilter = new OpenApiValidationFilter(validator);
    }

    public static Filter getValidationFilter() {
        return CONTRACT_STATE.get().validationFilter;
    }

    public static boolean isEnabled() {
        return CONTRACT_STATE.get().enabled;
    }

    public static String getSpecificationLocation() {
        return CONTRACT_STATE.get().specificationLocation;
    }

    public static void reset() {
        CONTRACT_STATE.remove();
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

    private static final class ContractState {
        private boolean enabled;
        private String specificationLocation;
        private Filter validationFilter;
    }
}
