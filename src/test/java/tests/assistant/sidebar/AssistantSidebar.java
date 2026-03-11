package tests.assistant.sidebar;

import base.Finder;
import org.openqa.selenium.WebElement;

import static base.Setup.testData;

public class AssistantSidebar {
    private static final String SIDEBAR_CONTAINER_XPATH = "//div[@data-slot='sidebar-container']";
    private static final String LOGOUT_BUTTON_XPATH =
            "//button[@aria-label='Logout' or @title='Logout' or @aria-label='Sign out' or @title='Sign out']" +
                    " | //*[contains(@class,'logout') or contains(@data-testid,'logout')]" +
                    "/ancestor::button[1]";

    public static WebElement getAssistantTab(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "']", false);
    }

    public static WebElement getAnalysisTab(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "']", false);
    }

    public static WebElement getSidebarSearchLabel(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "' and not(self::input)]", true);
    }

    public static WebElement getNewChatButton(String text) {
        return Finder.getByPartialText(text, "span", true);
    }

    public static WebElement getYourChatsLabel(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "']", false);
    }

    public static WebElement getOrganizationTitle(String text) {
        return Finder.getByXpath("//*[contains(normalize-space(.),'" + text + "')]", false);
    }

    public static WebElement getSidebarCollapseToggleButton() {
        return Finder.getByXpath("//button[@aria-label='Toggle sidebar']", false);
    }

    public static WebElement getUserName(String name) {
        return Finder.getByXpath("//*[normalize-space()='" + name + "']", false);
    }

    public static WebElement getUserEmail(String email) {
        return Finder.getByXpath("//*[normalize-space()='" + email + "']", false);
    }

    public static WebElement getLogoutButton() {
        return Finder.getByXpath(LOGOUT_BUTTON_XPATH, false);
    }

    public static WebElement getSidebarContainer() {
        return Finder.getByXpath(SIDEBAR_CONTAINER_XPATH, false);
    }

    public static void openAssistantMode() {
        getAssistantTab(testData.getHomeData().get("assistantTab").asText()).click();
    }
}
