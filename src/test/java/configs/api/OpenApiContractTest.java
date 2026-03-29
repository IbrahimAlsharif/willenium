package configs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.filter.Filter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class OpenApiContractTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SPECIFICATION_TEMPLATE = "{\n" +
            "  \"openapi\": \"3.0.3\",\n" +
            "  \"info\": {\n" +
            "    \"title\": \"Spec %s\",\n" +
            "    \"version\": \"1.0\"\n" +
            "  },\n" +
            "  \"paths\": {\n" +
            "    \"/health\": {\n" +
            "      \"get\": {\n" +
            "        \"responses\": {\n" +
            "          \"200\": {\n" +
            "            \"description\": \"ok\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n";

    @AfterMethod
    public void resetContractState() {
        OpenApiContract.reset();
    }

    @Test
    public void verifyConfiguredContractCreatesValidationFilter() throws IOException {
        Path specPath = writeSpecFile("main-thread");
        ObjectNode apiData = contractValidation(true, specPath);

        OpenApiContract.configure(apiData);

        Assert.assertTrue(OpenApiContract.isEnabled(), "Contract validation should be enabled for the current thread");
        Assert.assertEquals(OpenApiContract.getSpecificationLocation(), specPath.toAbsolutePath().toString());
        Assert.assertNotNull(OpenApiContract.getValidationFilter(), "A validation filter should be created for enabled contracts");
    }

    @Test
    public void verifyContractStateIsThreadScoped() throws Exception {
        Path mainSpec = writeSpecFile("main");
        Path workerSpec = writeSpecFile("worker");

        OpenApiContract.configure(contractValidation(true, mainSpec));
        String mainThreadSpecification = OpenApiContract.getSpecificationLocation();
        Filter mainThreadFilter = OpenApiContract.getValidationFilter();

        AtomicReference<String> workerSpecification = new AtomicReference<>();
        AtomicReference<Filter> workerFilter = new AtomicReference<>();
        AtomicReference<Throwable> workerFailure = new AtomicReference<>();

        Thread worker = new Thread(() -> {
            try {
                OpenApiContract.configure(contractValidation(true, workerSpec));
                workerSpecification.set(OpenApiContract.getSpecificationLocation());
                workerFilter.set(OpenApiContract.getValidationFilter());
            } catch (Throwable throwable) {
                workerFailure.set(throwable);
            } finally {
                OpenApiContract.reset();
            }
        });

        worker.start();
        worker.join();

        if (workerFailure.get() != null) {
            throw new AssertionError("Worker thread should configure its own contract state", workerFailure.get());
        }

        Assert.assertEquals(mainThreadSpecification, mainSpec.toAbsolutePath().toString(), "Main thread should keep its configured specification");
        Assert.assertEquals(workerSpecification.get(), workerSpec.toAbsolutePath().toString(), "Worker thread should use its own specification");
        Assert.assertNotNull(mainThreadFilter, "Main thread should have a validation filter");
        Assert.assertNotNull(workerFilter.get(), "Worker thread should have a validation filter");
        Assert.assertNotSame(mainThreadFilter, workerFilter.get(), "Each thread should own an independent validation filter");
        Assert.assertEquals(OpenApiContract.getSpecificationLocation(), mainSpec.toAbsolutePath().toString(), "Worker configuration must not overwrite the main thread");
    }

    @Test
    public void verifyResetClearsCurrentThreadState() throws IOException {
        Path specPath = writeSpecFile("reset");

        OpenApiContract.configure(contractValidation(true, specPath));
        OpenApiContract.reset();

        Assert.assertFalse(OpenApiContract.isEnabled(), "Reset should disable contract validation for the current thread");
        Assert.assertNull(OpenApiContract.getSpecificationLocation(), "Reset should clear the stored specification location");
        Assert.assertNull(OpenApiContract.getValidationFilter(), "Reset should clear the validation filter");
    }

    private static ObjectNode contractValidation(boolean enabled, Path specificationPath) {
        ObjectNode apiData = OBJECT_MAPPER.createObjectNode();
        ObjectNode contractValidation = apiData.putObject("contractValidation");
        contractValidation.put("enabled", enabled);
        contractValidation.put("specificationPath", specificationPath.toString());
        return apiData;
    }

    private static Path writeSpecFile(String name) throws IOException {
        Path specificationFile = Files.createTempFile("openapi-contract-" + name, ".json");
        Files.writeString(specificationFile, SPECIFICATION_TEMPLATE.formatted(name));
        specificationFile.toFile().deleteOnExit();
        return specificationFile;
    }
}
