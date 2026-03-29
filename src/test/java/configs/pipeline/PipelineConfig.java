package configs.pipeline;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Locale;

public class PipelineConfig {
    public static final boolean testRailReport = getBoolean("WILLENIUM_TESTRAIL_REPORT", false);
    public static final boolean isBrowserHeadless = getBoolean("WILLENIUM_BROWSER_HEADLESS", System.getenv("CI") != null);
    public static final boolean isBrowserIncognito = getBoolean("WILLENIUM_BROWSER_INCOGNITO", true);
    public static final boolean maximizeBrowserWindow = getBoolean("WILLENIUM_BROWSER_MAXIMIZE", !isBrowserHeadless);
    public static final boolean autoOpenExtentReport = getBoolean("WILLENIUM_AUTO_OPEN_EXTENT_REPORT", System.getenv("CI") == null);
    public static final boolean highlightInteractions = getBoolean("WILLENIUM_UI_HIGHLIGHT_INTERACTIONS", false);
    public static final boolean verifyTypedText = getBoolean("WILLENIUM_UI_VERIFY_TYPED_TEXT", true);
    public static final int uiWaitTimeoutSeconds = getPositiveInt("WILLENIUM_UI_WAIT_TIMEOUT_SECONDS", 30);
    public static final int uiInteractionRetryAttempts = getPositiveInt("WILLENIUM_UI_INTERACTION_RETRY_ATTEMPTS", 3);
    public static final int uiInteractionRetryDelayMillis = getPositiveInt("WILLENIUM_UI_INTERACTION_RETRY_DELAY_MILLIS", 250);
    public static final int pageSourceSnippetLength = getPositiveInt("WILLENIUM_PAGE_SOURCE_SNIPPET_CHARS", 4000);
    public static final String browserWindowSize = getValue("WILLENIUM_BROWSER_WINDOW_SIZE", "1366x768");
    public static final PageLoadStrategy browserPageLoadStrategy = getPageLoadStrategy("WILLENIUM_PAGE_LOAD_STRATEGY", PageLoadStrategy.EAGER);

    private PipelineConfig() {
        // Utility config holder
    }

    public static Dimension getBrowserWindowDimension() {
        String[] parts = browserWindowSize.toLowerCase(Locale.ROOT).split("x");
        if (parts.length != 2) {
            return new Dimension(1366, 768);
        }

        try {
            return new Dimension(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException exception) {
            return new Dimension(1366, 768);
        }
    }

    private static String getValue(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty.trim();
        }

        String environmentValue = System.getenv(key);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }

        return defaultValue;
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key, String.valueOf(defaultValue)).toLowerCase(Locale.ROOT);
        return value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("on");
    }

    private static int getPositiveInt(String key, int defaultValue) {
        String value = getValue(key, String.valueOf(defaultValue));
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private static PageLoadStrategy getPageLoadStrategy(String key, PageLoadStrategy defaultValue) {
        String value = getValue(key, defaultValue.name()).toUpperCase(Locale.ROOT);
        try {
            return PageLoadStrategy.valueOf(value);
        } catch (IllegalArgumentException exception) {
            return defaultValue;
        }
    }
}
