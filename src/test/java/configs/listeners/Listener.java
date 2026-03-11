package configs.listeners;

import base.Go;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import configs.pipeline.PipelineConfig;
import configs.testRail.APIException;
import configs.testRail.TestRailManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static base.Go.testRunId;
import static base.Setup.testCaseId;
import static base.Setup.testRail;

public class Listener implements ITestListener {
    private ExtentReports extent;
    private final Map<String, ExtentTest> testMap = new HashMap<>();
    private boolean halt = false;

    @Override
    public void onStart(ITestContext result) {
        if (halt) {
            System.exit(1);
        }
        System.out.println(" >>>>>>>>>>> Test Started " + result.getName()+ " <<<<<<<<<<<<");
        if (extent == null) {
            extent = new ExtentReports();
            extent.attachReporter(new ExtentSparkReporter("extent-reports/extent-report.html"));
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
        methodTest.log(Status.FAIL, "Test failed");
        methodTest.fail(result.getThrowable());
        methodTest.addScreenCaptureFromBase64String(Go.getShotAsBase64(), "Screenshot");
        File screenShot = Go.getShotAsFile(result.getName());

        if (PipelineConfig.testRailReport) {
            try {
                testRail.setResult(testRunId, testCaseId, TestRailManager.FAILED, screenShot.getAbsolutePath());
            } catch (IOException | APIException e) {
                throw new RuntimeException(e);
            }
        }
        if (halt) {
            System.exit(1);
        }
        for (String tag : result.getMethod().getGroups()) {
            if (tag.equalsIgnoreCase("haltWhenFail")) {
                halt = true;
            }
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testMap.get(result.getMethod().getMethodName()).log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = testMap.get(result.getTestContext().getName());
        ExtentTest methodTest = test.createNode(result.getMethod().getMethodName());
        testMap.put(result.getMethod().getMethodName(), methodTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED -> " + result.getName());
        testMap.get(result.getMethod().getMethodName()).log(Status.PASS, "Test passed");
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

}
