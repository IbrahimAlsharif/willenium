package configs.testRail;

public final class TestRailContext {
    private static final InheritableThreadLocal<String> CURRENT_TEST_CASE_ID = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<String> CURRENT_TEST_RUN_ID = new InheritableThreadLocal<>();

    private TestRailContext() {
        // Static context holder
    }

    public static void setCurrentTestCaseId(String testCaseId) {
        if (hasText(testCaseId)) {
            CURRENT_TEST_CASE_ID.set(testCaseId.trim());
        } else {
            CURRENT_TEST_CASE_ID.remove();
        }
    }

    public static String getCurrentTestCaseId() {
        return CURRENT_TEST_CASE_ID.get();
    }

    public static void clearCurrentTestCaseId() {
        CURRENT_TEST_CASE_ID.remove();
    }

    public static void setCurrentTestRunId(String testRunId) {
        if (hasText(testRunId)) {
            CURRENT_TEST_RUN_ID.set(testRunId.trim());
        } else {
            CURRENT_TEST_RUN_ID.remove();
        }
    }

    public static String getCurrentTestRunId() {
        return CURRENT_TEST_RUN_ID.get();
    }

    public static void clearCurrentTestRunId() {
        CURRENT_TEST_RUN_ID.remove();
    }

    public static void clearAll() {
        clearCurrentTestCaseId();
        clearCurrentTestRunId();
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
