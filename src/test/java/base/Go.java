package base;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static base.Setup.driver;


public class Go {
    public static Faker faker = new Faker();
    public static String testRunId;
    public static String location;
    public static String currentTab;
    private static String mainTab;
    private static Map<String, Object> vars;
    private static WebDriver webDriver;
    private static JavascriptExecutor javascriptExecutor;
    private static WebDriverWait wait;

    /**
     * initialize Go by passing the required parameters
     *
     * @param driver        WebDriver object
     * @param js            the JavascriptExecutor object
     * @param webdriverWait the WebDriverWait object
     */
    public static void initialize(WebDriver driver, JavascriptExecutor js, WebDriverWait webdriverWait) {
        webDriver = driver;
        javascriptExecutor = js;
        wait = webdriverWait;
    }

    /**
     * Clicks the target element using Actions
     *
     * @param element the target element
     */
    public static void actionClick(WebElement element) {
        {
            new Actions(webDriver).click(element).perform();
        }
    }

    /**
     * Double-click the target element using Actions
     *
     * @param element the target element
     */
    public static void doubleClick(WebElement element) {
        {
            new Actions(webDriver).doubleClick(element).perform();
        }
    }

    public static void click(WebElement element) {
        boolean clicked = false;
        scrollDownToElement(element);
        try { // 1. Try native click
            element.click();
            clicked = true;
        } catch (Exception e1) {
            try { // 2. Try JS click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                clicked = true;
            } catch (JavascriptException e2) {
                try { // 3. Try Actions click
                    new Actions(driver).moveToElement(element).click().perform();
                    clicked = true;
                } catch (Exception e3) {
                    //
                }
            }
        }

        if (!clicked) {
            throw new RuntimeException("Failed to click on element");
        }
    }


    /**
     * Moves to the target element and click using Actions
     *
     * @param element the target element
     */
    public static void moveAndClick(WebElement element) {
        {
            new Actions(webDriver).moveToElement(element).click().perform();
        }
    }

    /**
     * Moves to the target element
     *
     * @param element the target element
     */
    public static void moveToElement(WebElement element) {
        {
            new Actions(webDriver).moveToElement(element);
        }
    }

    /**
     * Moves to the target element, then click and hold
     *
     * @param element the target element
     */
    public static void moveAndClickAndHold(WebElement element) {
        {
            new Actions(webDriver).moveToElement(element).clickAndHold().perform();
        }
    }

    /**
     * Clicks and hold the target element
     *
     * @param element the target element
     */
    public static void clickAndHold(WebElement element) {
        {
            new Actions(webDriver).moveToElement(element).clickAndHold().perform();
        }
    }

    /**
     * @return the system user directory based on the current OS
     */
    public static String getProperty() {
        return location = System.getProperty("user.dir");
    }

    /**
     * @param javaScript script to execute
     * @param element    element to execute script on it
     */
    public static void javascriptExecutor(String javaScript, WebElement element) {
        javascriptExecutor.executeScript(javaScript, element);
    }

    public static void clickWithJavaScript(WebElement element) {
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }


    /**
     * Executes Javascript
     *
     * @param script the full script to execute
     */
    public static void executeJavaScript(String script) {
        javascriptExecutor.executeScript(script);
    }

    /**
     * Scrolls down by value of pixels
     *
     * @param value pixels
     */
    public static void scrollDownBy(int value) {
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;
        String scrollStatement = "window.scrollBy(0," + value + ")";
        jse.executeScript(scrollStatement);
    }

    /**
     * Scrolls up by value of pixels
     *
     * @param value pixels
     */
    public static void scrollUpBy(int value) {
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;
        jse.executeScript("window.scrollBy(0," + (-1 * value) + ")");
    }

    /**
     * Scrolls right by value of pixels
     *
     * @param value pixels
     */
    public static void scrollRight(int value) {
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;
        jse.executeScript("window.scrollBy(" + value + ",0)");
    }

    /**
     * Switches to browser tab by index number starting from 0
     *
     * @param index order of the tab starting from 0
     */
    public static void switchToTabByIndex(int index) {
        currentTab = webDriver.getWindowHandle();
        String targetTab = currentTab;
        Set<String> handlers = webDriver.getWindowHandles();

        // Use generics to specify that the Iterator contains Strings
        Iterator<String> iterator = handlers.iterator();

        for (int i = 0; i < index; i++) {
            targetTab = iterator.next();
        }

        webDriver.switchTo().window(targetTab);
    }

    /**
     * Switches back to the original tab
     */
    public static void switchBackToTab() {
        webDriver.close();
        webDriver.switchTo().window(currentTab);
    }

    /**
     * Switches back to the window of the original tab
     */
    public static void switchBackToTabWithoutCloseTab() {
        webDriver.switchTo().window(currentTab);
    }

    /**
     * @return current window handle
     */
    public static String getCurrentWindowHandle() {
        return webDriver.getWindowHandle();
    }

    /**
     * Waits until a new window opens or the current URL contains a given substring.
     *
     * @param urlPart        part of URL to wait for (nullable)
     * @param timeoutSeconds timeout in seconds
     */
    public static void waitForNewWindowOrUrl(String urlPart, int timeoutSeconds) {
        Set<String> initialHandles = webDriver.getWindowHandles();
        new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> d.getWindowHandles().size() > initialHandles.size()
                        || (urlPart != null && !urlPart.isEmpty() && d.getCurrentUrl().contains(urlPart)));
    }

    /**
     * Switches to a window whose URL contains the provided substring.
     *
     * @param urlPart        part of URL to match
     * @param timeoutSeconds timeout in seconds
     * @return window handle after switching
     */
    public static String switchToWindowByUrlContains(String urlPart, int timeoutSeconds) {
        String originalHandle = webDriver.getWindowHandle();
        new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> {
                    for (String handle : d.getWindowHandles()) {
                        d.switchTo().window(handle);
                        try {
                            if (d.getCurrentUrl().contains(urlPart)) {
                                return true;
                            }
                        } catch (Exception ignored) {
                            // Ignore transient navigation issues while switching
                        }
                    }
                    d.switchTo().window(originalHandle);
                    return false;
                });

        for (String handle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(handle);
            if (webDriver.getCurrentUrl().contains(urlPart)) {
                return handle;
            }
        }
        webDriver.switchTo().window(originalHandle);
        return originalHandle;
    }

    /**
     * Switches to a specific window handle, or falls back to the first available handle.
     *
     * @param handle target window handle
     * @return window handle after switching
     */
    public static String switchToWindowOrFallback(String handle) {
        Set<String> handles = webDriver.getWindowHandles();
        if (handle != null && handles.contains(handle)) {
            webDriver.switchTo().window(handle);
            return handle;
        }
        String firstHandle = handles.iterator().next();
        webDriver.switchTo().window(firstHandle);
        return firstHandle;
    }

    /**
     * Refreshes the current page
     */
    public static void refreshPage() {
        webDriver.navigate().refresh();
    }

    public static String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Accepts the active alert
     */
    public static void alertAccept() {
        webDriver.switchTo().alert().accept();
    }

    /**
     * Scrolls to the target element
     *
     * @param element the target element
     */
    public void scrollToElement(WebElement element) {
        Coordinates coordinate = ((Locatable) element)
                .getCoordinates();
        coordinate.onPage();
        coordinate.inViewPort();
    }

    /**
     * Scrolls down to the target element
     *
     * @param element the target element
     */
    public static void scrollDownToElement(WebElement element) {
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scrolls up by 150 pixels until reaching the target element
     *
     * @param element the target element
     */
    public static void scrollUpUntilReachElement(WebElement element) {
        while (!element.isDisplayed()) {
            scrollUpBy(150);
        }
    }

    public static void scrollDownUntilReachElement(WebElement element) {
        while (!element.isDisplayed()) {
            scrollDownBy(-150);
        }
    }

    /**
     * Highlights the target element
     *
     * @param element the target element
     */
    public static void highlightElement(WebElement element) {
        for (int i = 0; i < 4; i++) {
            javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                    element, "color: yellow; border: 4px solid blue;");
            javascriptExecutor.executeScript("arguments[0].setAttribute('style’' arguments[1]);",
                    element, "");
        }
    }


    /**
     * @return focused element
     */
    public static WebElement getFocusedElement() {
        return webDriver.switchTo().activeElement();
    }

    /**
     * Waits until the target element becomes accessible
     *
     * @param locator locator of the target element
     */
    public static void waitForElementToBePresentBy(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println(" Element is not located in DOM");
        }
    }

    /**
     * Waits until the target element becomes visible
     *
     * @param locator locator of the target element
     */
    public static void waitForElementToBeVisibleBy(By locator) throws TimeoutException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the target element becomes visible
     *
     * @param element locator of the target element
     */
    public static void waitForElementToBeInVisibleBy(WebElement element) throws TimeoutException {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits until the target element becomes clickable
     *
     * @param locator locator of the target element
     */
    public static void waitForElementToBeClickableBy(By locator) throws TimeoutException {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the target element becomes clickable
     *
     * @param element locator of the target element
     */
    public static void waitForElementToBeClickable(WebElement element) throws TimeoutException {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Selects item from the dropdown by text
     *
     * @param dropDownMenu the dropdown web element
     * @param text         text of the target item
     */
    public static void selectByText(WebElement dropDownMenu, String text) {
        Select select = new Select(dropDownMenu);
        select.selectByVisibleText(text);
    }

    /**
     * Keep trying click element until other element becomes visible
     * used when you struggled to click some elements
     *
     * @param element            the target element to click
     * @param locatorToBeVisible locator of the element to be visible
     */

    public static void clickUntilVisibilityOfBy(@NotNull WebElement element, @NotNull By locatorToBeVisible) {
        int counter = 1;
        try {
            element.click();
            System.out.println("Tries =" + counter);
            waitForElementToBeVisibleBy(locatorToBeVisible);
        } catch (Exception exception) {
            screenShot("click try " + counter++ + "@");
            Actions action = new Actions(webDriver);
            action.click(element).perform();
            try {
                System.out.println("Tries =" + counter);
                waitForElementToBeVisibleBy(locatorToBeVisible);
            } catch (Exception exception1) {
                screenShot("click try " + counter++ + "@");
                Go.javascriptExecutor("arguments[0].click();", element);
                try {
                    System.out.println("Tries =" + counter);
                    waitForElementToBeVisibleBy(locatorToBeVisible);
                } catch (Exception exception2) {
                    screenShot("click try " + counter++ + "@");
                    Go.javascriptExecutor("arguments[0].scrollIntoView(true);", element);
                    Go.javascriptExecutor("arguments[0].click();", element);
                    try {
                        System.out.println("Tries =" + counter);
                        waitForElementToBeVisibleBy(locatorToBeVisible);
                    } catch (Exception exception3) {
                        screenShot("click try " + counter + "@");
                        Finder.getByXpath(Finder.getElementXPath(element), true).click();
                    }
                }
            }
        }
    }

    public static void clickUntilInvisibility(@NotNull WebElement element) {
        int counter = 1;
        try {
            element.click();
            System.out.println("Tries =" + counter);
            waitForElementToBeInVisibleBy(element);
        } catch (Exception exception) {
            screenShot("click try " + counter++ + "@");
            Actions action = new Actions(webDriver);
            action.click(element).perform();
            try {
                System.out.println("Tries =" + counter);
                waitForElementToBeInVisibleBy(element);
            } catch (Exception exception1) {
                screenShot("click try " + counter++ + "@");
                Go.javascriptExecutor("arguments[0].click();", element);
                try {
                    System.out.println("Tries =" + counter);
                    waitForElementToBeInVisibleBy(element);
                } catch (Exception exception2) {
                    screenShot("click try " + counter++ + "@");
                    Go.javascriptExecutor("arguments[0].scrollIntoView(true);", element);
                    Go.javascriptExecutor("arguments[0].click();", element);
                    try {
                        System.out.println("Tries =" + counter);
                        waitForElementToBeInVisibleBy(element);
                    } catch (Exception exception3) {
                        screenShot("click try " + counter + "@");
                        Finder.getByXpath(Finder.getElementXPath(element), true).click();
                    }
                }
            }
        }
    }

    /**
     * Waits until expected visible element becomes invisible
     *
     * @param by the locator of the target element
     */
    public static void waitIfVisible(By by) {
        try {
            waitForElementToBeInVisibleBy(webDriver.findElement(by));
        } catch (Exception ex) {
            System.out.println("waitIfVisible() -> " + "Element is not visible");
        }
    }

    public static void waitIfToastVisible() {
        try {
            waitForElementToBeInVisibleBy(webDriver.findElement(By.id("toast-container")));
        } catch (Exception ex) {
            System.out.println("waitIfVisible() -> " + "Element is not visible");
        }
    }

    public static boolean elementIsVisible(By by) {
        try {
            webDriver.findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isVisible(By locator) {
        return elementIsVisible(locator);
    }

    public static void waitForVisible(By locator, int timeoutSeconds) {
        new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForAnyVisible(int timeoutSeconds, By... locators) {
        new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> {
                    for (By locator : locators) {
                        if (isVisible(locator)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    public static boolean clickIfVisible(By locator) {
        if (isVisible(locator)) {
            webDriver.findElement(locator).click();
            return true;
        }
        return false;
    }

    /**
     * Snaps a screenshot as a file
     *
     * @param name the screenshot name to be saved in screenshots folder
     */
    public static void screenShot(String name) {
        getShotAsFile(name);
    }

    /**
     * Clears a text from input
     *
     * @param element the target element
     */
    public static void clearText(WebElement element) {
        element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
    }


    public static WebElement visibleAndInteractiveInXSeconds(String xpath, @Optional boolean isClickable, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        By byXpath = By.xpath(xpath);
        wait.until(ExpectedConditions.visibilityOfElementLocated(byXpath));
        if (isClickable) {
            wait.until(ExpectedConditions.elementToBeClickable(byXpath));
        }
        return driver.findElement(byXpath);
    }


    /**
     * Gets the current month on the form MMyy
     *
     * @return Date as a string
     */
    public static String getCurrentDate() {
        java.sql.Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dMMyy");
        return (simpleDateFormat.format(date));
    }

    public static String getCurrentDateWithSlashes() {
        java.sql.Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return (simpleDateFormat.format(date));
    }

    public static String getCurrentDayDate() {
        java.sql.Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
        return (simpleDateFormat.format(date));
    }


    /**
     * Pasts text into input
     *
     * @param element the target element to past text to it
     */
    public static void paste(WebElement element) {
        Actions a = new Actions(webDriver);
        a.keyDown(element, Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();
    }

    /**
     * Navigates back
     */
    public static void back() {
        webDriver.navigate().back();
    }

    /**
     * Waits for window for timeout milliseconds
     *
     * @param timeout number od milliseconds to wait
     * @return Strint
     */
    public static String waitForWindow(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> whNow = webDriver.getWindowHandles();
        @SuppressWarnings("unchecked")
        Set<String> whThen = (Set<String>) vars.get("window_handles");
        if (whNow.size() > whThen.size()) {
            whNow.removeAll(whThen);
        }
        return whNow.iterator().next();
    }


    public static void switchToTab() {
        //Store the ID of the original window
        String originalWindow = webDriver.getWindowHandle();

        //Loop through until we find a new window handle
        for (String windowHandle : webDriver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                webDriver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public static void getWindowHandles() {
        vars.put("window_handles", webDriver.getWindowHandles());
    }

    /**
     * Switch to window by tag
     *
     * @param tag the tag you set before using setWindowTag(tag)
     */
    public static void switchToWindowByTag(String tag) {
        vars.put(tag, waitForWindow(2000));
        setWindowTag("root");
        webDriver.switchTo().window(vars.get(tag).toString());
    }

    /**
     * Set a tag for the current window
     *
     * @param tag of the target window
     */
    public static void setWindowTag(String tag) {
        vars.put(tag, webDriver.getWindowHandle());
    }

    /**
     * Switches to iFrame by tag name
     *
     * @param tagName of the target iFrame
     */
    public static void switchToFrame(String tagName) {
        webDriver.switchTo().frame(webDriver.findElement(By.tagName(tagName)));
    }


    public static void switchToFrameByIndex(int index) {
        webDriver.switchTo().frame(index);
    }

    /**
     * Switches for iFrame by its xpath
     *
     * @param xPath of the target element
     */
    public static void switchToFrameByXpath(String xPath) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((By.xpath(xPath))));
    }

    public static void switchToFrameIfExistsByXpath(String xPath) {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((By.xpath(xPath))));
        } catch (TimeoutException e) {
            System.out.println("Frame doesn't exist");
        }
    }

    /**
     * Switches to the default page content (iFrame)
     */
    public static void switchBackToMainFrame() {
//        driver.switchTo().parentFrame();
        webDriver.switchTo().defaultContent();
    }

    /**
     * Opens page Url
     *
     * @param url the target url
     */
    public static void openUrl(String url) {
        webDriver.get(url);
    }

    /**
     * Attempts to open a URL multiple times until the page loads.
     *
     * @param url         the target url
     * @param maxAttempts maximum number of tries
     */
    public static void openUrlWithRetry(String url, int maxAttempts) {
        int tries = 0;
        while (tries < maxAttempts) {
            try {
                webDriver.get(url);
                new WebDriverWait(webDriver, Duration.ofSeconds(10))
                        .until(driver -> ((JavascriptExecutor) driver)
                                .executeScript("return document.readyState").equals("complete"));
                return;
            } catch (Exception e) {
                tries++;
                if (tries >= maxAttempts) {
                    throw new RuntimeException("Failed to open URL after " + tries + " attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
        }
    }



    /**
     * Gets screenshot as a file
     *
     * @param shotName name of the image file
     * @return File
     */
    public static File getShotAsFile(String shotName) {
        final String folder = "screenshots/";
        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        String currentTime = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss-S").format(new Date(System.currentTimeMillis()));
        File shot = new File(folder + shotName + currentTime + ".png");
        try {
            FileUtils.copyFile(scrFile, shot);
        } catch (IOException ex) {
            Logger.getLogger(Go.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shot;
    }

    /**
     * Gets a screenshot as based64 format
     *
     * @return String
     */
    public static String getShotAsBase64() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);
    }

    // Tabs Manager

    /**
     * Mark the first tab as the main tab
     */
    public static void setMainTab() {
        mainTab = webDriver.getWindowHandle();
    }

    /**
     * Get the current list of tabs (auto-updating)
     */
    private static List<String> getTabs() {
        return new ArrayList<>(webDriver.getWindowHandles());
    }

    /**
     * Switch to a specific tab by index
     */
    public static void switchToTab(int index) {
        List<String> tabs = getTabs(); // Refresh tab list before switching
        if (index < 0 || index >= tabs.size()) {
            throw new IllegalArgumentException("Invalid tab index.");
        }
        webDriver.switchTo().window(tabs.get(index));
    }

    /**
     * Switch to the main tab
     */
    public static void switchToMainTab() {
        webDriver.switchTo().window(mainTab); // Switch back to the main tab
    }

    /**
     * Close the current tab and switch to the last opened tab
     */
    public static void closeCurrentTab() {
        webDriver.close(); // Closes the current tab
        List<String> tabs = getTabs();  // Refresh tab list after closing
        if (!tabs.isEmpty()) {
            webDriver.switchTo().window(tabs.get(tabs.size() - 1)); // Switch to the last remaining tab
        }
    }

    /**
     * Close all tabs except the main tab and switch to it
     */
    public static void closeAllTabsExceptMain() {
        List<String> tabs = getTabs(); // Refresh tab list before closing
        System.out.println("Count-> " + tabs.size());
        for (String tab : tabs) {
            if (!tab.equals(mainTab)) {
                webDriver.switchTo().window(tab);
                webDriver.close();
            }
        }
        webDriver.switchTo().window(mainTab); // Switch back to the main tab
    }

    /**
     * Open a new tab and switch to it
     */
    public static void openNewTab() {
        ((JavascriptExecutor) webDriver).executeScript("window.open();");
        List<String> tabs = getTabs(); // Refresh tab list after opening
        webDriver.switchTo().window(tabs.get(tabs.size() - 1)); // Switch to the new tab
    }

    /**
     * switch to the last tab by order
     */
    public static void switchToLastTab() {
        try {
            Thread.sleep(2000);  // Optional delay to ensure tabs are fully loaded
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<String> tabs = getTabs();  // Get the list of tabs
        System.out.println("Number of tabs before cleanup: " + tabs.size());

        // Close invalid tabs and collect valid ones
        List<String> validTabs = new ArrayList<>();
        for (String tab : tabs) {
            webDriver.switchTo().window(tab);
            String url = webDriver.getCurrentUrl();
//            System.out.println("Tab ID: " + tab + " | URL: " + url);

            // Check if the URL is valid and meaningful for your test
            if (isValidUrl(url)) {
                validTabs.add(tab);
                System.out.println("Valid " + tab + " " + url);
            } else {
//                System.out.println("Closed "+tab+" "+url);
                webDriver.close();  // Close unexpected tab
            }
        }

        // Check if there are valid tabs left and switch to the last one
        if (!validTabs.isEmpty()) {
            System.out.println("Number of valid tabs: " + validTabs.size());
            webDriver.switchTo().window(validTabs.get(validTabs.size() - 1));
            try {
                Thread.sleep(2000);  // Optional delay to ensure tabs are fully loaded
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }// Switch to the last valid tab
        } else {
            throw new RuntimeException("No valid tabs remaining to switch to.");
        }
    }

    private static boolean isValidUrl(String url) {
        return url.contains("micetribe.com") || url.contains("revolut.com") || url.contains("skipcashtest.azurewebsites.net");
    }


    /**
     * Switch to the last tab and close all other tabs
     */
    public static void switchToLastTabAndCloseOthers() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> tabs = getTabs(); // Get the updated list of tabs
        if (tabs.size() <= 1) {
            return;
        }
        System.out.println("#tabs: " + tabs.size());
        String lastTab = tabs.get(tabs.size() - 1); // Get the last tab handle
        // Close all other tabs except the last one
        for (String tab : tabs) {
            if (!tab.equals(lastTab)) {
                webDriver.switchTo().window(tab);
                webDriver.close();
            }
        }
        webDriver.switchTo().window(lastTab);
    }

    public static String getWordOfNumber(int num) {
        return String.valueOf(num).replace("0", "zero")
                .replace("1", "one")
                .replace("2", "two")
                .replace("3", "three")
                .replace("4", "four")
                .replace("5", "five")
                .replace("6", "six")
                .replace("7", "seven")
                .replace("8", "eight")
                .replace("9", "nine")
                .replace("_", " ");
    }

    public static void takeSystemLevelScreenShot(String imageName) throws AWTException, InterruptedException, IOException {
        Robot robot = new Robot();
        Thread.sleep(5000);
        java.awt.Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenCapture = robot.createScreenCapture(screenRect);
        ImageIO.write(screenCapture, "PNG", new File(imageName));
    }

    public static void takeAshotSystemLevelScreenShot(String imageName) throws IOException {
        Go.screenShot("just-to-create-folder");
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))  // Handles scrolling
                .takeScreenshot(driver);
        BufferedImage image = screenshot.getImage();
        ImageIO.write(image, "PNG", new File(imageName));
    }


    public static boolean imagesAreSimilar(String targetImageName, String templateImageName, int differenceThreshold) throws IOException, InterruptedException {
        targetImageName = "screenshots/" + targetImageName;
        if (System.getenv("CI") != null) {
            int dot = templateImageName.indexOf('.');
            templateImageName = "src/images/" + templateImageName.substring(0, dot) + "CI.png";
        } else {
            templateImageName = "src/images/" + templateImageName;
        }
        System.out.println("templateImageName -> " + templateImageName);
        Thread.sleep(2000);
        Go.takeAshotSystemLevelScreenShot(targetImageName);
        return Go.getImagesDifference(templateImageName, targetImageName, differenceThreshold) < differenceThreshold;
    }

    private static int getImagesDifference(String templateImageName, String targetImageName, int diffSize) throws IOException {
        BufferedImage templateBufferedImage = ImageIO.read(new File(templateImageName));
        BufferedImage targetBufferedImage = ImageIO.read(new File(targetImageName));
        //Detect
        ImageDiffer imgDiffer = new ImageDiffer();
        ImageDiff diff = imgDiffer.makeDiff(targetBufferedImage, templateBufferedImage).withDiffSizeTrigger(diffSize);
        System.out.println("DiffSize: " + diff.getDiffSize());
        ImageIO.write(diff.getMarkedImage(), "PNG", new File("screenshots/image-diff.png"));
        return diff.getDiffSize();
    }

    public static Map<String, String> readDataFromExcel(String filePath) throws IOException {
        Map<String, String> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet

            Row headerRow = sheet.getRow(0);  // Assume the first row is the header
            Row dataRow = sheet.getRow(1);    // Assume data is on the second row

            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                String key = headerRow.getCell(i).getStringCellValue();
                String value = dataRow.getCell(i).getStringCellValue();
                System.out.println("Value: " + value);
                data.put(key, value);
            }
        }

        return data;
    }

    public static String generateUniqueName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d_h_m_s", Locale.ENGLISH);
        String currentDate = dateFormat.format(new java.util.Date());
        return currentDate
                .replace("0", "zero")
                .replace("1", "one")
                .replace("2", "two")
                .replace("3", "three")
                .replace("4", "four")
                .replace("5", "five")
                .replace("6", "six")
                .replace("7", "seven")
                .replace("8", "eight")
                .replace("9", "nine")
                .replace("_", " ");
    }


    public static void clickAtScreenCoordinates(int x, int y, WebElement canvas) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Actions actions = new Actions(driver);
            System.out.println(canvas.getSize().getWidth());
            System.out.println(canvas.getSize().getHeight());
            actions.moveToElement(canvas, x, y).click().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dimension getWindowDimension() {
        return webDriver.manage().window().getSize();
    }

    public static void waitForSomeSeconds(int seconds) throws InterruptedException {
        if (seconds < 5) {
            Thread.sleep(seconds * 1000);
        } else {
            int fiveTimes = seconds / 5;
            for (int i = 0; i < fiveTimes; i++) {
                System.out.println("Waiting... " + (i + 1) * 5 + " seconds");
                Thread.sleep(5000); // 5 seconds
            }
        }
    }


    // Attach marker directly inside the popup (adjust selector based on the popup container)
//        String addMarkerScript = String.format(
//                "var marker = document.createElement('div');" +
//                        "marker.style.position = 'fixed';" +         // Use fixed instead of absolute
//                        "marker.style.left = '%dpx';" +              // X coordinate
//                        "marker.style.top = '%dpx';" +               // Y coordinate
//                        "marker.style.width = '12px';" +
//                        "marker.style.height = '12px';" +
//                        "marker.style.background = 'red';" +         // Red marker
//                        "marker.style.borderRadius = '50%%';" +
//                        "marker.style.border = '2px solid white';" + // Add contrast for better visibility
//                        "marker.style.zIndex = '2147483647';" +      // Max z-index value
//                        "document.body.appendChild(marker);",
//                x, y);
//        ((JavascriptExecutor) driver).executeScript(addMarkerScript);


//    public static void clickAtScreenCoordinates(int x, int y) {
//        try {
//            Robot robot = new Robot();
//            robot.mouseMove(x, y);
//            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String addMarkerScript = String.format(
//                "var marker = document.createElement('div');" +
//                        "marker.style.position = 'absolute';" +
//                        "marker.style.left = '%dpx';" +       // X coordinate
//                        "marker.style.top = '%dpx';" +        // Y coordinate
//                        "marker.style.width = '10px';" +
//                        "marker.style.height = '10px';" +
//                        "marker.style.background = 'red';" +  // Red marker
//                        "marker.style.borderRadius = '50%%';" + // Circle shape
//                        "marker.style.zIndex = '9999999';" +     // Bring to front
//                        "document.body.appendChild(marker);",
//                x, y
//        );
//        ((JavascriptExecutor) driver).executeScript(addMarkerScript);
//
//    }
}
