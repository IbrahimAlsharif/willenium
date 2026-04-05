package tests.examples.wewill.home;

import base.Go;
import base.Setup;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WeWillLanguageSwitchTest {
    private static final By ARABIC_LANGUAGE_BUTTON = By.cssSelector(".lang-btn[data-lang='ar']");
    private static final By ACTIVE_ARABIC_LANGUAGE_BUTTON = By.cssSelector(".lang-btn.is-active[data-lang='ar']");

    @Test(priority = 1)
    public void switchHomePageToArabic() {
        // This keeps the Arabic flow honest by switching the rendered experience instead of assuming the URL changes locale.
        Setup.ensureUiSessionReady();
        Go.click(ARABIC_LANGUAGE_BUTTON);

        // This confirms the site finished the client-side language swap before the Arabic content assertions begin.
        Assert.assertEquals(
                Setup.driver.findElement(By.tagName("html")).getAttribute("lang"),
                "ar",
                "Arabic flow should switch the document language to Arabic"
        );
        Assert.assertTrue(
                Setup.driver.findElement(ACTIVE_ARABIC_LANGUAGE_BUTTON).isDisplayed(),
                "Arabic language toggle should stay active after the language switch"
        );
    }
}
