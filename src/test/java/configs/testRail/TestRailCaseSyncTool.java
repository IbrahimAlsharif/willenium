package configs.testRail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRailCaseSyncTool {

    private static final Pattern SUITE_FILE_PATTERN = Pattern.compile("<suite-file\\s+path=\"([^\"]+)\"");
    private static final Pattern CLASS_PATTERN = Pattern.compile("<class\\s+name=\"([^\"]+)\"");
    private static final String FLOW_FILTER_PROPERTY = "willenium.testrail.flow";
    private static final String ROOT = System.getProperty("user.dir");
    private static final Path ALL_FLOWS = Paths.get(ROOT, "flows", "AllFlows.xml");

    private final TestRailConfig config = new TestRailConfig();
    private final TestRailManager testRailManager = new TestRailManager();
    private final APIClient client = testRailManager.getClient();

    public static void main(String[] args) throws Exception {
        new TestRailCaseSyncTool().sync();
    }

    public void sync() throws Exception {
        config.requireConfigured("TestRail case sync");

        List<ResolvedTestClass> testClasses = resolveFlowTestClasses();
        if (testClasses.isEmpty()) {
            throw new IllegalStateException("No test files were resolved from " + ALL_FLOWS);
        }

        for (ResolvedTestClass testClass : testClasses) {
            syncTestClass(testClass);
        }
    }

    private void syncTestClass(ResolvedTestClass testClass) throws Exception {
        String content = Files.readString(testClass.sourceFile(), StandardCharsets.UTF_8);
        Class<?> javaClass = Class.forName(testClass.className());
        String defaultSectionName = humanize(stripTestSuffix(javaClass.getSimpleName()));
        Map<String, String> methodIds = new LinkedHashMap<>();
        Map<String, String> sectionIds = new LinkedHashMap<>();
        Map<String, Map<String, String>> casesBySectionTitle = new LinkedHashMap<>();

        for (Method method : javaClass.getDeclaredMethods()) {
            if (method.getAnnotation(Test.class) == null) {
                continue;
            }

            TestRailCase testRailCase = method.getAnnotation(TestRailCase.class);
            String currentId = testRailCase == null ? "" : safeTrim(testRailCase.value());
            String title = resolveTitle(method, testRailCase);
            String sectionName = resolveSection(defaultSectionName, testRailCase);
            String sectionId = sectionIds.computeIfAbsent(sectionName, this::ensureSectionUnchecked);
            Map<String, String> existingCasesByTitle = casesBySectionTitle.computeIfAbsent(
                    sectionName,
                    key -> getCasesByTitleUnchecked(sectionId)
            );

            String caseId = syncCase(sectionId, sectionName, title, currentId, existingCasesByTitle);
            methodIds.put(method.getName(), caseId);
        }

        if (methodIds.isEmpty()) {
            System.out.println("SKIPPED " + testClass.sourceFile() + " (no @Test methods found)");
            return;
        }

        String updatedContent = updateAnnotatedCaseIds(content, methodIds);
        if (!updatedContent.equals(content)) {
            Files.writeString(testClass.sourceFile(), updatedContent, StandardCharsets.UTF_8);
            System.out.println("UPDATED IDS -> " + testClass.sourceFile());
        }
    }

    private String syncCase(
            String sectionId,
            String sectionName,
            String title,
            String currentId,
            Map<String, String> existingCasesByTitle
    ) throws IOException, APIException {
        JSONObject existingCase = currentId.isBlank() ? null : tryGetCase(currentId);
        if (existingCase != null) {
            String existingTitle = String.valueOf(existingCase.get("title"));
            String existingSectionId = String.valueOf(existingCase.get("section_id"));

            if (!sectionId.equals(existingSectionId)) {
                String targetCaseId = existingCasesByTitle.get(title);
                if (targetCaseId == null) {
                    targetCaseId = createCase(sectionId, title);
                    existingCasesByTitle.put(title, targetCaseId);
                    System.out.println("REASSIGNED " + sectionName + " -> " + title + " => C" + targetCaseId
                            + " (replaced mismatched C" + currentId + " from section " + existingSectionId + ")");
                } else {
                    System.out.println("RELINKED " + sectionName + " -> " + title + " => C" + targetCaseId
                            + " (replaced mismatched C" + currentId + " from section " + existingSectionId + ")");
                }
                return targetCaseId;
            }

            if (!title.equals(existingTitle)) {
                updateCaseTitle(currentId, title);
                existingCasesByTitle.remove(existingTitle);
                System.out.println("RENAMED " + sectionName + " -> " + existingTitle + " => " + title + " (C" + currentId + ")");
            } else {
                System.out.println("EXISTS  " + sectionName + " -> " + title + " => C" + currentId);
            }

            existingCasesByTitle.put(title, currentId);
            return currentId;
        }

        String caseId = existingCasesByTitle.get(title);
        if (caseId == null) {
            caseId = createCase(sectionId, title);
            existingCasesByTitle.put(title, caseId);
            System.out.println("CREATED " + sectionName + " -> " + title + " => C" + caseId);
            return caseId;
        }

        if (!caseId.equals(currentId)) {
            System.out.println("MATCHED " + sectionName + " -> " + title + " => C" + caseId);
        } else {
            System.out.println("EXISTS  " + sectionName + " -> " + title + " => C" + caseId);
        }
        return caseId;
    }

    private List<ResolvedTestClass> resolveFlowTestClasses() throws IOException {
        LinkedHashMap<String, ResolvedTestClass> files = new LinkedHashMap<>();
        for (Path rootSuite : resolveRequestedRootSuites()) {
            resolveSuiteFile(rootSuite, files);
        }
        return new ArrayList<>(files.values());
    }

    private List<Path> resolveRequestedRootSuites() {
        String configuredFlows = System.getProperty(FLOW_FILTER_PROPERTY, "").trim();
        if (configuredFlows.isBlank()) {
            return List.of(ALL_FLOWS);
        }

        List<Path> requestedSuites = new ArrayList<>();
        for (String configuredFlow : configuredFlows.split(",")) {
            String trimmedFlow = configuredFlow.trim();
            if (trimmedFlow.isEmpty()) {
                continue;
            }

            Path suitePath = resolveSuitePath(ALL_FLOWS, trimmedFlow);
            if (!Files.exists(suitePath)) {
                throw new IllegalStateException(
                        "Configured TestRail flow does not exist: " + trimmedFlow
                                + ". Pass a valid suite XML path through -" + "D" + FLOW_FILTER_PROPERTY
                );
            }
            requestedSuites.add(suitePath);
        }

        if (requestedSuites.isEmpty()) {
            throw new IllegalStateException(
                    "No valid flow paths were provided through -" + "D" + FLOW_FILTER_PROPERTY
            );
        }

        return requestedSuites;
    }

    private void resolveSuiteFile(Path suiteFile, Map<String, ResolvedTestClass> files) throws IOException {
        String suiteContent = Files.readString(suiteFile, StandardCharsets.UTF_8);

        Matcher suiteFileMatcher = SUITE_FILE_PATTERN.matcher(suiteContent);
        while (suiteFileMatcher.find()) {
            Path nestedSuite = resolveSuitePath(suiteFile, suiteFileMatcher.group(1));
            if (Files.exists(nestedSuite)) {
                resolveSuiteFile(nestedSuite, files);
            }
        }

        Matcher classMatcher = CLASS_PATTERN.matcher(suiteContent);
        while (classMatcher.find()) {
            String className = classMatcher.group(1);
            if (className.startsWith("base.")) {
                continue;
            }
            Path testFile = Paths.get(ROOT, "src", "test", "java", className.replace(".", "\\") + ".java");
            if (Files.exists(testFile)) {
                files.putIfAbsent(className, new ResolvedTestClass(className, testFile));
            }
        }
    }

    private String ensureSection(String sectionName) throws IOException, APIException {
        Optional<String> existingSectionId = findSectionId(sectionName);
        if (existingSectionId.isPresent()) {
            return existingSectionId.get();
        }

        JSONObject payload = new JSONObject();
        payload.put("name", sectionName);
        if (config.getSuiteId() != null) {
            payload.put("suite_id", config.getSuiteId());
        }

        JSONObject response = (JSONObject) client.sendPost("add_section/" + config.getProjectId(), payload);
        return String.valueOf(response.get("id"));
    }

    private Optional<String> findSectionId(String sectionName) throws IOException, APIException {
        for (JSONObject section : getSections()) {
            if (sectionName.equalsIgnoreCase(String.valueOf(section.get("name")))) {
                return Optional.of(String.valueOf(section.get("id")));
            }
        }
        return Optional.empty();
    }

    private List<JSONObject> getSections() throws IOException, APIException {
        String endpoint = config.getSuiteId() == null
                ? "get_sections/" + config.getProjectId()
                : "get_sections/" + config.getProjectId() + "&suite_id=" + config.getSuiteId();
        JSONArray response = asArray(client.sendGet(endpoint), "sections");
        List<JSONObject> sections = new ArrayList<>();
        for (Object item : response) {
            sections.add((JSONObject) item);
        }
        return sections;
    }

    private Map<String, String> getCasesByTitle(String sectionId) throws IOException, APIException {
        Map<String, String> cases = new LinkedHashMap<>();
        int offset = 0;

        while (true) {
            String endpoint = config.getSuiteId() == null
                    ? "get_cases/" + config.getProjectId() + "&section_id=" + sectionId + "&limit=250&offset=" + offset
                    : "get_cases/" + config.getProjectId() + "&suite_id=" + config.getSuiteId() + "&section_id=" + sectionId + "&limit=250&offset=" + offset;
            JSONArray response = asArray(client.sendGet(endpoint), "cases");
            if (response.isEmpty()) {
                break;
            }

            for (Object item : response) {
                JSONObject json = (JSONObject) item;
                cases.put(String.valueOf(json.get("title")), String.valueOf(json.get("id")));
            }

            if (response.size() < 250) {
                break;
            }
            offset += response.size();
        }

        return cases;
    }

    private String createCase(String sectionId, String title) throws IOException, APIException {
        JSONObject payload = new JSONObject();
        payload.put("title", title);
        payload.put("priority_id", config.getPriorityId());
        JSONObject response = (JSONObject) client.sendPost("add_case/" + sectionId, payload);
        return String.valueOf(response.get("id"));
    }

    private void updateCaseTitle(String caseId, String title) throws IOException, APIException {
        JSONObject payload = new JSONObject();
        payload.put("title", title);
        client.sendPost("update_case/" + caseId, payload);
    }

    private JSONObject tryGetCase(String caseId) throws IOException, APIException {
        try {
            return (JSONObject) client.sendGet("get_case/" + caseId);
        } catch (APIException exception) {
            if (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("case")) {
                System.out.println("MISSING CASE ID C" + caseId + " -> creating or matching by title instead");
                return null;
            }
            throw exception;
        }
    }

    private String updateAnnotatedCaseIds(String content, Map<String, String> methodIds) {
        for (Map.Entry<String, String> entry : methodIds.entrySet()) {
            String methodName = Pattern.quote(entry.getKey());
            String caseId = entry.getValue();
            content = updateAnnotationForMethod(content, methodName, caseId);
        }
        return content;
    }

    private String updateAnnotationForMethod(String content, String methodName, String caseId) {
        Pattern namedValuePattern = Pattern.compile(
                "(@TestRailCase\\()([^)]*?value\\s*=\\s*\")([^\"]*)(\"[^)]*\\)\\s*@Test(?:\\s*\\([^)]*\\))?\\s*public\\s+void\\s+" + methodName + "\\s*\\()",
                Pattern.MULTILINE
        );
        Matcher namedValueMatcher = namedValuePattern.matcher(content);
        if (namedValueMatcher.find()) {
            return namedValueMatcher.replaceFirst(
                    Matcher.quoteReplacement(namedValueMatcher.group(1) + namedValueMatcher.group(2) + caseId + namedValueMatcher.group(4))
            );
        }

        Pattern shorthandPattern = Pattern.compile(
                "(@TestRailCase\\(\")([^\"]*)(\"\\)\\s*@Test(?:\\s*\\([^)]*\\))?\\s*public\\s+void\\s+" + methodName + "\\s*\\()",
                Pattern.MULTILINE
        );
        Matcher shorthandMatcher = shorthandPattern.matcher(content);
        if (shorthandMatcher.find()) {
            return shorthandMatcher.replaceFirst(
                    Matcher.quoteReplacement(shorthandMatcher.group(1) + caseId + shorthandMatcher.group(3))
            );
        }

        Pattern annotationWithoutValuePattern = Pattern.compile(
                "(@TestRailCase\\()([^)]*?)(\\)\\s*@Test(?:\\s*\\([^)]*\\))?\\s*public\\s+void\\s+" + methodName + "\\s*\\()",
                Pattern.MULTILINE
        );
        Matcher annotationWithoutValueMatcher = annotationWithoutValuePattern.matcher(content);
        if (annotationWithoutValueMatcher.find()) {
            String annotationBody = annotationWithoutValueMatcher.group(2).trim();
            String replacementBody = annotationBody.isEmpty()
                    ? "value = \"" + caseId + "\""
                    : "value = \"" + caseId + "\", " + annotationBody;
            return annotationWithoutValueMatcher.replaceFirst(
                    Matcher.quoteReplacement(annotationWithoutValueMatcher.group(1) + replacementBody + annotationWithoutValueMatcher.group(3))
            );
        }

        String insertPattern = "(@Test(?:\\s*\\([^)]*\\))?\\s*public\\s+void\\s+" + methodName + "\\s*\\()";
        return content.replaceAll(insertPattern, "@TestRailCase(value = \"" + Matcher.quoteReplacement(caseId) + "\")\n    $1");
    }

    private String resolveTitle(Method method, TestRailCase testRailCase) {
        if (testRailCase != null && !safeTrim(testRailCase.title()).isBlank()) {
            return safeTrim(testRailCase.title());
        }
        return humanizeMethodName(method.getName());
    }

    private String resolveSection(String defaultSectionName, TestRailCase testRailCase) {
        if (testRailCase != null && !safeTrim(testRailCase.section()).isBlank()) {
            return safeTrim(testRailCase.section());
        }
        return defaultSectionName;
    }

    private String stripTestSuffix(String value) {
        return value.endsWith("Test") ? value.substring(0, value.length() - 4) : value;
    }

    private String humanizeMethodName(String methodName) {
        String normalized = methodName;
        normalized = normalized.replaceFirst("^TC\\d+_?", "");
        normalized = normalized.replaceFirst("^(verify|should|can|is|has)", "");
        return humanize(normalized);
    }

    private String humanize(String value) {
        String normalized = value.replaceAll("([a-z])([A-Z])", "$1 $2").replace('_', ' ').trim();
        String[] parts = normalized.split("\\s+");
        List<String> words = new ArrayList<>();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (part.equals(part.toUpperCase())) {
                words.add(part);
            } else {
                words.add(part.substring(0, 1).toUpperCase() + part.substring(1));
            }
        }

        return String.join(" ", words).trim();
    }

    private JSONArray asArray(Object response, String key) {
        if (response instanceof JSONArray) {
            return (JSONArray) response;
        }
        if (response instanceof JSONObject jsonObject) {
            Object nested = jsonObject.get(key);
            if (nested instanceof JSONArray) {
                return (JSONArray) nested;
            }
        }
        throw new IllegalStateException("Unexpected TestRail response shape for key '" + key + "': " + response);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private String ensureSectionUnchecked(String sectionName) {
        try {
            return ensureSection(sectionName);
        } catch (IOException | APIException exception) {
            throw new IllegalStateException("Failed to resolve TestRail section '" + sectionName + "'", exception);
        }
    }

    private Map<String, String> getCasesByTitleUnchecked(String sectionId) {
        try {
            return getCasesByTitle(sectionId);
        } catch (IOException | APIException exception) {
            throw new IllegalStateException("Failed to read TestRail cases for section " + sectionId, exception);
        }
    }

    private Path resolveSuitePath(Path currentSuiteFile, String configuredPath) {
        Path candidate = Paths.get(configuredPath);
        if (candidate.isAbsolute()) {
            return candidate.normalize();
        }

        String normalizedPath = configuredPath.replace('\\', '/');
        if (normalizedPath.startsWith("flows/")) {
            return Paths.get(ROOT).resolve(configuredPath).normalize();
        }

        return currentSuiteFile.getParent().resolve(configuredPath).normalize();
    }

    private record ResolvedTestClass(String className, Path sourceFile) {
    }
}
