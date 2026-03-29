package configs.testRail;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

public class TestRailContextTest {

    @AfterMethod
    public void clearContext() {
        TestRailContext.clearAll();
    }

    @Test
    public void verifyCurrentThreadStateCanBeStoredAndCleared() {
        TestRailContext.setCurrentTestRunId("RUN-100");
        TestRailContext.setCurrentTestCaseId("CASE-100");

        Assert.assertEquals(TestRailContext.getCurrentTestRunId(), "RUN-100");
        Assert.assertEquals(TestRailContext.getCurrentTestCaseId(), "CASE-100");

        TestRailContext.clearAll();

        Assert.assertNull(TestRailContext.getCurrentTestRunId(), "Clearing should remove the run ID");
        Assert.assertNull(TestRailContext.getCurrentTestCaseId(), "Clearing should remove the case ID");
    }

    @Test
    public void verifyChildThreadChangesDoNotOverwriteParentThreadState() throws Exception {
        TestRailContext.setCurrentTestRunId("RUN-PARENT");
        TestRailContext.setCurrentTestCaseId("CASE-PARENT");

        AtomicReference<String> childRunId = new AtomicReference<>();
        AtomicReference<String> childCaseId = new AtomicReference<>();

        Thread worker = new Thread(() -> {
            childRunId.set(TestRailContext.getCurrentTestRunId());
            childCaseId.set(TestRailContext.getCurrentTestCaseId());

            TestRailContext.setCurrentTestRunId("RUN-CHILD");
            TestRailContext.setCurrentTestCaseId("CASE-CHILD");
        });

        worker.start();
        worker.join();

        Assert.assertEquals(childRunId.get(), "RUN-PARENT", "Child threads should inherit the parent run ID");
        Assert.assertEquals(childCaseId.get(), "CASE-PARENT", "Child threads should inherit the parent case ID");
        Assert.assertEquals(TestRailContext.getCurrentTestRunId(), "RUN-PARENT", "Child updates must not overwrite the parent run ID");
        Assert.assertEquals(TestRailContext.getCurrentTestCaseId(), "CASE-PARENT", "Child updates must not overwrite the parent case ID");
    }
}
