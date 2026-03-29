package configs.testRail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class TestRailConfig {
    private static final String CONFIG_RESOURCE = "testrail.properties";
    private static final String LOCAL_CONFIG_RESOURCE = "testrail.local.properties";
    private static final TestRailConfig INSTANCE = new TestRailConfig();

    private final Properties properties = new Properties();

    private TestRailConfig() {
        loadProperties(CONFIG_RESOURCE, false);
        loadProperties(LOCAL_CONFIG_RESOURCE, true);
    }

    TestRailConfig(Properties properties) {
        this.properties.putAll(properties);
    }

    private void loadProperties(String resourceName, boolean optional) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else if (!optional) {
                throw new IllegalStateException("Required TestRail config resource is missing: " + resourceName);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load TestRail config from " + resourceName, exception);
        }
    }

    public static TestRailConfig getInstance() {
        return INSTANCE;
    }

    public String getBaseUrl() {
        return getValue("testrail.baseUrl", "");
    }

    public String getApiUrl() {
        return getValue("testrail.apiUrl", "index.php?/api/v2/");
    }

    public String getUsername() {
        return getValue("testrail.username", "");
    }

    public String getPassword() {
        return getValue("testrail.password", "");
    }

    public String getProjectName() {
        return getValue("testrail.projectName", "Willenium");
    }

    public String getApiSetupCaseId() {
        return getValue("testrail.apiSetupCaseId", "");
    }

    public int getProjectId() {
        String value = getValue("testrail.projectId", "");
        if (value.isBlank()) {
            throw new IllegalStateException("Missing TestRail config: testrail.projectId");
        }

        try {
            int projectId = Integer.parseInt(value);
            if (projectId <= 0) {
                throw new IllegalStateException("Invalid TestRail config: testrail.projectId must be greater than 0");
            }
            return projectId;
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("Invalid TestRail config: testrail.projectId must be a number");
        }
    }

    public int getConnectTimeoutMillis() {
        return getPositiveInt("testrail.connectTimeoutMillis", 10000);
    }

    public int getReadTimeoutMillis() {
        return getPositiveInt("testrail.readTimeoutMillis", 30000);
    }

    public void validateRuntimeConfig() {
        validateRequired("testrail.baseUrl", getBaseUrl(), "TestRail base URL must be provided");
        validateRequired("testrail.username", getUsername(), "TestRail username must be provided");
        validateRequired("testrail.password", getPassword(), "TestRail password or API key must be provided");
        getProjectId();
    }

    private String getValue(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty.trim();
        }

        String environmentValue = System.getenv(toEnvironmentKey(key));
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }

        String fileValue = properties.getProperty(key);
        if (fileValue != null && !fileValue.isBlank()) {
            return fileValue.trim();
        }

        return defaultValue;
    }

    private int getPositiveInt(String key, int defaultValue) {
        String value = getValue(key, String.valueOf(defaultValue));
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) {
                throw new IllegalStateException("Invalid TestRail config: " + key + " must be greater than 0");
            }
            return parsedValue;
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("Invalid TestRail config: " + key + " must be a number");
        }
    }

    private void validateRequired(String key, String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message + " (" + key + ")");
        }
    }

    private String toEnvironmentKey(String key) {
        return key.toUpperCase(Locale.ROOT).replace('.', '_');
    }
}
