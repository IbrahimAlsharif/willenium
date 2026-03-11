package tests.assistant.search;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class GlobalSearchValidTest {

    @Test(priority = 1)
    public void verifyGlobalSearchInputVisible() {
        testCaseId = "9670";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        Assert.assertTrue(GlobalSearch.getGlobalSearchInput(placeholder).isDisplayed(),
                "Global search input should be visible");
    }

    @Test(priority = 2, dependsOnMethods = "verifyGlobalSearchInputVisible")
    public void verifySearchResultsBannerVisible() {
        testCaseId = "9671";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("query").asText();
        String prefix = testData.getSearchData().get("resultsPrefix").asText();
        GlobalSearch.searchFor(placeholder, query);
        Assert.assertTrue(GlobalSearch.getResultsBanner(prefix, query).isDisplayed(),
                "Search results banner should be visible");
    }

    @Test(priority = 3, dependsOnMethods = "verifySearchResultsBannerVisible")
    public void verifySearchResultsCardsExist() {
        testCaseId = "9672";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("query").asText();
        GlobalSearch.searchFor(placeholder, query);
        Assert.assertTrue(GlobalSearch.getResultCards().size() > 0,
                "Search results should return at least one topic card");
    }

    @Test(priority = 4, dependsOnMethods = "verifySearchResultsCardsExist")
    public void verifyFirstSearchResultTitleContainsQuery() {
        testCaseId = "9673";
        String placeholder = testData.getSearchData().get("globalSearchPlaceholder").asText();
        String query = testData.getSearchData().get("query").asText();
        GlobalSearch.searchFor(placeholder, query);
        Assert.assertTrue(GlobalSearch.getFirstResultTitle().getText().contains(query),
                "First search result title should contain the query");
    }
}
