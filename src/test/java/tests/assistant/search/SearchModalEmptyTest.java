package tests.assistant.search;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class SearchModalEmptyTest {

    @Test(priority = 1)
    public void verifyModalNoResultsBannerVisible() {
        testCaseId = "9679";
        String query = testData.getSearchModalData().get("emptyQuery").asText();
        String prefix = testData.getSearchModalData().get("noResultsPrefix").asText();
        SearchModal.openModalFromSidebar();
        SearchModal.searchFor(query);
        Assert.assertTrue(SearchModal.getEmptyResultsBanner(prefix, query).isDisplayed(),
                "Modal no results banner should be visible");
    }

    @Test(priority = 2)
    public void verifyModalNoResultsListEmpty() {
        testCaseId = "9680";
        String query = testData.getSearchModalData().get("emptyQuery").asText();
        SearchModal.searchFor(query);
        Assert.assertEquals(SearchModal.getResultLinksCount(), 0,
                "Modal results list should be empty for no results query");
    }
}
