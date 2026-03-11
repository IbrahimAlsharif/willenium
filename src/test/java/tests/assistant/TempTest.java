package tests.assistant;

import base.Finder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TempTest {
    @Test
    public void readTableElement() {
        String actual = Finder.getElementInRowUnderHeader("Job title", 4).getText();
        String expected = "Automation Tester";
        Assert.assertEquals(actual, expected);
    }
}
