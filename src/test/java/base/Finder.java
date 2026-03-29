package base;

import configs.pipeline.PipelineConfig;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;

import java.time.Duration;
import java.util.List;

import static base.Setup.driver;
import static org.openqa.selenium.support.locators.RelativeLocator.with;


public class Finder {
    private static WebDriver webDriver;
    private static WebDriverWait wait;

    /**
     * initialize Go by passing the required parameters
     *
     * @param driver        WebDriver object
     * @param webdriverWait the WebDriverWait object
     */
    public static void initialize(WebDriver driver, WebDriverWait webdriverWait) {
        webDriver = driver;
        wait = webdriverWait;
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
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the target element becomes visible
     *
     * @param locator locator of the target element
     */
    public static void waitForElementToBeVisibleBy(By locator) throws TimeoutException {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement getFirstDisplayedByXpath(String xpath) {
        By byXpath = By.xpath(xpath);
        waitForElementToBePresentBy(byXpath);
        List<WebElement> elements = driver.findElements(byXpath);
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        if (elements.isEmpty()) {
            return getByXpath(xpath, false);
        }
        return elements.get(0);
    }

    /**
     * Waits until the target element becomes visible
     *
     * @param element locator of the target element
     */
    public static void waitForElementToBeInVisibleBy(WebElement element) throws TimeoutException {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits until the target element becomes clickable
     *
     * @param locator locator of the target element
     */
    public static void waitForElementToBeClickableBy(By locator) throws TimeoutException {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the target element becomes clickable
     *
     * @param element locator of the target element
     */
    public static void waitForElementToBeClickable(WebElement element) throws TimeoutException {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement get(By locator) {
        return find(locator, false);
    }

    public static WebElement getClickable(By locator) {
        return find(locator, true);
    }

    public static WebElement find(By locator, boolean isClickable) {
        return getWait()
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until(driver -> isClickable
                        ? ExpectedConditions.elementToBeClickable(locator).apply(driver)
                        : ExpectedConditions.visibilityOfElementLocated(locator).apply(driver));
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
     * Waits until expected visible element becomes invisible
     *
     * @param by the locator of the target element
     */

    public static boolean elementIsVisible(By by) {
        try {
            webDriver.findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    /**
     * Gets the element by its xpath,
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param xpath       xpath of the target element
     * @param isClickable true or false
     * @return The target web element
     */
    public static WebElement getByXpath(String xpath, @Optional boolean isClickable) {
        By byXpath = By.xpath(xpath);
        return find(byXpath, Boolean.TRUE.equals(isClickable));
    }

    /**
     * Gets a div element by its class name and index (XPath style).
     *
     * @param className the class name of the div
     * @param index     the index of the element in the matching list (1-based)
     * @return WebElement representing the div at the specified index
     */
    public static WebElement getByClassWithIndex(String tagName, String className, int index, @Optional boolean isClickable) {
        String xpath = String.format("(//%s[@class='%s'])[%d]", tagName, className, index);
        By byXpath = By.xpath(xpath);
        return find(byXpath, Boolean.TRUE.equals(isClickable));
    }

    /**
     * Gets a div element by its  name and index (XPath style).
     *
     * @param Name  the name of the div
     * @param index the index of the element in the matching list (1-based)
     * @return WebElement representing the div at the specified index
     */
    public static WebElement getByNameWithIndex(String tagName, String Name, int index, @Optional boolean isClickable) {
        String xpath = String.format("(//%s[@name='%s'])[%d]", tagName, Name, index);
        By byXpath = By.xpath(xpath);
        return find(byXpath, Boolean.TRUE.equals(isClickable));
    }

    public static WebElement getByXpathInParent(String xpath, WebElement parent, @Optional boolean isClickable) {
        By byXpath = By.xpath(xpath);
        waitForElementToBeVisibleBy(byXpath);
        if (Boolean.TRUE.equals(isClickable)) {
            waitForElementToBeClickableBy(byXpath);
        }
        return parent.findElement(byXpath);
    }

    /**
     * @param text        the exact text of the target component
     * @param tagName     the HTML tag, input empty string or * for any tag
     * @param isClickable should wait until the element becomes clickable?
     * @return WebElement
     */
    public static WebElement getByExactText(String text, @Optional String tagName, @Optional boolean isClickable) {
        // Use "*" if tagName is not provided.
        String tag = (tagName != null && !tagName.isEmpty()) ? tagName : "*";

        // Construct XPath for exact text match.
        String xpath = String.format("//%s[text()='%s']", tag, text);

        // Call the common method.
        return getByXpath(xpath, isClickable);
    }

    /**
     * @param partialText the partial text of the target component
     * @param tagName     the HTML tag, input empty string or * for any tag
     * @param isClickable should wait until the element becomes clickable?
     * @return WebElement
     */
    public static WebElement getByPartialText(String partialText, @Optional String tagName, @Optional boolean isClickable) {
        // Use "*" if tagName is not provided.
        String tag = (tagName != null && !tagName.isEmpty()) ? tagName : "*";

        // Construct XPath for partial text match.
        String xpath = String.format("//%s[contains(text(),'%s')]", tag, partialText);

        // Call the common method.
        return getByXpath(xpath, isClickable);
    }

    /**
     * @param partialText the partial text of the target component
     * @param tagName     the HTML tag, input empty string or * for any tag
     * @param isClickable should wait until the element becomes clickable?
     * @return WebElement
     */
    public static WebElement getByPartialText(String partialText, @Optional String tagName, int index, @Optional boolean isClickable) {
        // Use "*" if tagName is not provided.
        String tag = (tagName != null && !tagName.isEmpty()) ? tagName : "*";

        // Construct XPath for partial text match.
        String xpath = String.format("(//%s[contains(text(),'%s')])[%d]", tag, partialText, index);

        // Call the common method.
        return getByXpath(xpath, isClickable);
    }

    /**
     * Gets an element by tag name that contains specific partial text at a given index.
     *
     * @param tagName     the tag name of the element (e.g., "p", "div", "span")
     * @param partialText the partial text to search for
     * @param index       the 1-based index of the matching element
     * @return WebElement representing the matching element at the specified index
     */
    public static WebElement getElementByPartialTextWithIndex(String tagName, String partialText, int index, @Optional boolean isClickable) {
        String xpath = String.format("(//%s[contains(text(),'%s')])[%d]", tagName, partialText, index);
        By byXpath = By.xpath(xpath);

        if (Boolean.TRUE.equals(isClickable)) {
            waitForElementToBeClickableBy(byXpath);
        }
        return webDriver.findElement(byXpath);
    }

    /**
     * Gets a div element by its class name using XPath.
     *
     * @param className the class name to search for
     * @return WebElement representing the div with the specified class name
     */
    public static WebElement getByDivClass(String className, @Optional boolean isClickable) {
        String xpath = String.format("//div[@class='%s']", className);
        return webDriver.findElement(By.xpath(xpath));
    }

    public static WebElement getByName(String name, boolean isClickable) {
        By byXpath = By.name(name);
        return find(byXpath, isClickable);
    }

    /**
     * Gets the element by its id,
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param Id          id of the target element
     * @param isClickable true or false
     * @return The target web element
     */
    public static WebElement getById(String Id, boolean isClickable) {
        By byId = By.id(Id);
        return find(byId, isClickable);
    }

    /**
     * Gets the element by its className,
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param className   Class name of the target element
     * @param isClickable true or false
     * @return The target web element
     */

    public static WebElement getByClassName(String className, boolean isClickable) {
        By byClassName = By.className(className);
        return find(byClassName, isClickable);
    }

    /**
     * Gets the element by its cssSelector,
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param cssSelector of the target element
     * @param isClickable true or false
     * @return The target web element
     */
    public static WebElement getByCssSelector(String cssSelector, boolean isClickable) {
        By byCssSelector = By.cssSelector(cssSelector);
        return find(byCssSelector, isClickable);
    }


    /**
     * Gets the element by its AccessibilityId,
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param AccessibilityId of the target element
     * @param isClickable     true or false
     * @return The target web element
     */
    public static WebElement getByAccessibilityId(String AccessibilityId, boolean isClickable) {
        By byAccessibilityId = MobileBy.AccessibilityId(AccessibilityId);
        return find(byAccessibilityId, isClickable);
    }

    public static List<WebElement> getListByXpath(String xpath) {
        By byXpath = By.xpath(xpath);
        waitForElementToBeVisibleBy(byXpath);
        return webDriver.findElements(byXpath);
    }

    public static WebElement visibleAndInteractiveInXSeconds(String xpath, @Optional boolean isClickable, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        By byXpath = By.xpath(xpath);
        return wait.ignoring(StaleElementReferenceException.class)
                .until(browser -> isClickable
                        ? ExpectedConditions.elementToBeClickable(byXpath).apply(browser)
                        : ExpectedConditions.visibilityOfElementLocated(byXpath).apply(browser));
    }


    /**
     * Gets element by tagName, which the target element located ABOVE the referenced web element
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param targetTagName tag name of the target element
     * @param element       the reference element
     * @param isClickable   true or false
     * @return The target web element
     */
    public static WebElement getByAboveElement(String targetTagName, WebElement element, boolean isClickable) {
        By relativeLocator = with(By.tagName(targetTagName)).below(element);
        return find(relativeLocator, isClickable);
    }

    /**
     * Gets element by tagName, which the target element located BELOW the referenced web element
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param targetTagName    tag name of the target element
     * @param referenceElement the reference element
     * @param isClickable      true or false
     * @return The target web element
     */
    public static WebElement getByBelowElement(String targetTagName, WebElement referenceElement, boolean isClickable) {
        By relativeLocator = with(By.tagName(targetTagName)).above(referenceElement);
        return find(relativeLocator, isClickable);
    }


    /**
     * Gets element by tagName, which the target element located RIGHT to the referenced web element
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param targetTagName    tag name of the target element
     * @param referenceElement the reference element
     * @param isClickable      true or false
     * @return The target web element
     */
    public static WebElement getByRightElement(String targetTagName, WebElement referenceElement, boolean isClickable) {
        By relativeLocator = with(By.tagName(targetTagName)).toLeftOf(referenceElement);
        return find(relativeLocator, isClickable);
    }

    /**
     * Gets element by tagName, which the target element located LEFT to the referenced web element
     * Waits for element to be visible and accessible
     * Waits for element to be clickable if isClickable is true
     *
     * @param targetTagName    tag name of the target element
     * @param referenceElement the reference element
     * @param isClickable      true or false
     * @return The target web element
     */
    public static WebElement getByLeftElement(String targetTagName, WebElement referenceElement, boolean isClickable) {
        By relativeLocator = with(By.tagName(targetTagName)).toRightOf(referenceElement);
        return find(relativeLocator, isClickable);
    }

    private static WebDriverWait getWait() {
        return new WebDriverWait(webDriver, Duration.ofSeconds(PipelineConfig.uiWaitTimeoutSeconds));
    }

    public static int getRowNumberContainsText(String targetText) {
        List<WebElement> rows = driver.findElements(By.xpath("//tr"));
        for (int i = 1; i < rows.size(); i++) {
            List<WebElement> child = Finder.getAllChildrenByXpath("//tr[" + i + "]");
            for (WebElement children : child) {
                if (children.getText().equals(targetText)) {
                    return i;
                }
            }
        }
        return 0;
    }

    public static WebElement getElementInRowUnderHeader(String headerText, int rowNumber) {
//        String headerXPath = "//table//th[normalize-space(text())='" + headerText + "']";
        String headerXPath = "//table//th[contains(text(), '" + headerText + "')]";
        WebElement headerElement = getByXpath(headerXPath, false);

        List<WebElement> headers = webDriver.findElements(By.xpath("//table//th"));
        int columnIndex = headers.indexOf(headerElement) + 1;

        String cellXPath = "//table//tr[" + rowNumber + "]//td[" + columnIndex + "]";
        return getByXpath(cellXPath, false);
    }


    /**
     * Gets all children of target element by its xpath
     *
     * @param xpath of the target element
     * @return List<WebElement>
     */
    public static List<WebElement> getAllChildrenByXpath(String xpath) {
        return Finder.getByXpath(xpath, false).findElements(By.xpath("./child::*"));
    }

    /**
     * Gets the image element by its alt attribute
     *
     * @param altText the alt text of the image
     * @return WebElement matching the alt attribute
     */
    public static WebElement getByAlt(String altText, boolean isClickable) {
        String xpath = String.format("//img[@alt='%s']", altText);
        return Finder.getByXpath(xpath, isClickable);
    }

    /**
     * Gets the div element by its part attribute.
     *
     * @param partValue the value of the part attribute
     * @return WebElement representing the div with the specified part attribute
     */
    public static WebElement getByPart(String partValue, boolean isClickable) {
        String xpath = String.format("//div[@part='%s']", partValue);
        System.out.println(xpath);
        return Finder.getByXpath(xpath, isClickable);
    }


    /**
     * Gets full xpath of the target element
     *
     * @param element the target element
     * @return String
     */
    public static String getElementXPath(WebElement element) {
        return (String) ((JavascriptExecutor) webDriver).executeScript(
                "function getXPath(elt){" +
                        "var path = '';" +
                        "for (; elt && elt.nodeType == 1; elt = elt.parentNode) {" +
                        "idx = getElementIdx(elt);" +
                        "xname = elt.tagName;" +
                        "if (idx > 1) xname += '[' + idx + ']';" +
                        "path = '/' + xname + path;" +
                        "}" +
                        "return path;" +
                        "}" +
                        "function getElementIdx(elt){" +
                        "var count = 1;" +
                        "for (var sib = elt.previousSibling; sib ; sib = sib.previousSibling) {" +
                        "if(sib.nodeType == 1 && sib.tagName == elt.tagName) count++;" +
                        "}" +
                        "return count;" +
                        "}" +
                        "return getXPath(arguments[0]).toLowerCase();", element);
    }

    public static WebElement getInputByTitle(String title) {
        return getByXpath("//input[@title='" + title + "']", false);
    }


}
