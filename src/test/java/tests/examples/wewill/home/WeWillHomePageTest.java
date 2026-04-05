package tests.examples.wewill.home;

import base.Setup;
import com.fasterxml.jackson.databind.JsonNode;
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

    private JsonNode getHomeExpectations() {
        return testData.getWeWillHomeData();
    }

    @TestRailCase("0001")
    @Test(priority = 1)
    public void verifyHeroHeadingVisible() {
        // This verifies the first-time visitor immediately sees the main home message.
        Assert.assertTrue(WeWillHomePage.getHeroHeading().getText().contains(getHomeExpectations().get("heroHeading").asText()),
                "Home hero heading should match the expected homepage promise");
    }

    @TestRailCase("0002")
    @Test(priority = 2)
    public void verifyPrimaryCallToActionVisible() {
        // This confirms the primary action stays visible with the expected business wording.
        Assert.assertEquals(WeWillHomePage.getPrimaryCallToAction().getText().trim(),
                getHomeExpectations().get("primaryCta").asText(),
                "Primary home call to action should match the expected copy");
    }

    @TestRailCase("0003")
    @Test(priority = 3)
    public void verifyMethodologyHeadingVisible() {
        // This checks that the page explains the service approach with the expected section heading.
        Assert.assertEquals(WeWillHomePage.getMethodologyHeading().getText().trim(),
                getHomeExpectations().get("methodologyHeading").asText(),
                "Methodology heading should match the expected offer");
    }

    @TestRailCase("0004")
    @Test(priority = 4)
    public void verifyBilingualOfferingCallToActionVisible() {
        // This makes sure the bilingual offer remains discoverable with the expected CTA text.
        Assert.assertEquals(WeWillHomePage.getBilingualOfferingCallToAction().getText().trim(),
                getHomeExpectations().get("bilingualOfferingCta").asText(),
                "The bilingual offering call to action should match the expected copy");
    }

    @TestRailCase("0005")
    @Test(priority = 5)
    public void verifyFooterSloganVisible() {
        // This verifies the closing brand message still appears in the footer with the expected wording.
        Assert.assertEquals(WeWillHomePage.getFooterSlogan().getText().trim(),
                getHomeExpectations().get("footerSlogan").asText(),
                "Footer slogan should match the expected brand promise");
    }
}
