package tests.assistant.home;

import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Setup.testCaseId;
import static base.Setup.testData;

public class TopicCardTest {

    @Test(priority = 1)
    public void verifyTopicCardTitleVisible() {
        testCaseId = "9551";
        Assert.assertTrue(TopicCard.getTitle(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card title should be visible");
    }

    @Test(priority = 2)
    public void verifyTopicCardTimeVisible() {
        testCaseId = "9552";
        Assert.assertTrue(TopicCard.getTime(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card time should be visible");
    }

    //todo add test for every badge (category- severity)
    @Test(priority = 3)
    public void verifyTopicCardBadgesPresent() {
        testCaseId = "9553";
        Assert.assertTrue(TopicCard.getBadges(TopicCard.getFirstTopicCard()).size() > 0,
                "Topic card should have at least one badge");
    }

    @Test(priority = 4)
    public void verifyTopicCardSummaryVisible() {
        testCaseId = "9554";
        Assert.assertTrue(TopicCard.getSummary(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card summary should be visible");
    }

    //todo update countries xpaths
    @Test(priority = 5)
    public void verifyTopicCardCountriesValueVisible() {
        testCaseId = "9555";
        Assert.assertTrue(TopicCard.getSourcesValue(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card Sources value should be visible");
    }

//    @Test(priority = 6)
//    public void verifyTopicCardMentionsValueVisible() {
//        testCaseId = "9556";
//        Assert.assertTrue(TopicCard.getMentionsValue(TopicCard.getFirstTopicCard()).isDisplayed(),
//                "Topic card Mentions value should be visible");
//    }

    @Test(priority = 7)
    public void verifyTopicCardSentimentSectionVisible() {
        testCaseId = "9557";
        Assert.assertTrue(TopicCard.getSentimentSection(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card Sentiment section should be visible");
    }

    //todo update xpath
//    @Test(priority = 8)
//    public void verifyTopicCardSentimentValuesPresent() {
//        testCaseId = "9558";
//        Assert.assertTrue(TopicCard.getSentimentValues(TopicCard.getFirstTopicCard()).size() > 0,
//                "Topic card Sentiment values should be present");
//    }

    @Test(priority = 9)
    public void verifyTopicCardActionLinkVisible() {
        testCaseId = "9559";
        Assert.assertTrue(TopicCard.getActionLink(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card action link should be visible");
    }

    @Test(priority = 10)
    public void verifyTopicCardCategoryBadgeVisible() {
        testCaseId = "9560";
        Assert.assertTrue(TopicCard.getCategoryBadge(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card category badge should be visible");
    }

//    @Test(priority = 11)
//    public void verifyTopicCardNetSentimentSectionVisible() {
//        testCaseId = "9561";
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertTrue(TopicCard.getNetSentimentSection(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Net Sentiment section should be visible");
//    }
//
//    @Test(priority = 12)
//    public void verifyTopicCardNetSentimentTooltipVisible() {
//        testCaseId = "9562";
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertTrue(TopicCard.getNetSentimentTooltipButton(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Net Sentiment tooltip button should be visible");
//    }
//
//    @Test(priority = 13)
//    public void verifyTopicCardNetSentimentTrendIconVisible() {
//        testCaseId = "9563";
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertTrue(TopicCard.getNetSentimentTrendIcon(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Net Sentiment trend icon should be visible");
//    }
//
//    @Test(priority = 14)
//    public void verifyTopicCardNetSentimentValueVisible() {
//        testCaseId = "9564";
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertTrue(TopicCard.getNetSentimentValue(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Net Sentiment value should be visible");
//    }
//
//    @Test(priority = 15)
//    public void verifyTopicCardNetSentimentLabelVisible() {
//        testCaseId = "9565";
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertTrue(TopicCard.getNetSentimentLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Net Sentiment label should be visible");
//    }

    @Test(priority = 16)
    public void verifyTopicCardCountriesLabelVisible() {
        testCaseId = "9566";
        String label = testData.getTopicData().get("sourcesLabel").asText();
        Assert.assertTrue(TopicCard.getCountriesLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
                "Topic card Sources label should be visible");
    }

    @Test(priority = 17)
    public void verifyTopicCardCountriesTooltipVisible() {
        testCaseId = "9567";
        String label = testData.getTopicData().get("sourcesLabel").asText();
        Assert.assertTrue(TopicCard.getSourcesTooltipButton(TopicCard.getFirstTopicCard(), label).isDisplayed(),
                "Topic card Sources tooltip button should be visible");
    }

//    @Test(priority = 18)
//    public void verifyTopicCardMentionsLabelVisible() {
//        testCaseId = "9568";
//        String label = testData.getTopicData().get("mentionsLabel").asText();
//        Assert.assertTrue(TopicCard.getMentionsLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Mentions label should be visible");
//    }
//
//    @Test(priority = 19)
//    public void verifyTopicCardMentionsTooltipVisible() {
//        testCaseId = "9569";
//        String label = testData.getTopicData().get("mentionsLabel").asText();
//        Assert.assertTrue(TopicCard.getMentionsTooltipButton(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Mentions tooltip button should be visible");
//    }

    @Test(priority = 26)
    public void verifyTopicCardSentimentLabelVisible() {
        testCaseId = "9570";
        String label = testData.getTopicData().get("sentimentLabel").asText();
        Assert.assertTrue(TopicCard.getSentimentLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
                "Topic card Sentiment label should be visible");
    }

    @Test(priority = 27)
    public void verifyTopicCardSentimentTooltipVisible() {
        testCaseId = "9571";
        String label = testData.getTopicData().get("sentimentLabel").asText();
        Assert.assertTrue(TopicCard.getSentimentTooltipButton(TopicCard.getFirstTopicCard(), label).isDisplayed(),
                "Topic card Sentiment tooltip button should be visible");
    }

    @Test(priority = 27)
    public void verifyTopicCardSentimentValue() {
        testCaseId = "10233";
        Assert.assertTrue(TopicCard.getSentimentValue(TopicCard.getFirstTopicCard()).isDisplayed(),
                "Topic card Positive sentiment value should be visible");
    }

//    @Test(priority = 20)
//    public void verifyTopicCardPositiveSentimentLabelVisible() {
//        testCaseId = "9572";
//        String label = testData.getTopicData().get("positiveLabel").asText();
//        Assert.assertTrue(TopicCard.getPositiveSentimentLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Positive sentiment label should be visible");
//    }
//
//    @Test(priority = 21)
//    public void verifyTopicCardPositiveSentimentValueVisible() {
//        testCaseId = "9573";
//        Assert.assertTrue(TopicCard.getPositiveSentimentValue(TopicCard.getFirstTopicCard()).isDisplayed(),
//                "Topic card Positive sentiment value should be visible");
//    }
//
//    @Test(priority = 22)
//    public void verifyTopicCardNeutralSentimentLabelVisible() {
//        testCaseId = "9574";
//        String label = testData.getTopicData().get("neutralLabel").asText();
//        Assert.assertTrue(TopicCard.getNeutralSentimentLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Neutral sentiment label should be visible");
//    }
//
//    @Test(priority = 23)
//    public void verifyTopicCardNeutralSentimentValueVisible() {
//        testCaseId = "9575";
//        Assert.assertTrue(TopicCard.getNeutralSentimentValue(TopicCard.getFirstTopicCard()).isDisplayed(),
//                "Topic card Neutral sentiment value should be visible");
//    }
//
//    @Test(priority = 24)
//    public void verifyTopicCardNegativeSentimentLabelVisible() {
//        testCaseId = "9576";
//        String label = testData.getTopicData().get("negativeLabel").asText();
//        Assert.assertTrue(TopicCard.getNegativeSentimentLabel(TopicCard.getFirstTopicCard(), label).isDisplayed(),
//                "Topic card Negative sentiment label should be visible");
//    }
//
//    @Test(priority = 25)
//    public void verifyTopicCardNegativeSentimentValueVisible() {
//        testCaseId = "9577";
//        Assert.assertTrue(TopicCard.getNegativeSentimentValue(TopicCard.getFirstTopicCard()).isDisplayed(),
//                "Topic card Negative sentiment value should be visible");
//    }

    @Test(priority = 28)
    public void verifyTopicCardTitleData() {
        testCaseId = "9578";
        String expected = testData.getTopicData().get("title").asText();
        Assert.assertEquals(TopicCard.getText(TopicCard.getTitle(TopicCard.getFirstTopicCard())), expected,
                "Topic card title should match expected data");
    }

    @Test(priority = 29)
    public void verifyTopicCardRiskBadgeData() {
        testCaseId = "9579";
        String expected = testData.getTopicData().get("risk").asText();
        Assert.assertEquals(TopicCard.getText(TopicCard.getRiskBadge(TopicCard.getFirstTopicCard())), expected,
                "Topic card risk badge should match expected data");
    }

    @Test(priority = 30)
    public void verifyTopicCardCategoryBadgeData() {
        testCaseId = "9580";
        String expected = testData.getTopicData().get("category").asText();
        Assert.assertEquals(TopicCard.getText(TopicCard.getCategoryBadge(TopicCard.getFirstTopicCard())), expected,
                "Topic card category badge should match expected data");
    }

//    @Test(priority = 31)
//    public void verifyTopicCardNetSentimentData() {
//        testCaseId = "9581";
//        String expected = testData.getTopicData().get("netSentiment").asText();
//        String label = testData.getTopicData().get("netSentimentLabel").asText();
//        Assert.assertEquals(TopicCard.getText(TopicCard.getNetSentimentValue(TopicCard.getFirstTopicCard(), label)), expected,
//                "Topic card net sentiment value should match expected data");
//    }

    @Test(priority = 32)
    public void verifyTopicCardSummaryDataPrefix() {
        testCaseId = "9582";
        String expected = testData.getTopicData().get("summaryPrefix").asText();
        String actual = TopicCard.getText(TopicCard.getSummary(TopicCard.getFirstTopicCard())).replaceAll("\\s+", " ");
        Assert.assertTrue(actual.startsWith(expected),
                "Topic card summary should start with expected data");
    }

    @Test(priority = 33)
    public void verifyTopicCardCountriesData() {
        testCaseId = "9583";
        String expected = testData.getTopicData().get("sources").asText();
        Assert.assertEquals(TopicCard.getText(TopicCard.getSourcesValue(TopicCard.getFirstTopicCard())), expected,
                "Topic card sources value should match expected data");
    }

//    @Test(priority = 34)
//    public void verifyTopicCardMentionsData() {
//        testCaseId = "9584";
//        String expected = testData.getTopicData().get("mentions").asText();
//        Assert.assertEquals(TopicCard.getText(TopicCard.getMentionsValue(TopicCard.getFirstTopicCard())), expected,
//                "Topic card mentions value should match expected data");
//    }
//
//    @Test(priority = 35)
//    public void verifyTopicCardPositiveSentimentData() {
//        testCaseId = "9585";
//        String expected = testData.getTopicData().get("positive").asText();
//        Assert.assertEquals(TopicCard.getText(TopicCard.getPositiveSentimentValue(TopicCard.getFirstTopicCard())), expected,
//                "Topic card positive sentiment value should match expected data");
//    }
//
//    @Test(priority = 36)
//    public void verifyTopicCardNeutralSentimentData() {
//        testCaseId = "9586";
//        String expected = testData.getTopicData().get("neutral").asText();
//        Assert.assertEquals(TopicCard.getText(TopicCard.getNeutralSentimentValue(TopicCard.getFirstTopicCard())), expected,
//                "Topic card neutral sentiment value should match expected data");
//    }
//
//    @Test(priority = 37)
//    public void verifyTopicCardNegativeSentimentData() {
//        testCaseId = "9587";
//        String expected = testData.getTopicData().get("negative").asText();
//        Assert.assertEquals(TopicCard.getText(TopicCard.getNegativeSentimentValue(TopicCard.getFirstTopicCard())), expected,
//                "Topic card negative sentiment value should match expected data");
//    }

    @Test(priority = 38)
    public void verifyTopicCardActionTextData() {
        testCaseId = "9588";
        String expected = testData.getTopicData().get("actionText").asText();
        Assert.assertTrue(TopicCard.getText(TopicCard.getActionLink(TopicCard.getFirstTopicCard())).contains(expected),
                "Topic card action text should match expected data");
    }

    //todo add test for tooltips content and visibility when hover
    //todo add tests for top source
}
