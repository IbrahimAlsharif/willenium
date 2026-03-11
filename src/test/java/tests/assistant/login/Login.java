package tests.assistant.login;

import base.Finder;
import base.Go;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Login {
    private static final By GOOGLE_PASSWORD_INPUT = By.name("Passwd");

    public static WebElement getWelcomeHeader(String text) {
        return Finder.getByExactText(text, "*", false);
    }

    public static WebElement getSignInInstruction(String text) {
        return Finder.getByPartialText(text, "*", false);
    }

    public static WebElement getRightPanelWelcome(String text) {
        return Finder.getByExactText(text, "*", false);
    }

    public static WebElement getRealTimeMonitoringText(String text) {
        return Finder.getByExactText(text, "*", false);
    }

    public static WebElement getGoogleSignInButton(String text) {
        String xpath = String.format("//button[contains(normalize-space(.),\"%s\")]", text);
        return Finder.getByXpath(xpath, true);
    }

    public static WebElement getGoogleSignInButtonInLoadingState() {
        return Finder.getByXpath("//button[@disabled and .//div[contains(normalize-space(.),'Loading...')]]", false);
    }

    public static boolean openGoogleAuthPage(String buttonText) {
        Go.click(getGoogleSignInButton(buttonText));
        Go.waitForNewWindowOrUrl("accounts.google.com", 20);
        Go.switchToWindowByUrlContains("accounts.google.com", 30);
        return Go.getCurrentUrl().contains("accounts.google.com");
    }

    public static WebElement getGoogleEmailInput() {
        return Finder.getById("identifierId", true);
    }

    public static WebElement getGoogleEmailNextButton() {
        return Finder.getById("identifierNext", true);
    }

    public static WebElement getGooglePasswordInput() {
        return Finder.getByName("Passwd", true);
    }

    public static WebElement getGooglePasswordNextButton() {
        return Finder.getById("passwordNext", true);
    }

    public static void clickGoogleEmailNext() {
        Go.click(getGoogleEmailNextButton());
    }

    public static void clickGooglePasswordNext() {
        Go.click(getGooglePasswordNextButton());
    }

    public static void switchToWindowOrFallback(String handle) {
        Go.switchToWindowOrFallback(handle);
    }

    public static boolean waitForGooglePassword() {
        Go.switchToWindowByUrlContains("accounts.google.com", 30);
        Go.waitForAnyVisible(30, GOOGLE_PASSWORD_INPUT);
        return Go.isVisible(GOOGLE_PASSWORD_INPUT);
    }
}
