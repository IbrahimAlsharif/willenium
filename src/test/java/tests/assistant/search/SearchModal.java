package tests.assistant.search;

import base.Finder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import tests.assistant.sidebar.AssistantSidebar;

import java.util.List;

import static base.Setup.driver;
import static base.Setup.testData;

public class SearchModal {
    private static final String MODAL_CONTAINER_XPATH =
            "(//section[.//input[@data-slot='input'] and .//article[contains(@class,'rounded-2xl')]])[2]";
    private static final String SEARCH_INPUT_XPATH = MODAL_CONTAINER_XPATH + "//input[@data-slot='input']";
    private static final String RESULTS_BANNER_XPATH_TEMPLATE =
            MODAL_CONTAINER_XPATH + "//*[contains(normalize-space(),'%s') and contains(normalize-space(),'%s')]";
    private static final String EMPTY_RESULTS_BANNER_XPATH_TEMPLATE =
            MODAL_CONTAINER_XPATH + "//span[contains(normalize-space(),'%s') and contains(normalize-space(),'%s')]";
    private static final String RESULT_LINKS_XPATH =
            MODAL_CONTAINER_XPATH + "//a[starts-with(@href,'/assistant/topics/') and not(contains(@href,'/alert_'))]";
    private static final String FIRST_RESULT_LINK_XPATH = "(" + RESULT_LINKS_XPATH + ")[1]";
    private static final String FIRST_RESULT_TITLE_XPATH =
            FIRST_RESULT_LINK_XPATH + "//p[@aria-label or @title]";

    public static void openModalFromSidebar() {
        By modalBy = By.xpath(MODAL_CONTAINER_XPATH);
        if (!Finder.elementIsVisible(modalBy)) {
            AssistantSidebar.getSidebarSearchLabel(testData.getHomeData().get("sidebarSearch").asText()).click();
        }
        getModalContainer();
    }

    public static WebElement getModalContainer() {
        return Finder.getByXpath(MODAL_CONTAINER_XPATH, false);
    }

    public static WebElement getSearchInput() {
        return Finder.getByXpath(SEARCH_INPUT_XPATH, false);
    }

    public static void searchFor(String query) {
        WebElement input = getSearchInput();
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(query);
    }

    public static WebElement getResultsBanner(String prefix, String query) {
        String xpath = String.format(RESULTS_BANNER_XPATH_TEMPLATE, prefix, query);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getEmptyResultsBanner(String prefix, String query) {
        String xpath = String.format(EMPTY_RESULTS_BANNER_XPATH_TEMPLATE, prefix, query);
        return Finder.getByXpath(xpath, false);
    }

    public static List<WebElement> getResultLinks() {
        return Finder.getListByXpath(RESULT_LINKS_XPATH);
    }

    public static int getResultLinksCount() {
        return driver.findElements(By.xpath(RESULT_LINKS_XPATH)).size();
    }

    public static WebElement getFirstResultLink() {
        return Finder.getByXpath(FIRST_RESULT_LINK_XPATH, false);
    }

    public static String getFirstResultTitleText() {
        WebElement title = Finder.getByXpath(FIRST_RESULT_TITLE_XPATH, false);
        String ariaLabel = title.getAttribute("aria-label");
        if (ariaLabel != null && !ariaLabel.trim().isEmpty()) {
            return ariaLabel.trim();
        }
        String titleAttr = title.getAttribute("title");
        if (titleAttr != null && !titleAttr.trim().isEmpty()) {
            return titleAttr.trim();
        }
        return title.getText().trim();
    }
}
