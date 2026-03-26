package tests.examples.wewill.home;

import base.Finder;
import org.openqa.selenium.WebElement;

public class WeWillHomePage {

    public static WebElement getHeroHeading(String text) {
        return getDisplayedElement("//*[self::h1 or self::h2][contains(normalize-space(.),%s)]", text);
    }

    public static WebElement getPrimaryCallToAction(String text) {
        return getDisplayedElement("//a[contains(normalize-space(.),%s)] | //button[contains(normalize-space(.),%s)]", text);
    }

    public static WebElement getMethodologyHeading(String text) {
        return getDisplayedElement("//*[self::h2 or self::h3][contains(normalize-space(.),%s)]", text);
    }

    public static WebElement getBilingualOfferingCallToAction(String text) {
        return getDisplayedElement("//a[contains(normalize-space(.),%s)] | //button[contains(normalize-space(.),%s)]", text);
    }

    public static WebElement getFooterSlogan(String text) {
        return getDisplayedElement("//*[contains(normalize-space(.),%s)]", text);
    }

    private static WebElement getDisplayedElement(String xpathTemplate, String text) {
        String xpathLiteral = toXpathLiteral(text);
        String xpath = String.format(xpathTemplate, xpathLiteral, xpathLiteral);
        return Finder.getFirstDisplayedByXpath(xpath);
    }

    private static String toXpathLiteral(String text) {
        if (!text.contains("'")) {
            return "'" + text + "'";
        }

        String[] parts = text.split("'");
        StringBuilder builder = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            builder.append("'").append(parts[i]).append("'");
            if (i < parts.length - 1) {
                builder.append(", \"'\", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
