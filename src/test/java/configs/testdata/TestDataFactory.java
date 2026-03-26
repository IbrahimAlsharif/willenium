package configs.testdata;

public class TestDataFactory {

    public static TestData getTestData(String branch, String language) {
        String testDataFileName;
        boolean isProduction = branch.equalsIgnoreCase("production");
        boolean isEnglish = language.equalsIgnoreCase("english");

        if (isProduction) {
            if (isEnglish) {
                testDataFileName = "exampleProductionEnglish.json";
            } else {
                testDataFileName = "exampleProductionArabic.json";
            }
        } else {
            if (isEnglish) {
                testDataFileName = "exampleStagingEnglish.json";
            } else {
                testDataFileName = "exampleStagingArabic.json";
            }
        }
        return new TestData(testDataFileName);
    }
}
