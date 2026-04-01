package tests.examples.wewill.home;

import base.Setup;
import configs.testRail.TestRailCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static base.Setup.testData;

public class WeWillHomePageTest {

    @BeforeMethod
    public void ensureExampleHomeJourneyIsReady() {
        // This keeps setup failures visible as framework-level setup issues instead of null-pointer noise.
        Setup.ensureUiSessionReady();
    }

    @TestRailCase("0001")
    @Test(priority = 1)
    public void verifyHeroHeadingVisible() {
        // This verifies the first-time visitor immediately sees the main home message.
        Assert.assertTrue(WeWillHomePage.getHeroHeading(testData.getWeWillHomeData().get("heroHeading").asText()).isDisplayed(),
                "Home hero heading should be visible");
    }

    @TestRailCase("0002")
    @Test(priority = 2)
    public void verifyPrimaryCallToActionVisible() {
        // This confirms the primary action is visible when the page first loads.
        Assert.assertTrue(WeWillHomePage.getPrimaryCallToAction(testData.getWeWillHomeData().get("primaryCta").asText()).isDisplayed(),
                "Primary home call to action should be visible");
    }

    @TestRailCase("0003")
    @Test(priority = 3)
    public void verifyMethodologyHeadingVisible() {
        // This checks that the page explains the service approach in a visible section.
        Assert.assertTrue(WeWillHomePage.getMethodologyHeading(testData.getWeWillHomeData().get("methodologyHeading").asText()).isDisplayed(),
                "Methodology heading should be visible");
    }

    @TestRailCase("0004")
    @Test(priority = 4)
    public void verifyBilingualOfferingCallToActionVisible() {
        // This makes sure the bilingual offer remains discoverable as a conversion path.
        Assert.assertTrue(WeWillHomePage.getBilingualOfferingCallToAction(testData.getWeWillHomeData().get("bilingualOfferingCta").asText()).isDisplayed(),
                "The bilingual offering call to action should be visible");
    }

    @TestRailCase("0005")
    @Test(priority = 5)
    public void verifyFooterSloganVisible() {
        // This verifies the closing brand message still appears in the footer.
        Assert.assertTrue(WeWillHomePage.getFooterSlogan(testData.getWeWillHomeData().get("footerSlogan").asText()).isDisplayed(),
                "Footer slogan should be visible");
    }
}
