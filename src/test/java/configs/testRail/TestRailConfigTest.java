package configs.testRail;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

public class TestRailConfigTest {

    @Test
    public void verifyProjectIdMustBeConfigured() {
        TestRailConfig config = config(
                "testrail.baseUrl", "https://demo.testrail.io/",
                "testrail.username", "user@example.com",
                "testrail.password", "secret"
        );

        IllegalStateException exception = Assert.expectThrows(IllegalStateException.class, config::validateRuntimeConfig);

        Assert.assertTrue(exception.getMessage().contains("testrail.projectId"));
    }

    @Test
    public void verifyProjectIdMustBeNumericAndPositive() {
        TestRailConfig nonNumeric = config("testrail.projectId", "abc");
        TestRailConfig nonPositive = config("testrail.projectId", "0");

        IllegalStateException nonNumericException = Assert.expectThrows(IllegalStateException.class, nonNumeric::getProjectId);
        IllegalStateException nonPositiveException = Assert.expectThrows(IllegalStateException.class, nonPositive::getProjectId);

        Assert.assertTrue(nonNumericException.getMessage().contains("must be a number"));
        Assert.assertTrue(nonPositiveException.getMessage().contains("greater than 0"));
    }

    @Test
    public void verifyApiSetupCaseIdIsOptional() {
        TestRailConfig config = config();

        Assert.assertEquals(config.getApiSetupCaseId(), "", "API setup case IDs should be optional by default");
    }

    private static TestRailConfig config(String... values) {
        Properties properties = new Properties();
        for (int index = 0; index < values.length; index += 2) {
            properties.setProperty(values[index], values[index + 1]);
        }
        return new TestRailConfig(properties);
    }
}
