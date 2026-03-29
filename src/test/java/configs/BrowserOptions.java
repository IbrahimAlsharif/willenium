package configs;

import configs.pipeline.RemoteExecutionConfig;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;

public class BrowserOptions {
    public ChromeOptions getChromeOptions(boolean isHeadless, boolean isIncognito) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");

        options.addArguments("--disable-web-security");
        // Disables web security features like the same-origin policy. Useful when testing websites that require cross-origin access.

        options.addArguments("--allow-running-insecure-content");
        // Allows Chrome to load insecure (HTTP) content on secure (HTTPS) pages, often needed in testing environments.

        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // Sets the page load strategy to 'EAGER', meaning Selenium considers the page loaded once the initial HTML is loaded and parsed (before other resources like images or CSS load fully).

        if (isIncognito) {
            options.addArguments("--incognito");
        }
        // Headless
        if (isHeadless) {
            options.addArguments("--disable-blink-features=AutomationControlled");

            options.addArguments("--disable-features=VizDisplayCompositor");
            // Disables the VizDisplayCompositor feature. Helps avoid issues related to GPU rendering when running in headless mode.

            options.addArguments("enable-automation");
            // Indicates that the browser is being controlled by automated software (used to notify websites).

            options.addArguments("--crash-dumps-dir=/tmp");
            // Specifies where crash dumps should be stored in case of a browser crash (uses the /tmp directory on Linux).

            options.addArguments("--no-sandbox");
            // Disables the Chrome sandbox, which can cause issues in CI environments like GitHub Actions or Docker.

            options.addArguments("--disable-dev-shm-usage");
            // Disables shared memory usage, forcing Chrome to write to disk instead. Necessary in environments with limited shared memory (like Docker).

            options.addArguments("--aggressive-cache-discard");
            // Forces aggressive discarding of cached resources. Helps ensure fresh data is used during tests.

            options.addArguments("--disable-cache");
            // Completely disables the browser cache to avoid loading outdated resources.

            options.addArguments("--disable-application-cache");
            // Disables the application cache, typically used for storing offline resources in web apps.

            options.addArguments("--disable-offline-load-stale-cache");
            // Prevents Chrome from using stale cached content when the system is offline or the server is unreachable.

            options.addArguments("--disk-cache-size=0");
            // Sets the disk cache size to 0, effectively disabling disk caching.

            options.addArguments("--headless");
            // Runs Chrome in headless mode (no GUI). Essential for running tests in environments like CI or when no display is available.

            options.addArguments("window-size=1366x768");
            // Sets the window size for the headless browser. Provides a consistent viewport for testing.

            options.addArguments("--disable-gpu");
            // Disables GPU hardware acceleration, as it's not typically used or available in headless mode.

            options.addArguments("--dns-prefetch-disable");
            // Disables DNS prefetching to avoid prematurely resolving domain names, which can sometimes cause errors in automated tests.

            options.addArguments("--no-proxy-server");
            // Ensures that Chrome connects directly to the internet without using a proxy server.

            options.addArguments("--log-level=3");
            // Sets the log level to FATAL, suppressing most logs and only showing critical errors.

            options.addArguments("--silent");
            // Runs Chrome in silent mode, reducing unnecessary log output.

            options.addArguments("--disable-browser-side-navigation");
            // Disables browser-side navigation optimizations that can cause issues in automated tests. (This flag is deprecated in newer Chrome versions.)


        }
        return options;
    }

    public SafariOptions getSafariOptions(boolean isHeadless, boolean isIncognito) {
        SafariOptions options = new SafariOptions();
        options.setAutomaticInspection(false); // Disables automatic inspection of pages (useful for debugging)
        options.setAutomaticProfiling(false); // Disables automatic profiling (helps with performance)
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    public FirefoxOptions getFirefoxOptions(boolean isHeadless, boolean isIncognito) {
        FirefoxOptions options = new FirefoxOptions();
        //Todo Add emulation device
        options.addPreference("security.fileuri.strict_origin_policy", false);

        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // Sets the page load strategy to 'EAGER', meaning Selenium considers the page loaded once the initial HTML is loaded and parsed (before other resources like images or CSS load fully).

        options.addArguments("--disable-blink-features=AutomationControlled");

        if (isIncognito) {
            options.addArguments("--incognito");
        }
        // Headless
        if (isHeadless) {
            options.addArguments("--disable-blink-features=AutomationControlled");

            options.addArguments("--disable-features=VizDisplayCompositor");
            // Disables the VizDisplayCompositor feature. Helps avoid issues related to GPU rendering when running in headless mode.

            options.addArguments("enable-automation");
            // Indicates that the browser is being controlled by automated software (used to notify websites).

            options.addArguments("--crash-dumps-dir=/tmp");
            // Specifies where crash dumps should be stored in case of a browser crash (uses the /tmp directory on Linux).

            options.addArguments("--no-sandbox");
            // Disables the Chrome sandbox, which can cause issues in CI environments like GitHub Actions or Docker.

            options.addArguments("--disable-dev-shm-usage");
            // Disables shared memory usage, forcing Chrome to write to disk instead. Necessary in environments with limited shared memory (like Docker).

            options.addArguments("--aggressive-cache-discard");
            // Forces aggressive discarding of cached resources. Helps ensure fresh data is used during tests.

            options.addArguments("--disable-cache");
            // Completely disables the browser cache to avoid loading outdated resources.

            options.addArguments("--disable-application-cache");
            // Disables the application cache, typically used for storing offline resources in web apps.

            options.addArguments("--disable-offline-load-stale-cache");
            // Prevents Chrome from using stale cached content when the system is offline or the server is unreachable.

            options.addArguments("--disk-cache-size=0");
            // Sets the disk cache size to 0, effectively disabling disk caching.

            options.addArguments("--headless");
            // Runs Chrome in headless mode (no GUI). Essential for running tests in environments like CI or when no display is available.

            options.addArguments("window-size=1366x768");
            // Sets the window size for the headless browser. Provides a consistent viewport for testing.

            options.addArguments("--disable-gpu");
            // Disables GPU hardware acceleration, as it's not typically used or available in headless mode.

            options.addArguments("--dns-prefetch-disable");
            // Disables DNS prefetching to avoid prematurely resolving domain names, which can sometimes cause errors in automated tests.

            options.addArguments("--no-proxy-server");
            // Ensures that Chrome connects directly to the internet without using a proxy server.

            options.addArguments("--log-level=3");
            // Sets the log level to FATAL, suppressing most logs and only showing critical errors.

            options.addArguments("--silent");
            // Runs Chrome in silent mode, reducing unnecessary log output.

            options.addArguments("--disable-browser-side-navigation");
            // Disables browser-side navigation optimizations that can cause issues in automated tests. (This flag is deprecated in newer Chrome versions.)

            options.addArguments("--disable-web-security");
            // Disables web security features like the same-origin policy. Useful when testing websites that require cross-origin access.

            options.addArguments("--allow-running-insecure-content");
            // Allows Chrome to load insecure (HTTP) content on secure (HTTPS) pages, often needed in testing environments.

        }
        return options;
    }

    public HashMap<String, Object> getLambdaTestOptions(RemoteExecutionConfig remoteExecutionConfig) {
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", remoteExecutionConfig.getRemoteUsername());
        ltOptions.put("accessKey", remoteExecutionConfig.getRemoteAccessKey());
        ltOptions.put("project", remoteExecutionConfig.getRemoteProject());
        ltOptions.put("build", remoteExecutionConfig.getRemoteBuild());
        ltOptions.put("selenium_version", "4.22.0");
        ltOptions.put("w3c", true);
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        ltOptions.put("console", true);
        ltOptions.put("smartUI.project", "smartui");
        return ltOptions;
    }
}
