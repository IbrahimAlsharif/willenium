package base;

import configs.BrowserOptions;
import configs.pipeline.PipelineConfig;
import configs.testRail.APIException;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;


public class Setup {

    public static WebDriver driver;
    public static TestData testData;
    public static WebDriverWait wait;
    public static String testCaseId;
    public static final TestRailManager testRail = new TestRailManager();


    @Test(priority = 1)
    @Parameters({"language", "branch", "browser"})
    public void setUpLocalDriver(String language, String branch, String browser) throws Exception {
        testCaseId = "9379";
        cleanScreenshotsDirectory();
        testData = TestDataFactory.getTestData(branch, language);
        initializeLocalDriver(browser, testData.getBaseUrl(language).asText());
        Assert.assertTrue(true);
    }

    @Test(priority = 2, groups = {"haltWhenFail"})
    @Parameters({"language"})
    public void openWebsite(String language){
        testCaseId = "9380";
        driver.get(testData.getBaseUrl(language).asText());
        Go.setMainTab();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    @Parameters({"language", "branch", "browser"})
    public void setUpRemoteDriver(String language, String branch, String browser) throws Exception {
        testCaseId = "9381";
        cleanScreenshotsDirectory();
        testData = TestDataFactory.getTestData(branch, language);
        initializeRemoteDriver(browser);
        Assert.assertTrue(true);
    }


    private void initializeRemoteDriver(String browser) throws IOException, APIException {
        if (PipelineConfig.testRailReport) {
            TestRailManager testRailManager = new TestRailManager();
            Go.testRunId = testRailManager.createTestRun("Sentra", 2);
        }
        AbstractDriverOptions<?> browserOptions = null;
        String platform = "Windows 10";
        String browserVersion = "126";
        if (browser.equalsIgnoreCase("chrome")) {
            //todo browserOptions
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPlatformName(platform);
            chromeOptions.setBrowserVersion(browserVersion);
            browserOptions = chromeOptions;
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setPlatformName(platform);
            firefoxOptions.setBrowserVersion(browserVersion);
            browserOptions = firefoxOptions;
        }
        browserOptions.setCapability("LT:Options", new BrowserOptions().getLambdaTestOptions());
        driver = new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), browserOptions);
        configureHelperComponents();
    }

    private void initializeLocalDriver(String browser, String url) throws APIException, IOException {
        if (PipelineConfig.testRailReport) {
            TestRailManager testRailManager = new TestRailManager();
            Go.testRunId = testRailManager.createTestRun("Sentra", 2);
        }
        if (browser.equalsIgnoreCase("chrome")) {
            driver = WebDriverManager.chromedriver().capabilities(new BrowserOptions().getChromeOptions(PipelineConfig.isBrowserHeadless, true)).create();
        } else if (browser.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        } else {
            driver = WebDriverManager.firefoxdriver().capabilities(new BrowserOptions().getFirefoxOptions(PipelineConfig.isBrowserHeadless, true)).create();
        }
        configureHelperComponents();
    }

    private void configureHelperComponents() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        Go.initialize(driver, javascriptExecutor, wait);
        Finder.initialize(driver, wait);
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

