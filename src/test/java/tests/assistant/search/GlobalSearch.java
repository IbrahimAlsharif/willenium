package tests.assistant.search;

import base.Finder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GlobalSearch {
    private static final String SEARCH_INPUT_XPATH_TEMPLATE =
            "//input[@type='search' or @role='searchbox' or contains(@placeholder,'%s') or contains(@aria-label,'%s')]";
    private static final String RESULTS_BANNER_XPATH_TEMPLATE =
            "//*[contains(normalize-space(),'%s') and contains(normalize-space(),'%s')]";
    private static final String NO_RESULTS_BANNER_XPATH_TEMPLATE =
            "//*[contains(normalize-space(),'%s') and contains(normalize-space(),'%s')]";
    private static final String EMPTY_STATE_TITLE_XPATH_TEMPLATE =
            "//*[normalize-space()='%s']";
    private static final String EMPTY_STATE_SUBTITLE_REL_XPATH =
            ".//*[self::p or self::span or self::div][normalize-space()!='' and not(@role='img')][1]";
    private static final String RESULT_CARDS_XPATH =
            "//a[starts-with(@href,'/assistant/topics/') and not(contains(@href,'/alert_'))]" +
                    "/ancestor::div[contains(@class,'w-full') or contains(@class,'max-w-full')]";
    private static final String FIRST_RESULT_CARD_XPATH = "(" + RESULT_CARDS_XPATH + ")[1]";
    private static final String RESULT_TITLE_REL_XPATH = ".//header//h3[normalize-space()!='']";

    public static WebElement getGlobalSearchInput(String placeholderText) {
        String xpath = String.format(SEARCH_INPUT_XPATH_TEMPLATE, placeholderText, placeholderText);
        return Finder.getByXpath(xpath, false);
    }

    public static void searchFor(String placeholderText, String query) {
        WebElement input = getGlobalSearchInput(placeholderText);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(query);
    }

    public static WebElement getResultsBanner(String prefix, String query) {
        String xpath = String.format(RESULTS_BANNER_XPATH_TEMPLATE, prefix, query);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getNoResultsBanner(String prefix, String query) {
        String xpath = String.format(NO_RESULTS_BANNER_XPATH_TEMPLATE, prefix, query);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getEmptyStateTitle(String text) {
        String xpath = String.format(EMPTY_STATE_TITLE_XPATH_TEMPLATE, text);
        return Finder.getByXpath(xpath, false);
    }

    public static WebElement getEmptyStateSubtitleByTitle(String titleText) {
        WebElement title = getEmptyStateTitle(titleText);
        WebElement container = title.findElement(By.xpath("./parent::*"));
        return Finder.getByXpathInParent(EMPTY_STATE_SUBTITLE_REL_XPATH, container, false);
    }

    public static List<WebElement> getResultCards() {
        Finder.getByXpath(FIRST_RESULT_CARD_XPATH, false);
        return Finder.getListByXpath(RESULT_CARDS_XPATH);
    }

    public static WebElement getFirstResultTitle() {
        WebElement card = Finder.getByXpath(FIRST_RESULT_CARD_XPATH, false);
        return Finder.getByXpathInParent(RESULT_TITLE_REL_XPATH, card, false);
    }
}
