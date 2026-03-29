package configs.listeners;

import base.Go;
import base.Setup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import configs.api.ApiContext;
import configs.pipeline.PipelineConfig;
import configs.testRail.APIException;
import configs.testRail.TestRailContext;
import configs.testRail.TestRailConfig;
import configs.testRail.TestRailManager;
import configs.testRail.TestRailPublisher;
import configs.testRail.TestRailReportingSupport;
import configs.testRail.TestRailRunCreator;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static base.Go.testRunId;
import static base.Setup.getUiInitializationBlockerMessage;
import static base.Setup.testCaseId;
public class Listener implements ITestListener, IInvokedMethodListener, IExecutionListener {
    private static final String TESTRAIL_RUN_ID_ATTRIBUTE = "testrail.runId";
    private static final TestRailConfig TEST_RAIL_CONFIG = TestRailConfig.getInstance();
    private ExtentReports extent;
    private final Map<String, ExtentTest> testMap = new HashMap<>();
    private final BooleanSupplier reportingEnabledSupplier;
    private final TestRailPublisher testRailPublisher;
    private final TestRailRunCreator testRailRunCreator;
    private boolean halt = false;

    public Listener() {
        this(
                (testRunId, testCaseId, status, comment, attachmentPath) ->
                        new TestRailManager().setResult(testRunId, testCaseId, status, comment, attachmentPath),
                () -> new TestRailManager().createTestRun(TEST_RAIL_CONFIG.getProjectName(), TEST_RAIL_CONFIG.getProjectId()),
                () -> PipelineConfig.testRailReport
        );
    }

    Listener(TestRailPublisher testRailPublisher) {
        this(
                testRailPublisher,
                () -> new TestRailManager().createTestRun(TEST_RAIL_CONFIG.getProjectName(), TEST_RAIL_CONFIG.getProjectId()),
                () -> PipelineConfig.testRailReport
        );
    }

    Listener(TestRailPublisher testRailPublisher, BooleanSupplier reportingEnabledSupplier) {
        this(
                testRailPublisher,
                () -> new TestRailManager().createTestRun(TEST_RAIL_CONFIG.getProjectName(), TEST_RAIL_CONFIG.getProjectId()),
                reportingEnabledSupplier
        );
    }

    Listener(TestRailPublisher testRailPublisher, TestRailRunCreator testRailRunCreator, BooleanSupplier reportingEnabledSupplier) {
        this.testRailPublisher = testRailPublisher;
        this.testRailRunCreator = testRailRunCreator;
        this.reportingEnabledSupplier = reportingEnabledSupplier;
    }

    @Override
    public void onStart(ITestContext result) {
        System.out.println(" >>>>>>>>>>> Test Started " + result.getName()+ " <<<<<<<<<<<<");
        TestRailContext.clearAll();
        if (extent == null) {
            extent = new ExtentReports();
            extent.attachReporter(new ExtentSparkReporter(ExtentReportSupport.getReportPathForReporter()));
        }
        testMap.put(result.getName(), extent.createTest(result.getName()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("FAILED WITH PERCENTAGE: " + result.getName());
        testMap.get(result.getMethod().getMethodName()).log(Status.FAIL, "Test failed with percentage");

    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED -> " + result.getName());
        ExtentTest methodTest = testMap.get(result.getName());
        if (methodTest == null) {
            ExtentTest suiteTest = testMap.get(result.getTestContext().getName());
            methodTest = suiteTest != null
                    ? suiteTest.createNode(result.getMethod().getMethodName())
                    : extent.createTest(result.getMethod().getMethodName());
            testMap.put(result.getMethod().getMethodName(), methodTest);
        }

        methodTest.log(Status.FAIL, "Test failed");
        methodTest.fail(result.getThrowable());
        attachBrowserContext(methodTest);

        if (ApiContext.hasExchange()) {
            methodTest.info(ApiContext.buildReportDetails());
        }

        File screenShot = null;
        if (Setup.driver != null) {
            try {
                String screenshotBase64 = Go.getShotAsBase64();
                if (screenshotBase64 != null && !screenshotBase64.isBlank()) {
                    methodTest.addScreenCaptureFromBase64String(screenshotBase64, "Screenshot");
                }
                screenShot = Go.getShotAsFile(result.getName());
                File pageSource = Go.getPageSourceAsFile(result.getName());
                methodTest.info("Page source saved to: " + pageSource.getAbsolutePath());
                methodTest.info("Page source snippet:\n" + Go.getPageSourceSnippet());
            } catch (Exception screenshotError) {
                methodTest.log(Status.WARNING, "Screenshot unavailable: " + screenshotError.getMessage());
            }
        }

        publishTestRailResult(
                result,
                TestRailManager.FAILED,
                TestRailReportingSupport.buildFailureComment(
                        result.getName(),
                        result.getThrowable(),
                        screenShot,
                        ApiContext.hasExchange() ? ApiContext.buildReportDetails() : null
                ),
                screenShot != null ? screenShot.getAbsolutePath() : null
        );
        for (String tag : result.getMethod().getGroups()) {
            if (tag.equalsIgnoreCase("haltWhenFail")) {
                halt = true;
            }
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest methodTest = testMap.get(result.getMethod().getMethodName());
        if (methodTest != null) {
            methodTest.log(Status.SKIP, "Test skipped");
            if (result.getThrowable() != null) {
                methodTest.skip(result.getThrowable());
            }
        }

        publishTestRailResult(
                result,
                TestRailManager.Blocked,
                TestRailReportingSupport.buildSkippedComment(result.getName(), result.getThrowable()),
                null
        );
    }

    @Override
    public void onTestStart(ITestResult result) {
        ApiContext.clear();
        TestRailContext.clearCurrentTestCaseId();
        testCaseId = null;
        syncRunIdFromContext(result.getTestContext());
        ExtentTest test = testMap.get(result.getTestContext().getName());
        ExtentTest methodTest = test.createNode(result.getMethod().getMethodName());
        testMap.put(result.getMethod().getMethodName(), methodTest);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }

        String blockerMessage = getUiInitializationBlockerMessage();
        String className = method.getTestMethod().getTestClass().getName();
        boolean isFrameworkLifecycleClass = className.equals("base.Setup") || className.equals("base.TearDownTest");

        if (blockerMessage != null && !isFrameworkLifecycleClass) {
            SkipException exception = new SkipException(blockerMessage);
            testResult.setThrowable(exception);
            testResult.setStatus(ITestResult.SKIP);
            throw exception;
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED -> " + result.getName());
        ExtentTest methodTest = testMap.get(result.getMethod().getMethodName());
        if (methodTest != null) {
            methodTest.log(Status.PASS, "Test passed");
            if (!isTearDownLifecycle(result)) {
                attachBrowserContext(methodTest);
            }
        }
        publishTestRailResult(result, TestRailManager.PASSED, "Test passed", null);
    }

    @Override
    public void onFinish(ITestContext result) {
        extent.flush();
    }

    @Override
    public void onExecutionFinish() {
        if (extent == null) {
            return;
        }

        extent.flush();
        ExtentReportSupport.openOrSuggestReport();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }

        rememberLegacyRunId(testResult.getTestContext());
        if (hasText(testCaseId)) {
            TestRailContext.setCurrentTestCaseId(testCaseId);
        }
    }

    private void attachBrowserContext(ExtentTest methodTest) {
        if (!Setup.hasActiveUiSession()) {
            String blockerMessage = getUiInitializationBlockerMessage();
            if (blockerMessage != null) {
                methodTest.info("UI initialization blocker: " + blockerMessage);
            }
            return;
        }

        try {
            methodTest.info("Current URL: " + Setup.driver.getCurrentUrl());
            methodTest.info("Page title: " + Go.getPageTitle());
            methodTest.info("Focused element: " + Go.describeFocusedElement());
        } catch (Exception exception) {
            methodTest.log(Status.WARNING, "Browser context unavailable: " + exception.getMessage());
        }
    }

    private boolean isTearDownLifecycle(ITestResult result) {
        return result.getMethod().getTestClass().getName().equals("base.TearDownTest");
    }

    private void publishTestRailResult(ITestResult result, int status, String comment, String attachmentPath) {
        if (!reportingEnabledSupplier.getAsBoolean()) {
            return;
        }

        String resolvedTestCaseId = resolveTestCaseId(result);
        if (!hasText(resolvedTestCaseId)) {
            System.out.printf(
                    "Skipping TestRail update because testCaseId is missing. caseId=%s%n",
                    resolvedTestCaseId
            );
            return;
        }

        String resolvedTestRunId = null;
        try {
            resolvedTestRunId = resolveOrCreateTestRunId(result);
            if (!TestRailReportingSupport.shouldPublish(true, resolvedTestRunId, resolvedTestCaseId)) {
                System.out.printf(
                        "Skipping TestRail update because testRunId or testCaseId is missing. runId=%s caseId=%s%n",
                        resolvedTestRunId,
                        resolvedTestCaseId
                );
                return;
            }
            testRailPublisher.publish(resolvedTestRunId, resolvedTestCaseId, status, comment, attachmentPath);
        } catch (IOException | APIException | RuntimeException exception) {
            System.out.printf(
                    "TestRail publish failed for runId=%s caseId=%s status=%s: %s%n",
                    resolvedTestRunId,
                    resolvedTestCaseId,
                    status,
                    exception.getMessage()
            );
        }
    }

    private String resolveTestCaseId(ITestResult result) {
        if (result == null || result.getMethod() == null || result.getMethod().getConstructorOrMethod() == null) {
            return TestRailReportingSupport.resolveTestCaseId(null, TestRailContext.getCurrentTestCaseId());
        }

        java.lang.reflect.Method method = result.getMethod().getConstructorOrMethod().getMethod();
        return TestRailReportingSupport.resolveTestCaseId(method, TestRailContext.getCurrentTestCaseId());
    }

    private String resolveOrCreateTestRunId(ITestResult result) throws IOException, APIException {
        syncRunIdFromContext(result != null ? result.getTestContext() : null);
        String currentRunId = TestRailContext.getCurrentTestRunId();
        if (currentRunId != null && !currentRunId.isBlank()) {
            return currentRunId;
        }

        ITestContext context = result != null ? result.getTestContext() : null;
        rememberLegacyRunId(context);
        currentRunId = TestRailContext.getCurrentTestRunId();
        if (currentRunId != null && !currentRunId.isBlank()) {
            return currentRunId;
        }

        if (context == null) {
            return null;
        }

        String createdRunId = testRailRunCreator.createRun();
        storeRunId(context, createdRunId);
        return createdRunId;
    }

    private void syncRunIdFromContext(ITestContext context) {
        if (context == null) {
            return;
        }

        Object storedRunId = context.getAttribute(TESTRAIL_RUN_ID_ATTRIBUTE);
        if (storedRunId instanceof String runId && hasText(runId)) {
            TestRailContext.setCurrentTestRunId(runId);
        }
    }

    private void rememberLegacyRunId(ITestContext context) {
        if (hasText(TestRailContext.getCurrentTestRunId())) {
            return;
        }

        if (context != null) {
            Object storedRunId = context.getAttribute(TESTRAIL_RUN_ID_ATTRIBUTE);
            if (storedRunId instanceof String runId && hasText(runId)) {
                TestRailContext.setCurrentTestRunId(runId);
                return;
            }
        }

        if (hasText(testRunId)) {
            storeRunId(context, testRunId);
        }
    }

    private void storeRunId(ITestContext context, String runId) {
        if (!hasText(runId)) {
            return;
        }

        String normalizedRunId = runId.trim();
        TestRailContext.setCurrentTestRunId(normalizedRunId);
        testRunId = normalizedRunId;
        if (context != null) {
            context.setAttribute(TESTRAIL_RUN_ID_ATTRIBUTE, normalizedRunId);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
