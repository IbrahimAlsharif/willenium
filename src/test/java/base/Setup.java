package base;

import configs.BrowserOptions;
import configs.pipeline.PipelineConfig;
import configs.pipeline.RemoteExecutionConfig;
import configs.testRail.APIException;
import configs.testRail.TestRailCase;
import configs.testRail.TestRailManager;
import configs.testdata.TestData;
import configs.testdata.TestDataFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;


public class Setup {

    public static WebDriver driver;
    public static TestData testData;
    public static WebDriverWait wait;
    public static final TestRailManager testRail = new TestRailManager();
    private static String uiInitializationBlockerMessage;


    @TestRailCase("9379")
    @Test(priority = 1)
    @Parameters({"language", "branch", "browser"})
    public void setUpLocalDriver(String language, String branch, String browser) throws Exception {
        resetSharedState();
        cleanScreenshotsDirectory();
        testData = TestDataFactory.getTestData(branch, language);
        initializePreferredDriver(browser, testData.getBaseUrl(language).asText());
        Assert.assertTrue(true);
    }

    @TestRailCase("9380")
    @Test(priority = 2, groups = {"haltWhenFail"})
    @Parameters({"language"})
    public void openWebsite(String language){
        if (driver == null || testData == null) {
            throw new SkipException("Skipping openWebsite because setup did not initialize driver and test data");
        }
        driver.get(testData.getBaseUrl(language).asText());
        Go.setMainTab();
        configureBrowserWindow();
    }

    @TestRailCase("9381")
    @Test(priority = 1)
    @Parameters({"language", "branch", "browser"})
    public void setUpRemoteDriver(String language, String branch, String browser) throws Exception {
        resetSharedState();
        cleanScreenshotsDirectory();
        testData = TestDataFactory.getTestData(branch, language);
        initializeRemoteDriver(browser, RemoteExecutionConfig.fromEnvironment());
        Assert.assertTrue(true);
    }


    private void initializePreferredDriver(String browser, String url) throws IOException, APIException {
        RemoteExecutionConfig remoteExecutionConfig = RemoteExecutionConfig.fromEnvironment();

        if (remoteExecutionConfig.isRemoteOnly()) {
            initializeRemoteDriver(browser, remoteExecutionConfig);
            return;
        }

        try {
            initializeLocalDriver(browser, url);
        } catch (IllegalStateException exception) {
            if (shouldFallbackToRemote(exception, remoteExecutionConfig)) {
                initializeRemoteDriver(browser, remoteExecutionConfig);
                return;
            }
            throw augmentLocalFailure(exception, remoteExecutionConfig);
        }
    }

    private void initializeRemoteDriver(String browser, RemoteExecutionConfig remoteExecutionConfig) throws IOException, APIException {
        if (PipelineConfig.testRailReport) {
            TestRailManager testRailManager = new TestRailManager();
            Go.testRunId = testRailManager.createTestRun("Sentra", 2);
        }
        ensureRemoteExecutionConfigured(remoteExecutionConfig);
        BrowserOptions configuredBrowserOptions = new BrowserOptions();
        AbstractDriverOptions<?> browserOptions = null;
        String platform = remoteExecutionConfig.getRemotePlatform();
        String browserVersion = remoteExecutionConfig.getRemoteBrowserVersion();
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = configuredBrowserOptions.getChromeOptions(PipelineConfig.isBrowserHeadless, PipelineConfig.isBrowserIncognito);
            applyRemoteMetadata(chromeOptions, platform, browserVersion);
            if (!browserVersion.isBlank()) {
                chromeOptions.setBrowserVersion(browserVersion);
            }
            browserOptions = chromeOptions;
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = configuredBrowserOptions.getFirefoxOptions(PipelineConfig.isBrowserHeadless, PipelineConfig.isBrowserIncognito);
            applyRemoteMetadata(firefoxOptions, platform, browserVersion);
            browserOptions = firefoxOptions;
        } else if (browser.equalsIgnoreCase("safari")) {
            SafariOptions safariOptions = configuredBrowserOptions.getSafariOptions(PipelineConfig.isBrowserHeadless, PipelineConfig.isBrowserIncognito);
            applyRemoteMetadata(safariOptions, platform, browserVersion);
            browserOptions = safariOptions;
        } else {
            throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }
        if (remoteExecutionConfig.isLambdaTest()) {
            browserOptions.setCapability("LT:Options", new BrowserOptions().getLambdaTestOptions(remoteExecutionConfig));
        }
        driver = new RemoteWebDriver(new URL(remoteExecutionConfig.getRemoteUrl()), browserOptions);
        configureHelperComponents();
    }

    private void initializeLocalDriver(String browser, String url) throws APIException, IOException {
        if (PipelineConfig.testRailReport) {
            TestRailManager testRailManager = new TestRailManager();
            Go.testRunId = testRailManager.createTestRun("Sentra", 2);
        }
        if (browser.equalsIgnoreCase("chrome")) {
            initializeManagedLocalDriver(
                    "Chrome",
                    () -> WebDriverManager.chromedriver()
                            .capabilities(new BrowserOptions().getChromeOptions(PipelineConfig.isBrowserHeadless, PipelineConfig.isBrowserIncognito))
                            .create()
            );
        } else if (browser.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        } else {
            initializeManagedLocalDriver(
                    "Firefox",
                    () -> WebDriverManager.firefoxdriver()
                            .capabilities(new BrowserOptions().getFirefoxOptions(PipelineConfig.isBrowserHeadless, PipelineConfig.isBrowserIncognito))
                            .create()
            );
        }
        configureHelperComponents();
    }

    public static String getUiInitializationBlockerMessage() {
        return uiInitializationBlockerMessage;
    }

    public static void ensureUiSessionReady() {
        if (uiInitializationBlockerMessage != null) {
            throw new SkipException(uiInitializationBlockerMessage);
        }
        if (driver == null || wait == null || testData == null) {
            throw new SkipException("UI setup did not complete. Run the suite through a setup XML and fix any setup failure before running page assertions.");
        }
    }

    public static boolean hasActiveUiSession() {
        if (driver == null) {
            return false;
        }

        try {
            if (driver instanceof RemoteWebDriver remoteWebDriver && remoteWebDriver.getSessionId() == null) {
                return false;
            }
            driver.getWindowHandle();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private void configureHelperComponents() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(PipelineConfig.uiWaitTimeoutSeconds));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        Go.initialize(driver, javascriptExecutor, wait);
        Finder.initialize(driver, wait);
    }

    private void configureBrowserWindow() {
        try {
            if (PipelineConfig.maximizeBrowserWindow && !PipelineConfig.isBrowserHeadless) {
                driver.manage().window().maximize();
            } else {
                driver.manage().window().setSize(PipelineConfig.getBrowserWindowDimension());
            }
        } catch (Exception exception) {
            System.out.println("Unable to apply browser window configuration: " + exception.getMessage());
        }
    }

    private void resetSharedState() {
        driver = null;
        wait = null;
        testData = null;
        uiInitializationBlockerMessage = null;
    }

    private void validateLocalExecutionEnvironment(String browserName) {
        try (ServerSocket socket = new ServerSocket(0, 0, InetAddress.getLoopbackAddress())) {
            socket.setReuseAddress(true);
        } catch (IOException exception) {
            uiInitializationBlockerMessage = String.format(
                    "%s local driver startup is blocked because this environment cannot bind a localhost port. Run the suite from a normal local terminal or use remote driver execution instead.",
                    browserName
            );
            throw new IllegalStateException(uiInitializationBlockerMessage, exception);
        }
    }

    private String buildDriverStartupFailureMessage(String browserName, RuntimeException exception) {
        String rootMessage = exception.getMessage();
        if (rootMessage != null && rootMessage.contains("Unable to find a free port")) {
            return String.format(
                    "%s local driver startup failed because no localhost port could be opened. This usually means the run is happening inside a restricted sandbox. Run the suite from a normal local terminal or use remote driver execution instead.",
                    browserName
            );
        }
        return String.format("%s local driver startup failed: %s", browserName, rootMessage);
    }

    private void initializeManagedLocalDriver(String browserName, LocalDriverFactory localDriverFactory) {
        validateLocalExecutionEnvironment(browserName);
        try {
            driver = localDriverFactory.create();
        } catch (RuntimeException exception) {
            uiInitializationBlockerMessage = buildDriverStartupFailureMessage(browserName, exception);
            throw new IllegalStateException(uiInitializationBlockerMessage, exception);
        }
    }

    private boolean shouldFallbackToRemote(IllegalStateException exception, RemoteExecutionConfig remoteExecutionConfig) {
        if (!remoteExecutionConfig.isAutoMode() || !remoteExecutionConfig.isRemoteConfigured()) {
            return false;
        }
        return isLocalExecutionBlocker(exception);
    }

    private boolean isLocalExecutionBlocker(IllegalStateException exception) {
        String message = exception.getMessage();
        if (message == null) {
            return false;
        }
        return message.contains("localhost port")
                || message.contains("cannot bind")
                || message.contains("sandbox")
                || message.contains("free port");
    }

    private IllegalStateException augmentLocalFailure(IllegalStateException exception, RemoteExecutionConfig remoteExecutionConfig) {
        if (!remoteExecutionConfig.isAutoMode() || remoteExecutionConfig.isRemoteConfigured()) {
            return exception;
        }
        return new IllegalStateException(
                exception.getMessage() + " Automatic remote fallback is available in execution mode 'auto' when remote configuration is present. "
                        + remoteExecutionConfig.describeRemoteRequirement(),
                exception
        );
    }

    private void ensureRemoteExecutionConfigured(RemoteExecutionConfig remoteExecutionConfig) {
        if (!remoteExecutionConfig.isRemoteConfigured()) {
            throw new IllegalStateException(
                    "Remote driver execution was requested but no remote URL is configured. " + remoteExecutionConfig.describeRemoteRequirement()
            );
        }
        if (remoteExecutionConfig.isLambdaTest() && !remoteExecutionConfig.hasRemoteCredentials()) {
            throw new IllegalStateException(
                    "Remote LambdaTest execution requires WILLENIUM_REMOTE_USERNAME and WILLENIUM_REMOTE_ACCESS_KEY."
            );
        }
    }

    private void applyRemoteMetadata(AbstractDriverOptions<?> browserOptions, String platform, String browserVersion) {
        if (!platform.isBlank()) {
            browserOptions.setPlatformName(platform);
        }
        if (!browserVersion.isBlank()) {
            browserOptions.setBrowserVersion(browserVersion);
        }
    }

    @FunctionalInterface
    private interface LocalDriverFactory {
        WebDriver create();
    }

    private void cleanScreenshotsDirectory() {
        Path screenshotsPath = Paths.get("screenshots");
        try {
            if (Files.exists(screenshotsPath)) {
                FileUtils.deleteDirectory(screenshotsPath.toFile());
            }
            Files.createDirectories(screenshotsPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to clean screenshots directory", e);
        }
    }

}
