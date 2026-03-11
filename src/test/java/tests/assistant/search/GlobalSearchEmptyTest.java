package tests.assistant.search;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class GlobalSearchEmptyTest {

    @Test(priority = 1)
    public void verifyNoResultsBannerVisible() {
        testCaseId = "9667";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("emptyQuery").asText();
        String prefix = testData.getSearchData().get("noResultsPrefix").asText();
        GlobalSearch.searchFor(placeholder, query);
        Assert.assertTrue(GlobalSearch.getNoResultsBanner(prefix, query).isDisplayed(),
                "No results banner should be visible");
    }

    @Test(priority = 2, dependsOnMethods = "verifyNoResultsBannerVisible")
    public void verifyEmptyStateTitleVisible() {
        testCaseId = "9668";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("emptyQuery").asText();
        GlobalSearch.searchFor(placeholder, query);
        String title = testData.getSearchData().get("emptyStateTitle").asText();
        Assert.assertTrue(GlobalSearch.getEmptyStateTitle(title).isDisplayed(),
                "Empty state title should be visible");
    }

    @Test(priority = 3, dependsOnMethods = "verifyEmptyStateTitleVisible")
    public void verifyEmptyStateSubtitleVisible() {
        testCaseId = "9669";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("emptyQuery").asText();
        GlobalSearch.searchFor(placeholder, query);
        String title = testData.getSearchData().get("emptyStateTitle").asText();
        String subtitle = testData.getSearchData().get("emptyStateSubtitle").asText();
        Assert.assertEquals(GlobalSearch.getEmptyStateSubtitleByTitle(title).getText().trim(), subtitle);
    }
}
