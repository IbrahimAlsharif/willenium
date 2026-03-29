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
import configs.testRail.TestRailManager;
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

import static base.Go.testRunId;
import static base.Setup.getUiInitializationBlockerMessage;
import static base.Setup.testCaseId;
import static base.Setup.testRail;

public class Listener implements ITestListener, IInvokedMethodListener, IExecutionListener {
    private ExtentReports extent;
    private final Map<String, ExtentTest> testMap = new HashMap<>();
    private boolean halt = false;

    @Override
    public void onStart(ITestContext result) {
        System.out.println(" >>>>>>>>>>> Test Started " + result.getName()+ " <<<<<<<<<<<<");
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

        if (PipelineConfig.testRailReport && screenShot != null) {
            try {
                testRail.setResult(testRunId, testCaseId, TestRailManager.FAILED, screenShot.getAbsolutePath());
            } catch (IOException | APIException e) {
                throw new RuntimeException(e);
            }
        }
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
    }

    @Override
    public void onTestStart(ITestResult result) {
        ApiContext.clear();
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
        if (PipelineConfig.testRailReport) {
            try {
                testRail.setResult(testRunId, testCaseId, TestRailManager.PASSED, null);
            } catch (IOException | APIException e) {
                throw new RuntimeException(e);
            }
        }
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
        // No action needed after invocation
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

}
