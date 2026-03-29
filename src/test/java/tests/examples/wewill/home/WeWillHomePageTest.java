package tests.examples.wewill.home;

import configs.testRail.TestRailCaseId;
import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testData;

public class WeWillHomePageTest {

    @Test(priority = 1)
    @TestRailCaseId("0001")
    public void verifyHeroHeadingVisible() {
        Assert.assertTrue(WeWillHomePage.getHeroHeading(testData.getWeWillHomeData().get("heroHeading").asText()).isDisplayed(),
                "Home hero heading should be visible");
    }

    @Test(priority = 2)
    @TestRailCaseId("0002")
    public void verifyPrimaryCallToActionVisible() {
        Assert.assertTrue(WeWillHomePage.getPrimaryCallToAction(testData.getWeWillHomeData().get("primaryCta").asText()).isDisplayed(),
                "Primary home call to action should be visible");
    }

    @Test(priority = 3)
    @TestRailCaseId("0003")
    public void verifyMethodologyHeadingVisible() {
        Assert.assertTrue(WeWillHomePage.getMethodologyHeading(testData.getWeWillHomeData().get("methodologyHeading").asText()).isDisplayed(),
                "Methodology heading should be visible");
    }

    @Test(priority = 4)
    @TestRailCaseId("0004")
    public void verifyBilingualOfferingCallToActionVisible() {
        Assert.assertTrue(WeWillHomePage.getBilingualOfferingCallToAction(testData.getWeWillHomeData().get("bilingualOfferingCta").asText()).isDisplayed(),
                "The bilingual offering call to action should be visible");
    }

    @Test(priority = 5)
    @TestRailCaseId("0005")
    public void verifyFooterSloganVisible() {
        Assert.assertTrue(WeWillHomePage.getFooterSlogan(testData.getWeWillHomeData().get("footerSlogan").asText()).isDisplayed(),
                "Footer slogan should be visible");
    }
}
