package tests.assistant.login;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import tests.assistant.sidebar.AssistantSidebar;

import static base.Setup.testCaseId;
import static base.Setup.testData;


public class LoginTest {

    String mainWindowHandle;

    @Test(priority = 1)
    public void verifyWelcomeHeader() {
        testCaseId = "9382";
        Assert.assertTrue(Login.getWelcomeHeader(testData.getLoginData().get("welcomeHeader").asText()).isDisplayed(),
                "Welcome header should be visible");
    }

    @Test(priority = 2)
    public void verifySignInInstruction() {
        testCaseId = "9383";
        Assert.assertTrue(Login.getSignInInstruction(testData.getLoginData().get("signInInstruction").asText()).isDisplayed(),
                "Sign in instruction should be visible");
    }

    @Test(priority = 3)
    public void verifyRightPanelWelcome() {
        testCaseId = "9384";
        Assert.assertTrue(Login.getRightPanelWelcome(testData.getLoginData().get("rightPanelWelcome").asText()).isDisplayed(),
                "Right panel welcome text should be visible");
    }

    @Test(priority = 4)
    public void verifyRealTimeMonitoringText() {
        testCaseId = "9385";
        Assert.assertTrue(Login.getRealTimeMonitoringText(testData.getLoginData().get("realTimeMonitoring").asText()).isDisplayed(),
                "Real-time monitoring text should be visible");
    }

    @Test(priority = 5)
    public void verifyGoogleSignInButton() {
        testCaseId = "9386";
        Assert.assertTrue(Login.getGoogleSignInButton(testData.getLoginData().get("googleSignInButton").asText()).isDisplayed(),
                "Google sign in button should be visible");
    }

    @Test(priority = 6, dependsOnMethods = "verifyGoogleSignInButton")
    public void testClickGoogleSignIn() {
        testCaseId = "9387";
        Assert.assertTrue(Login.openGoogleAuthPage(testData.getLoginData().get("googleSignInButton").asText()),
                "Google authentication page should open");
    }

    @Parameters({"userType"})
    @Test(priority = 7, dependsOnMethods = "testClickGoogleSignIn")
    public void testEnterGoogleEmail(String userType) {
        testCaseId = "9388";
        Login.getGoogleEmailInput().sendKeys(testData.getUserData(userType).get("email").asText());
        Assert.assertEquals(Login.getGoogleEmailInput().getAttribute("value"), testData.getUserData(userType).get("email").asText(),
                "Email was not entered correctly");
    }

    @Test(priority = 8, dependsOnMethods = "testEnterGoogleEmail")
    public void testClickEmailNext() {
        testCaseId = "9389";
        Login.clickGoogleEmailNext();
        Assert.assertTrue(Login.waitForGooglePassword(), "Password field should be displayed");
    }

    @Parameters({"userType"})
    @Test(priority = 9, dependsOnMethods = "testClickEmailNext")
    public void testEnterGooglePassword(String userType) {
        testCaseId = "9390";
        Login.getGooglePasswordInput().sendKeys(testData.getUserData(userType).get("password").asText());
        Assert.assertNotNull(Login.getGooglePasswordInput().getAttribute("value"), testData.getUserData(userType).get("password").asText());
    }

    @Test(priority = 10, dependsOnMethods = "testEnterGooglePassword")
    public void testClickPasswordNext() {
        testCaseId = "9391";
        Login.clickGooglePasswordNext();
        Login.switchToWindowOrFallback(mainWindowHandle);
        Assert.assertTrue(AssistantSidebar.getNewChatButton(testData.getHomeData().get("newChat").asText()).isDisplayed(),
                "New Chat button should be visible on Dashboard");
    }
}
