package tests.assistant.home;

import base.Finder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TopicCard {
    private static final String TOPIC_CARD_ROOT_XPATH =
            ".//a[starts-with(@href,'/assistant/topics/') and not(contains(@href,'/alert_'))] /ancestor::div[contains(@class,'w-full') and contains(@class,'max-w-full')][1]";
    private static final String TITLE_XPATH = ".//header//h3[normalize-space()!='']";
    private static final String TIME_XPATH =
            "//section[contains(@class,'space-y-1')]\n" +
                    "  //div[contains(@class,'justify-between')]\n" +
                    "  //p[contains(@class,'text-2xs') or contains(normalize-space(.),'ago')]";
    private static final String BADGE_XPATH = ".//span[@data-slot='badge']";
    private static final String RISK_BADGE_XPATH = "(.//span[@data-slot='badge'])[1]";
    private static final String CATEGORY_BADGE_XPATH = "(.//span[@data-slot='badge'])[2]";
    private static final String SUMMARY_XPATH =
            ".//div[contains(@class,'md:min-h-12') or contains(@class,'min-h')]" +
                    "//p[normalize-space()!='']";
    private static final String SOURCES_VALUE_XPATH =
            ".//section//div[contains(@class,'border')]/article[1]//h3";
    private static final String MENTIONS_VALUE_XPATH =
            ".//section//div[contains(@class,'border')]/article[2]//h3";
    private static final String SOURCES_LABEL_BASE_XPATH =
            ".//section//div[contains(@class,'border')]/article[1]";
    private static final String MENTIONS_LABEL_BASE_XPATH =
            ".//section//div[contains(@class,'border')]/article[2]";
    private static final String SENTIMENT_SECTION_XPATH = ".//article[contains(@class,'border-t')]";
    private static final String SENTIMENT_VALUES_XPATH = ".//article[contains(@class,'border-t')]//h3";
    private static final String SENTIMENT_LABEL_BASE_XPATH =
            ".//article[contains(@class,'border-t')]";
    private static final String SENTIMENT_VALUE_XPATH =
            "(.//article[contains(@class,'border-t')]//*[contains(@class,'font-bold')])[1]";
    private static final String SENTIMENT_POSITIVE_VALUE_XPATH =
            "(.//article[contains(@class,'border-t')]//h3[contains(@class,'percentage')])[1]";
    private static final String SENTIMENT_NEUTRAL_VALUE_XPATH =
            "(.//article[contains(@class,'border-t')]//h3[contains(@class,'percentage')])[2]";
    private static final String SENTIMENT_NEGATIVE_VALUE_XPATH =
            "(.//article[contains(@class,'border-t')]//h3[contains(@class,'percentage')])[3]";
    private static final String SENTIMENT_LABEL_XPATH_TEMPLATE =
            ".//article[contains(@class,'border-t')]//p[normalize-space()='%s']";
    private static final String ACTION_XPATH = ".//a[starts-with(@href,'/assistant/topics/')]";
    private static final String NET_SENTIMENT_CONTAINER_BY_LABEL_XPATH =
            ".//span[normalize-space()='%s']/ancestor::div[contains(@class,'items-center')][1]";
    private static final String NET_SENTIMENT_LABEL_XPATH_TEMPLATE = ".//span[normalize-space()='%s']";

    public static WebElement getFirstTopicCard() {
        return Finder.getByXpath(TOPIC_CARD_ROOT_XPATH, false);
    }

    public static WebElement getTitle(WebElement card) {
        return Finder.getByXpathInParent(TITLE_XPATH, card, false);
    }

    public static WebElement getTime(WebElement card) {
        return Finder.getByXpathInParent(TIME_XPATH, card, false);
    }

    public static List<WebElement> getBadges(WebElement card) {
        return card.findElements(By.xpath(BADGE_XPATH));
    }

    public static WebElement getRiskBadge(WebElement card) {
        return Finder.getByXpathInParent(RISK_BADGE_XPATH, card, false);
    }

    public static WebElement getCategoryBadge(WebElement card) {
        return Finder.getByXpathInParent(CATEGORY_BADGE_XPATH, card, false);
    }

    public static WebElement getSummary(WebElement card) {
        return Finder.getByXpathInParent(SUMMARY_XPATH, card, false);
    }

    public static WebElement getSourcesValue(WebElement card) {
        return Finder.getByXpathInParent(SOURCES_VALUE_XPATH, card, false);
    }

    public static WebElement getMentionsValue(WebElement card) {
        return Finder.getByXpathInParent(MENTIONS_VALUE_XPATH, card, false);
    }

    public static WebElement getCountriesLabel(WebElement card, String labelText) {
        String xpath = String.format(SOURCES_LABEL_BASE_XPATH + "//p[normalize-space()='%s']", labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getMentionsLabel(WebElement card, String labelText) {
        String xpath = String.format(MENTIONS_LABEL_BASE_XPATH + "//p[normalize-space()='%s']", labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getSourcesTooltipButton(WebElement card, String labelText) {
        String xpath = String.format(SOURCES_LABEL_BASE_XPATH + "//p[normalize-space()='%s']" +
                "/ancestor::div[contains(@class,'items-center')][1]//button[@data-slot='tooltip-trigger']", labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getMentionsTooltipButton(WebElement card, String labelText) {
        String xpath = String.format(MENTIONS_LABEL_BASE_XPATH + "//p[normalize-space()='%s']" +
                "/ancestor::div[contains(@class,'items-center')][1]//button[@data-slot='tooltip-trigger']", labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getSentimentSection(WebElement card) {
        return Finder.getByXpathInParent(SENTIMENT_SECTION_XPATH, card, false);
    }

    public static List<WebElement> getSentimentValues(WebElement card) {
        return card.findElements(By.xpath(SENTIMENT_VALUES_XPATH));
    }

    public static WebElement getSentimentLabel(WebElement card, String labelText) {
        String xpath = String.format(SENTIMENT_LABEL_XPATH_TEMPLATE, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getSentimentTooltipButton(WebElement card, String labelText) {
        String xpath = String.format(SENTIMENT_LABEL_BASE_XPATH + "//p[normalize-space()='%s']" +
                "/ancestor::div[contains(@class,'items-center')][1]//button[@data-slot='tooltip-trigger']", labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getSentimentValue(WebElement card) {
        return Finder.getByXpathInParent(SENTIMENT_VALUE_XPATH, card, false);
    }

    public static WebElement getPositiveSentimentValue(WebElement card) {
        return Finder.getByXpathInParent(SENTIMENT_POSITIVE_VALUE_XPATH, card, false);
    }

    public static WebElement getNeutralSentimentValue(WebElement card) {
        return Finder.getByXpathInParent(SENTIMENT_NEUTRAL_VALUE_XPATH, card, false);
    }

    public static WebElement getNegativeSentimentValue(WebElement card) {
        return Finder.getByXpathInParent(SENTIMENT_NEGATIVE_VALUE_XPATH, card, false);
    }

    public static WebElement getPositiveSentimentLabel(WebElement card, String labelText) {
        String xpath = String.format(SENTIMENT_LABEL_XPATH_TEMPLATE, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getNeutralSentimentLabel(WebElement card, String labelText) {
        String xpath = String.format(SENTIMENT_LABEL_XPATH_TEMPLATE, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getNegativeSentimentLabel(WebElement card, String labelText) {
        String xpath = String.format(SENTIMENT_LABEL_XPATH_TEMPLATE, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getActionLink(WebElement card) {
        return Finder.getByXpathInParent(ACTION_XPATH, card, false);
    }

    public static String getText(WebElement element) {
        return element.getText().trim();
    }

    private static WebElement getNetSentimentContainer(WebElement card, String labelText) {
        String xpath = String.format(NET_SENTIMENT_CONTAINER_BY_LABEL_XPATH, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }

    public static WebElement getNetSentimentSection(WebElement card, String labelText) {
        return getNetSentimentContainer(card, labelText);
    }

    public static WebElement getNetSentimentTooltipButton(WebElement card, String labelText) {
        WebElement container = getNetSentimentContainer(card, labelText);
        return Finder.getByXpathInParent(".//button[@data-slot='tooltip-trigger']", container, false);
    }

    public static WebElement getNetSentimentTrendIcon(WebElement card, String labelText) {
        WebElement container = getNetSentimentContainer(card, labelText);
        return Finder.getByXpathInParent(".//*[name()='svg' and not(ancestor::button[@data-slot='tooltip-trigger'])][1]",
                container, false);
    }

    public static WebElement getNetSentimentValue(WebElement card, String labelText) {
        WebElement container = getNetSentimentContainer(card, labelText);
        return Finder.getByXpathInParent(".//span[@dir='ltr']", container, false);
    }

    public static WebElement getNetSentimentLabel(WebElement card, String labelText) {
        String xpath = String.format(NET_SENTIMENT_LABEL_XPATH_TEMPLATE, labelText);
        return Finder.getByXpathInParent(xpath, card, false);
    }
}
