package tests.assistant.home;

import base.Finder;
import org.openqa.selenium.WebElement;

public class AssistantHomePage {
    private static final String Global_SEARCH_XPATH =
            "//input[contains(@placeholder,'Search') or @type='search' or @aria-label='Search' or @role='searchbox']";
    private static final String CHAT_Dock_XPATH =
            "//textarea[contains(@placeholder,'Describe what you want to monitor')]" +
                    " | //input[contains(@placeholder,'Describe what you want to monitor')]" +
                    " | //*[@contenteditable='true' and contains(@data-placeholder,'Describe what you want to monitor')]";
    private static final String NOTIFICATIONS_ICON_BUTTON_XPATH =
            "//button[@aria-label='Notifications' or @title='Notifications' or @data-testid='notifications']" +
                    " | //*[name()='svg' and (contains(@data-icon,'bell') or contains(@class,'bell'))]" +
                    "/ancestor::button[1]";
    private static final String CHAT_DOCK_SEND_BUTTON_XPATH =
            "//button[@aria-label='Send message']";
    private static final String COPILOT_Assistant_BUTTON_XPATH =
            "//button[@aria-label='Open Chat']";


    public static WebElement getGlobalSearchInput(String placeholderText) {
        String xpath = String.format("//input[contains(@placeholder,'%s') or @type='search' or @aria-label='%s' or @role='searchbox']",
                placeholderText, placeholderText);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getLiveMonitoringLabel(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "']", false);
    }

    public static WebElement getViewAllButton(String text) {
        return Finder.getByXpath("//*[normalize-space()='" + text + "']", false);
    }

    public static WebElement getLanguageToggle() {
        return Finder.getByXpath("//button[@data-slot='dropdown-menu-trigger' and @aria-haspopup='menu']", true);
    }

    public static WebElement getChatDockInput(String placeholderText) {
        String xpath = String.format(
                "//textarea[contains(@placeholder,'%s')]" +
                        " | //input[contains(@placeholder,'%s')]" +
                        " | //*[@contenteditable='true' and contains(@data-placeholder,'%s')]",
                placeholderText, placeholderText, placeholderText);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getNotificationsIconButton() {
        return Finder.getByXpath(NOTIFICATIONS_ICON_BUTTON_XPATH, false);
    }

    public static WebElement getChatDockArButton() {
        return Finder.getByXpath("//button[normalize-space()='AR'] | //*[normalize-space()='AR']/ancestor::button[1]", true);
    }

    public static WebElement getChatDockVoiceButton(String text) {
        return Finder.getByXpath("//button[normalize-space()='" + text + "'] | //*[" +
                "normalize-space()='" + text + "']/ancestor::button[1]", true);
    }

    public static WebElement getChatDockSendButton() {
        return Finder.getByXpath(CHAT_DOCK_SEND_BUTTON_XPATH, false);
    }

    public static WebElement getCopilotAssistantButton() {
        return Finder.getByXpath(COPILOT_Assistant_BUTTON_XPATH, false);
    }
}
