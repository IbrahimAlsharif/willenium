package configs.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class TestDataStructureTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TESTDATA_DIRECTORY = "src/test/java/configs/testdata/";
    private static final String PRODUCTION_ARABIC = "exampleProductionArabic.json";
    private static final String PRODUCTION_ENGLISH = "exampleProductionEnglish.json";
    private static final String STAGING_ARABIC = "exampleStagingArabic.json";
    private static final String STAGING_ENGLISH = "exampleStagingEnglish.json";

    @Test
    public void verifyEnvironmentAndLanguageTestDataFilesShareTheSameStructure() throws IOException {
        JsonNode productionArabic = readTestData(PRODUCTION_ARABIC);
        JsonNode productionEnglish = readTestData(PRODUCTION_ENGLISH);
        JsonNode stagingArabic = readTestData(STAGING_ARABIC);
        JsonNode stagingEnglish = readTestData(STAGING_ENGLISH);

        assertSameStructure(productionArabic, productionEnglish, PRODUCTION_ARABIC, PRODUCTION_ENGLISH, "$");
        assertSameStructure(productionArabic, stagingArabic, PRODUCTION_ARABIC, STAGING_ARABIC, "$");
        assertSameStructure(productionArabic, stagingEnglish, PRODUCTION_ARABIC, STAGING_ENGLISH, "$");
    }

    private static JsonNode readTestData(String fileName) throws IOException {
        return OBJECT_MAPPER.readTree(new File(TESTDATA_DIRECTORY + fileName));
    }

    private static void assertSameStructure(
            JsonNode expected,
            JsonNode actual,
            String expectedFileName,
            String actualFileName,
            String path
    ) {
        Assert.assertEquals(
                actual.getNodeType(),
                expected.getNodeType(),
                String.format("Node type mismatch at %s between %s and %s", path, expectedFileName, actualFileName)
        );

        if (expected.isObject()) {
            TreeSet<String> expectedFields = fieldNamesOf(expected);
            TreeSet<String> actualFields = fieldNamesOf(actual);
            Assert.assertEquals(
                    actualFields,
                    expectedFields,
                    String.format("Object fields mismatch at %s between %s and %s", path, expectedFileName, actualFileName)
            );

            for (String fieldName : expectedFields) {
                assertSameStructure(
                        expected.get(fieldName),
                        actual.get(fieldName),
                        expectedFileName,
                        actualFileName,
                        path + "." + fieldName
                );
            }
            return;
        }

        if (expected.isArray()) {
            Assert.assertEquals(
                    actual.size(),
                    expected.size(),
                    String.format("Array size mismatch at %s between %s and %s", path, expectedFileName, actualFileName)
            );

            for (int index = 0; index < expected.size(); index++) {
                assertSameStructure(
                        expected.get(index),
                        actual.get(index),
                        expectedFileName,
                        actualFileName,
                        path + "[" + index + "]"
                );
            }
        }
    }

    private static TreeSet<String> fieldNamesOf(JsonNode node) {
        TreeSet<String> fields = new TreeSet<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            fields.add(iterator.next().getKey());
        }
        return fields;
    }
}
