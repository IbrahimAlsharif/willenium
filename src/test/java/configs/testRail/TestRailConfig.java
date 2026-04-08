package configs.testRail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TestRailConfig {

    private static final String DEFAULT_API_URL = "index.php?/api/v2/";
    private static final String DEFAULT_RUN_NAME = "Willenium";
    private static final int DEFAULT_PRIORITY_ID = 3;
    private static final Path LOCAL_PROPERTIES_PATH = Paths.get("testrail.local.properties");
    private static final Properties LOCAL_PROPERTIES = loadLocalProperties();

    private final String baseUrl;
    private final String apiUrl;
    private final String username;
    private final String password;
    private final Integer projectId;
    private final Integer suiteId;
    private final String runName;
    private final int priorityId;

    public TestRailConfig() {
        this.baseUrl = getOptional("WILLENIUM_TESTRAIL_BASE_URL");
        this.apiUrl = getValue("WILLENIUM_TESTRAIL_API_URL", DEFAULT_API_URL);
        this.username = getOptional("WILLENIUM_TESTRAIL_USERNAME");
        this.password = getOptional("WILLENIUM_TESTRAIL_PASSWORD");
        this.projectId = getOptionalInt("WILLENIUM_TESTRAIL_PROJECT_ID");
        this.suiteId = getOptionalInt("WILLENIUM_TESTRAIL_SUITE_ID");
        this.runName = getValue("WILLENIUM_TESTRAIL_RUN_NAME", DEFAULT_RUN_NAME);
        this.priorityId = getPositiveInt("WILLENIUM_TESTRAIL_PRIORITY_ID", DEFAULT_PRIORITY_ID);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public String getRunName() {
        return runName;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public boolean isConfigured() {
        return notBlank(baseUrl)
                && notBlank(username)
                && notBlank(password)
                && projectId != null;
    }

    public void requireConfigured(String purpose) {
        if (isConfigured()) {
            return;
        }

        throw new IllegalStateException(
                "TestRail is not fully configured for " + purpose + ". Required keys: "
                        + "WILLENIUM_TESTRAIL_BASE_URL, WILLENIUM_TESTRAIL_USERNAME, "
                        + "WILLENIUM_TESTRAIL_PASSWORD, WILLENIUM_TESTRAIL_PROJECT_ID."
        );
    }

    private static String getValue(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (notBlank(systemProperty)) {
            return systemProperty.trim();
        }

        String environmentValue = System.getenv(key);
        if (notBlank(environmentValue)) {
            return environmentValue.trim();
        }

        String localFileValue = LOCAL_PROPERTIES.getProperty(key);
        if (notBlank(localFileValue)) {
            return localFileValue.trim();
        }

        return defaultValue;
    }

    private static String getOptional(String key) {
        String value = getValue(key, "");
        return value.isBlank() ? null : value;
    }

    private static Integer getOptionalInt(String key) {
        String value = getOptional(key);
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(key + " must be a valid integer, but was: " + value);
        }
    }

    private static int getPositiveInt(String key, int defaultValue) {
        String value = getValue(key, String.valueOf(defaultValue));
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static Properties loadLocalProperties() {
        Properties properties = new Properties();
        if (!Files.exists(LOCAL_PROPERTIES_PATH)) {
            return properties;
        }

        try (InputStream inputStream = Files.newInputStream(LOCAL_PROPERTIES_PATH)) {
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + LOCAL_PROPERTIES_PATH.toAbsolutePath(), exception);
        }
    }
}
