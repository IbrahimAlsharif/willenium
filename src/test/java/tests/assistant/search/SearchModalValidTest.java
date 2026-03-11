package tests.assistant.search;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class SearchModalValidTest {

    @Test(priority = 1)
    public void verifyModalSearchInputVisible() {
        testCaseId = "9674";
        SearchModal.openModalFromSidebar();
        Assert.assertTrue(SearchModal.getSearchInput().isDisplayed(),
                "Modal search input should be visible");
    }

    @Test(priority = 2)
    public void verifyModalSearchInputValue() {
        testCaseId = "9676";
        String query = testData.getSearchModalData().get("query").asText();
        SearchModal.searchFor(query);
        Assert.assertEquals(SearchModal.getSearchInput().getAttribute("value"), query,
                "Modal search input value should match the query");
    }

    @Test(priority = 3)
    public void verifyModalSearchResultsBannerVisible() {
        testCaseId = "9674";
        String query = testData.getSearchModalData().get("query").asText();
        String prefix = testData.getSearchModalData().get("resultsPrefix").asText();
        SearchModal.searchFor(query);
        Assert.assertTrue(SearchModal.getResultsBanner(prefix, query).isDisplayed(),
                "Modal search results banner should be visible");
    }

    @Test(priority = 4)
    public void verifyModalSearchResultsExist() {
        testCaseId = "9677";
        String query = testData.getSearchModalData().get("query").asText();
        SearchModal.searchFor(query);
        Assert.assertTrue(SearchModal.getResultLinks().size() > 0,
                "Modal search should return at least one topic result");
    }

    @Test(priority = 5)
    public void verifyFirstModalResultContainsQuery() {
        testCaseId = "9678";
        String query = testData.getSearchModalData().get("query").asText();
        SearchModal.searchFor(query);
        String title = SearchModal.getFirstResultTitleText();
        Assert.assertTrue(title.toLowerCase().contains(query.toLowerCase()),
                "Modal First result should contain the query");
    }
}
