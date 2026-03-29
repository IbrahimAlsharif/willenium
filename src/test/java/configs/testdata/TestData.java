package configs.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class TestData {
    private JsonNode testData;

    public TestData(String fileName){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            testData = objectMapper.readTree(new File("src/test/java/configs/testdata/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode getUserData(String userType) {
        return testData.get("users").get(userType);
    }

    public JsonNode getBaseUrl(String language) {
        return testData.get("base-url").get("env").get(language);
    }

    public JsonNode getApiData() {
        return testData.get("api");
    }

    public JsonNode getTopicData() {
        return testData.get("topic");
    }

    public JsonNode getWeWillHomeData() {
        return testData.get("wewillHome");
    }

    public JsonNode getLoginData() {
        return testData.get("login");
    }

    public JsonNode getHomeData() {
        return testData.get("home");
    }

    public JsonNode getSearchData() {
        return testData.get("search");
    }

    public JsonNode getSearchModalData() {
        return testData.get("searchModal");
    }

    public JsonNode getAnalysisBusinessData() {
        return testData.get("analysisBusiness");
    }

    public JsonNode getAnalysisEntitiesData() {
        return testData.get("analysisEntities");
    }

    public JsonNode getAnalysisInfluencersData() {
        return testData.get("analysisInfluencers");
    }

    public JsonNode getAnalysisParliamentProfileData() {
        return testData.get("analysisParliamentProfile");
    }

    public JsonNode getAnalysisParliamentProfileEntityData() {
        return testData.get("analysisParliamentProfile").get("entity2");
    }

    public JsonNode getAnalysisParliamentProfileStaticData() {
        return testData.get("analysisParliamentProfile").get("static");
    }

    public JsonNode getAnalysisGovernmentalProfileData() {
        return testData.get("analysisGovernmentalProfile");
    }

    public JsonNode getAnalysisGovernmentalProfileEntityData() {
        return testData.get("analysisGovernmentalProfile").get("entity2");
    }

    public JsonNode getAnalysisGovernmentalProfileStaticData() {
        return testData.get("analysisGovernmentalProfile").get("static");
    }
}
