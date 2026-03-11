package tests.assistant.home;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class AssistantHomePageTest {

    @Test(priority = 1)
    public void verifyGlobalSearchInputVisible() {
        testCaseId = "9540";
        Assert.assertTrue(AssistantHomePage.getGlobalSearchInput(testData.getHomeData().get("sidebarSearch").asText()).isDisplayed(),
                "Global search input should be visible");
    }

    @Test(priority = 2)
    public void verifyLiveMonitoringLabelVisible() {
        testCaseId = "9541";
        Assert.assertTrue(AssistantHomePage.getLiveMonitoringLabel(testData.getHomeData().get("liveMonitoring").asText()).isDisplayed(),
                "Live Monitoring label should be visible");
    }

    @Test(priority = 3)
    public void verifyLanguageToggleVisible() {
        testCaseId = "9542";
        Assert.assertTrue(AssistantHomePage.getLanguageToggle().isDisplayed(),
                "Language toggle should be visible");
    }

    @Test(priority = 4)
    public void verifyChatDockVisible() {
        testCaseId = "9543";
        Assert.assertTrue(AssistantHomePage.getChatDockInput(testData.getHomeData().get("chatDockPlaceholder").asText()).isDisplayed(),
                "Chat dock input should be visible");
    }

    @Test(priority = 5)
    public void verifyNotificationsIconVisible() {
        testCaseId = "9544";
        Assert.assertTrue(AssistantHomePage.getNotificationsIconButton().isDisplayed(),
                "Notifications icon button should be visible");
    }

    @Test(priority = 6)
    public void verifyChatDockArButtonVisible() {
        testCaseId = "9545";
        Assert.assertTrue(AssistantHomePage.getChatDockArButton().isDisplayed(),
                "Chat dock AR button should be visible");
    }

    @Test(priority = 7)
    public void verifyChatDockVoiceButtonVisible() {
        testCaseId = "9546";
        Assert.assertTrue(AssistantHomePage.getChatDockVoiceButton(testData.getHomeData().get("voiceButton").asText()).isDisplayed(),
                "Chat dock voice button should be visible");
    }

    @Test(priority = 8)
    public void verifyChatDockSendButtonVisible() {
        testCaseId = "9547";
        Assert.assertTrue(AssistantHomePage.getChatDockSendButton().isDisplayed(),
                "Chat Dock send button should be visible");
    }

    @Test(priority = 9)
    public void verifyCopilotAssistantButtonVisible() {
        testCaseId = "9548";
        Assert.assertTrue(AssistantHomePage.getCopilotAssistantButton().isDisplayed(),
                "Copilot assistant button should be visible");
    }

    @Test(priority = 10)
    public void verifyChatDockSendButtonDisabledWhenInputEmpty() {
        testCaseId = "9549";
        WebElement input = AssistantHomePage.getChatDockInput(testData.getHomeData().get("chatDockPlaceholder").asText());
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        Assert.assertFalse(AssistantHomePage.getChatDockSendButton().isEnabled(),
                "Chat dock send button should be disabled when input is empty");
    }

    @Test(priority = 11)
    public void verifyChatDockSendButtonEnabledWhenInputHasText() {
        testCaseId = "9550";
        WebElement input = AssistantHomePage.getChatDockInput(testData.getHomeData().get("chatDockPlaceholder").asText());
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys("hello");
        Assert.assertTrue(AssistantHomePage.getChatDockSendButton().isEnabled(),
                "Chat dock send button should be enabled after input");
    }
}
