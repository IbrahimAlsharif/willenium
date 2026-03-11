package tests.common.navigation;

import base.Go;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static base.Setup.driver;
import static base.Setup.testCaseId;

public class BrowserBackTest {

    @Test(priority = 1)
    public void navigateBack() {
//        testCaseId = "0000"; // TODO: replace with valid TestRail case ID if reporting is enabled
        Go.back();
        waitForPageReady();
    }

    private void waitForPageReady() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                        .toString().equals("complete"));
    }
}
