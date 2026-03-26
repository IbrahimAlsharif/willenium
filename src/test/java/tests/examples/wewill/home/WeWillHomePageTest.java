package tests.examples.wewill.home;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class WeWillHomePageTest {

    @Test(priority = 1)
    public void verifyHeroHeadingVisible() {
        testCaseId = "0001";
        Assert.assertTrue(WeWillHomePage.getHeroHeading(testData.getWeWillHomeData().get("heroHeading").asText()).isDisplayed(),
                "Home hero heading should be visible");
    }

    @Test(priority = 2)
    public void verifyPrimaryCallToActionVisible() {
        testCaseId = "0002";
        Assert.assertTrue(WeWillHomePage.getPrimaryCallToAction(testData.getWeWillHomeData().get("primaryCta").asText()).isDisplayed(),
                "Primary home call to action should be visible");
    }

    @Test(priority = 3)
    public void verifyMethodologyHeadingVisible() {
        testCaseId = "0003";
        Assert.assertTrue(WeWillHomePage.getMethodologyHeading(testData.getWeWillHomeData().get("methodologyHeading").asText()).isDisplayed(),
                "Methodology heading should be visible");
    }

    @Test(priority = 4)
    public void verifyBilingualOfferingCallToActionVisible() {
        testCaseId = "0004";
        Assert.assertTrue(WeWillHomePage.getBilingualOfferingCallToAction(testData.getWeWillHomeData().get("bilingualOfferingCta").asText()).isDisplayed(),
                "The bilingual offering call to action should be visible");
    }

    @Test(priority = 5)
    public void verifyFooterSloganVisible() {
        testCaseId = "0005";
        Assert.assertTrue(WeWillHomePage.getFooterSlogan(testData.getWeWillHomeData().get("footerSlogan").asText()).isDisplayed(),
                "Footer slogan should be visible");
    }
}
