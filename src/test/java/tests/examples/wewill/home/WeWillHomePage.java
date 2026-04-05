package tests.examples.wewill.home;

import base.Finder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WeWillHomePage {

    public static WebElement getHeroHeading() {
        return Finder.get(By.cssSelector("[data-i18n='hero.title']"));
    }

    public static WebElement getPrimaryCallToAction() {
        return Finder.get(By.cssSelector("[data-i18n='hero.ctaPrimary']"));
    }

    public static WebElement getMethodologyHeading() {
        return Finder.get(By.cssSelector("[data-i18n='genai.card.title']"));
    }

    public static WebElement getBilingualOfferingCallToAction() {
        return Finder.get(By.cssSelector("[data-i18n='genai.cta']"));
    }

    public static WebElement getFooterSlogan() {
        return Finder.get(By.cssSelector("[data-site-setting='footer_tagline']"));
    }
}
