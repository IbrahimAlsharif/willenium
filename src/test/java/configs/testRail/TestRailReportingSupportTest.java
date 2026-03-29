package configs.testRail;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestRailReportingSupportTest {

    @Test
    public void verifyResolveTestCaseIdPrefersCurrentThreadValue() throws NoSuchMethodException {
        Method method = AnnotatedFixture.class.getDeclaredMethod("annotatedCase");

        String resolvedCaseId = TestRailReportingSupport.resolveTestCaseId(method, "CURRENT-123");

        Assert.assertEquals(resolvedCaseId, "CURRENT-123", "Current thread case ID should override annotation metadata");
    }

    @Test
    public void verifyResolveTestCaseIdFallsBackToAnnotation() throws NoSuchMethodException {
        Method method = AnnotatedFixture.class.getDeclaredMethod("annotatedCase");

        String resolvedCaseId = TestRailReportingSupport.resolveTestCaseId(method, null);

        Assert.assertEquals(resolvedCaseId, "CASE-101", "Annotated case ID should be used when no current thread value exists");
    }

    @Test
    public void verifyResolveTestCaseIdReturnsNullWhenNoSourceExists() throws NoSuchMethodException {
        Method method = AnnotatedFixture.class.getDeclaredMethod("plainCase");

        String resolvedCaseId = TestRailReportingSupport.resolveTestCaseId(method, null);

        Assert.assertNull(resolvedCaseId, "Missing case metadata should resolve to null");
    }

    @Test
    public void verifyShouldPublishRequiresToggleAndBothIds() {
        Assert.assertTrue(TestRailReportingSupport.shouldPublish(true, "RUN-1", "CASE-1"), "Publishing should be enabled only when both IDs exist");
        Assert.assertFalse(TestRailReportingSupport.shouldPublish(false, "RUN-1", "CASE-1"), "Disabled reporting should never publish");
        Assert.assertFalse(TestRailReportingSupport.shouldPublish(true, null, "CASE-1"), "Missing run IDs should block publishing");
        Assert.assertFalse(TestRailReportingSupport.shouldPublish(true, "RUN-1", ""), "Missing case IDs should block publishing");
    }

    @Test
    public void verifyFailureCommentIncludesAvailableArtifacts() throws IOException {
        Path screenshotPath = Files.createTempFile("testrail-comment-", ".png");
        screenshotPath.toFile().deleteOnExit();

        String comment = TestRailReportingSupport.buildFailureComment(
                "verifyApiFailure",
                new IllegalStateException("Expected status 200"),
                screenshotPath.toFile(),
                "Request: GET /health\nResponse: 500"
        );

        Assert.assertTrue(comment.contains("Test failed: verifyApiFailure"), "Comment should include the test name");
        Assert.assertTrue(comment.contains("Error: Expected status 200"), "Comment should include the failure message");
        Assert.assertTrue(comment.contains("Screenshot: " + screenshotPath.toFile().getAbsolutePath()), "Comment should include the screenshot path");
        Assert.assertTrue(comment.contains("Request: GET /health"), "Comment should include API exchange details when present");
    }

    @Test
    public void verifyFailureCommentOmitsBlankOptionalSections() {
        String comment = TestRailReportingSupport.buildFailureComment(
                "verifySimpleFailure",
                new RuntimeException("Boom"),
                null,
                "   "
        );

        Assert.assertTrue(comment.contains("Test failed: verifySimpleFailure"), "Comment should include the test name");
        Assert.assertTrue(comment.contains("Error: Boom"), "Comment should include the throwable message");
        Assert.assertFalse(comment.contains("Screenshot:"), "Comment should omit screenshot details when no screenshot exists");
    }

    @Test
    public void verifySkippedCommentIncludesReasonWhenPresent() {
        String comment = TestRailReportingSupport.buildSkippedComment(
                "verifySkippedFlow",
                new RuntimeException("Setup was blocked")
        );

        Assert.assertTrue(comment.contains("Test skipped: verifySkippedFlow"), "Comment should include the skipped test name");
        Assert.assertTrue(comment.contains("Reason: Setup was blocked"), "Comment should include the skip reason");
    }

    private static class AnnotatedFixture {
        @TestRailCaseId("CASE-101")
        public void annotatedCase() {
            // fixture method
        }

        public void plainCase() {
            // fixture method
        }
    }
}
