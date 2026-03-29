package configs.testRail;

import java.io.File;
import java.lang.reflect.Method;
import java.util.StringJoiner;

public final class TestRailReportingSupport {
    private TestRailReportingSupport() {
        // Utility helper
    }

    public static boolean shouldPublish(boolean reportingEnabled, String testRunId, String testCaseId) {
        return reportingEnabled && hasText(testRunId) && hasText(testCaseId);
    }

    public static String resolveTestCaseId(Method method, String currentCaseId) {
        if (hasText(currentCaseId)) {
            return currentCaseId.trim();
        }

        if (method == null) {
            return null;
        }

        TestRailCaseId annotation = method.getAnnotation(TestRailCaseId.class);
        return annotation != null && hasText(annotation.value()) ? annotation.value().trim() : null;
    }

    public static String buildFailureComment(String testName, Throwable throwable, File screenShot, String apiDetails) {
        StringJoiner comment = new StringJoiner("\n\n");
        comment.add("Test failed: " + testName);

        if (throwable != null && hasText(throwable.getMessage())) {
            comment.add("Error: " + throwable.getMessage().trim());
        }

        if (screenShot != null) {
            comment.add("Screenshot: " + screenShot.getAbsolutePath());
        }

        if (hasText(apiDetails)) {
            comment.add(apiDetails.trim());
        }

        return comment.toString();
    }

    public static String buildSkippedComment(String testName, Throwable throwable) {
        StringJoiner comment = new StringJoiner("\n\n");
        comment.add("Test skipped: " + testName);

        if (throwable != null && hasText(throwable.getMessage())) {
            comment.add("Reason: " + throwable.getMessage().trim());
        }

        return comment.toString();
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
