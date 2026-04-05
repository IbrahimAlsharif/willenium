package tests.examples.wewill.home;

import base.Finder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WeWillHomePage {

    public static WebElement getHeroHeading() {
        return Finder.get(By.id("hero-heading"));
    }

    public static WebElement getPrimaryCallToAction() {
        return Finder.get(By.name("primary-cta"));
    }

    public static WebElement getMethodologyHeading() {
        return Finder.get(By.id("methodology-heading"));
    }

    public static WebElement getBilingualOfferingCallToAction() {
        return Finder.get(By.name("bilingual-offering-cta"));
    }

    public static WebElement getFooterSlogan() {
        return Finder.get(By.id("footer-slogan"));
    }
}
