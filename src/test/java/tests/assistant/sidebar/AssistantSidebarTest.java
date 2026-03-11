package tests.assistant.sidebar;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class AssistantSidebarTest {

    @Test(priority = 1)
    public void verifyAssistantTabVisible() {
        testCaseId = "9530";
        AssistantSidebar.openAssistantMode();
        Assert.assertTrue(AssistantSidebar.getAssistantTab(testData.getHomeData().get("assistantTab").asText()).isDisplayed(),
                "Assistant tab should be visible");
    }

    @Test(priority = 2)
    public void verifyAnalysisTabVisible() {
        testCaseId = "9531";
        Assert.assertTrue(AssistantSidebar.getAnalysisTab(testData.getHomeData().get("analysisTab").asText()).isDisplayed(),
                "Analysis tab should be visible");
    }

    @Test(priority = 3)
    public void verifySidebarSearchVisible() {
        testCaseId = "9532";
        Assert.assertTrue(AssistantSidebar.getSidebarSearchLabel(testData.getHomeData().get("sidebarSearch").asText()).isDisplayed(),
                "Sidebar Search label should be visible");
    }

    @Test(priority = 4)
    public void verifyNewChatButtonVisible() {
        testCaseId = "9533";
        Assert.assertTrue(AssistantSidebar.getNewChatButton(testData.getHomeData().get("newChat").asText()).isDisplayed(),
                "New Chat button should be visible");
    }

    @Test(priority = 5)
    public void verifyYourChatsLabelVisible() {
        testCaseId = "9534";
        Assert.assertTrue(AssistantSidebar.getYourChatsLabel(testData.getHomeData().get("yourChats").asText()).isDisplayed(),
                "Your Chats label should be visible");
    }

    @Test(priority = 6)
    public void verifyOrganizationTitleVisible() {
        testCaseId = "9535";
        Assert.assertTrue(AssistantSidebar.getOrganizationTitle(testData.getHomeData().get("organizationTitle").asText()).isDisplayed(),
                "Organization title should be visible");
    }

    @Test(priority = 7)
    public void verifySidebarCollapseToggleVisible() {
        testCaseId = "9536";
        Assert.assertTrue(AssistantSidebar.getSidebarCollapseToggleButton().isDisplayed(),
                "Sidebar collapse toggle should be visible");
    }

    @Test(priority = 8)
    public void verifySidebarUserNameVisible() {
        testCaseId = "9537";
        String name = testData.getUserData("validUser").get("name").asText();
        Assert.assertTrue(AssistantSidebar.getUserName(name).isDisplayed(),
                "User name should be visible");
    }

    @Test(priority = 9)
    public void verifySidebarUserEmailVisible() {
        testCaseId = "9538";
        String email = testData.getUserData("validUser").get("email").asText();
        Assert.assertTrue(AssistantSidebar.getUserEmail(email).isDisplayed(),
                "User email should be visible");
    }

    @Test(priority = 10)
    public void verifyLogoutVisible() {
        testCaseId = "9539";
        Assert.assertTrue(AssistantSidebar.getLogoutButton().isDisplayed(),
                "Logout button should be visible");
    }
}
