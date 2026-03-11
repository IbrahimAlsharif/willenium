package configs.testdata;

public class TestDataFactory {

    public static TestData getTestData(String branch, String language) {
        String testDataFileName;
        boolean isProduction = branch.equalsIgnoreCase("production");
        boolean isEnglish = language.equalsIgnoreCase("english");

        if (isProduction) {
            if (isEnglish) {
                testDataFileName = "productionEnglish.json";
            } else {
                testDataFileName = "productionArabic.json";
            }
        } else {
            if (isEnglish) {
                testDataFileName = "stagingEnglish.json";
            } else {
                testDataFileName = "stagingArabic.json";
            }
        }
        return new TestData(testDataFileName);
    }
}
