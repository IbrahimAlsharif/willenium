package configs.listeners;

import configs.testRail.APIException;
import configs.testRail.TestRailCaseId;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.TestResult;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerTest {

    @AfterMethod
    public void clearThreadState() {
        configs.testRail.TestRailContext.clearAll();
        base.Setup.testCaseId = null;
        base.Go.testRunId = null;
    }

    @Test
    public void verifySuccessPublishesResolvedAnnotatedCaseId() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(publisher, () -> true);
        ITestContext context = testContext("listener-success");
        ITestResult result = testResult("annotatedSuccess", context);

        listener.onStart(context);
        context.setAttribute("testrail.runId", "RUN-42");
        listener.onTestStart(result);
        listener.onTestSuccess(result);

        Assert.assertEquals(publisher.events.size(), 1, "Success should publish exactly one TestRail result");
        PublishedEvent event = publisher.events.get(0);
        Assert.assertEquals(event.testRunId, "RUN-42");
        Assert.assertEquals(event.testCaseId, "CASE-SUCCESS");
        Assert.assertEquals(event.status, 1);
    }

    @Test
    public void verifySkippedPublishesBlockedStatusWhenIdsExist() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(publisher, () -> true);
        ITestContext context = testContext("listener-skipped");
        ITestResult result = testResult("annotatedSkipped", context);
        result.setThrowable(new RuntimeException("Setup blocked"));

        listener.onStart(context);
        context.setAttribute("testrail.runId", "RUN-77");
        listener.onTestStart(result);
        listener.onTestSkipped(result);

        Assert.assertEquals(publisher.events.size(), 1, "Skipped tests should publish a blocked result");
        PublishedEvent event = publisher.events.get(0);
        Assert.assertEquals(event.testCaseId, "CASE-SKIP");
        Assert.assertEquals(event.status, 2);
        Assert.assertTrue(event.comment.contains("Setup blocked"), "Skip reason should be included in the TestRail comment");
    }

    @Test
    public void verifyMissingIdsDoNotPublish() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(publisher, () -> true);
        ITestContext context = testContext("listener-missing-ids");
        ITestResult result = testResult("plainMethod", context);

        listener.onStart(context);
        listener.onTestStart(result);
        listener.onTestSuccess(result);

        Assert.assertTrue(publisher.events.isEmpty(), "Missing run or case IDs should skip publishing");
    }

    @Test
    public void verifyLegacyStaticIdsStillPublish() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(publisher, () -> true);
        ITestContext context = testContext("listener-legacy-ids");
        ITestResult result = testResult("plainMethod", context);

        base.Go.testRunId = "RUN-LEGACY";
        base.Setup.testCaseId = "CASE-LEGACY";

        listener.onStart(context);
        listener.onTestStart(result);
        base.Setup.testCaseId = "CASE-LEGACY";
        listener.afterInvocation(invokedMethod(), result);
        listener.onTestSuccess(result);

        Assert.assertEquals(publisher.events.size(), 1, "Legacy static IDs should still be supported");
        PublishedEvent event = publisher.events.get(0);
        Assert.assertEquals(event.testRunId, "RUN-LEGACY");
        Assert.assertEquals(event.testCaseId, "CASE-LEGACY");
    }

    @Test
    public void verifyPublisherFailuresDoNotFailTheListener() throws Exception {
        Listener listener = new Listener((testRunId, testCaseId, status, comment, attachmentPath) -> {
            throw new IOException("TestRail unavailable");
        }, () -> true);
        ITestContext context = testContext("listener-publisher-failure");
        ITestResult result = testResult("annotatedSuccess", context);

        listener.onStart(context);
        context.setAttribute("testrail.runId", "RUN-99");
        listener.onTestStart(result);
        listener.onTestSuccess(result);

        Assert.assertEquals(result.getStatus(), ITestResult.SUCCESS, "Reporting failures should not mutate the original test result");
    }

    @Test
    public void verifyLazyRunCreationPublishesUsingCreatedRunId() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(
                publisher,
                () -> "RUN-LAZY",
                () -> true
        );
        ITestContext context = testContext("listener-lazy-run");
        ITestResult result = testResult("annotatedSuccess", context);

        listener.onStart(context);
        listener.onTestStart(result);
        listener.onTestSuccess(result);

        Assert.assertEquals(publisher.events.size(), 1, "Lazy run creation should still publish the TestRail result");
        PublishedEvent event = publisher.events.get(0);
        Assert.assertEquals(event.testRunId, "RUN-LAZY");
        Assert.assertEquals(context.getAttribute("testrail.runId"), "RUN-LAZY", "Created run IDs should be cached on the TestNG context");
    }

    @Test
    public void verifyRunCreationFailuresDoNotFailTheListener() throws Exception {
        RecordingPublisher publisher = new RecordingPublisher();
        Listener listener = new Listener(
                publisher,
                () -> {
                    throw new IllegalStateException("Invalid TestRail config");
                },
                () -> true
        );
        ITestContext context = testContext("listener-run-creation-failure");
        ITestResult result = testResult("annotatedSuccess", context);

        listener.onStart(context);
        listener.onTestStart(result);
        listener.onTestSuccess(result);

        Assert.assertTrue(publisher.events.isEmpty(), "Run creation failures should skip publishing cleanly");
        Assert.assertEquals(result.getStatus(), ITestResult.SUCCESS, "Run creation failures should not mutate the original test result");
    }

    @Test
    public void verifyRuntimePublishFailuresDoNotFailTheListener() throws Exception {
        Listener listener = new Listener((testRunId, testCaseId, status, comment, attachmentPath) -> {
            throw new IllegalStateException("Invalid TestRail config");
        }, () -> true);
        ITestContext context = testContext("listener-runtime-publisher-failure");
        ITestResult result = testResult("annotatedSuccess", context);

        base.Go.testRunId = "RUN-RUNTIME";
        listener.onStart(context);
        listener.onTestStart(result);
        listener.afterInvocation(invokedMethod(), result);
        listener.onTestSuccess(result);

        Assert.assertEquals(result.getStatus(), ITestResult.SUCCESS, "Runtime publish failures should not mutate the original test result");
    }

    private static ITestContext testContext(String name) {
        Map<String, Object> attributes = new HashMap<>();
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("getName")) {
                return name;
            }
            if (method.getName().equals("setAttribute")) {
                attributes.put((String) args[0], args[1]);
                return null;
            }
            if (method.getName().equals("getAttribute")) {
                return attributes.get(args[0]);
            }
            if (method.getName().equals("removeAttribute")) {
                return attributes.remove(args[0]);
            }
            if (method.getReturnType().equals(boolean.class)) {
                return false;
            }
            if (method.getReturnType().equals(int.class)) {
                return 0;
            }
            if (method.getReturnType().equals(long.class)) {
                return 0L;
            }
            return null;
        };

        return (ITestContext) Proxy.newProxyInstance(
                ListenerTest.class.getClassLoader(),
                new Class[]{ITestContext.class},
                handler
        );
    }

    private static ITestResult testResult(String methodName, ITestContext context) throws NoSuchMethodException {
        Method javaMethod = ListenerFixture.class.getDeclaredMethod(methodName);
        ITestNGMethod testNgMethod = testNgMethod(javaMethod);
        TestResult result = TestResult.newContextAwareTestResult(testNgMethod, context);
        result.setStatus(ITestResult.SUCCESS);
        return result;
    }

    private static org.testng.IInvokedMethod invokedMethod() {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("isTestMethod")) {
                return true;
            }
            if (method.getReturnType().equals(boolean.class)) {
                return false;
            }
            if (method.getReturnType().equals(int.class)) {
                return 0;
            }
            if (method.getReturnType().equals(long.class)) {
                return 0L;
            }
            return null;
        };

        return (org.testng.IInvokedMethod) Proxy.newProxyInstance(
                ListenerTest.class.getClassLoader(),
                new Class[]{org.testng.IInvokedMethod.class},
                handler
        );
    }

    private static ITestNGMethod testNgMethod(Method javaMethod) {
        InvocationHandler handler = (proxy, method, args) -> {
            switch (method.getName()) {
                case "getMethodName":
                    return javaMethod.getName();
                case "getConstructorOrMethod":
                    return new ConstructorOrMethod(javaMethod);
                case "getGroups":
                    return new String[0];
                case "getTestClass":
                    return testClass(javaMethod.getDeclaringClass());
                case "isTest":
                    return true;
                case "getEnabled":
                    return true;
                case "getInvocationCount":
                    return 1;
                case "getSuccessPercentage":
                    return 100;
                case "getPriority":
                    return 0;
                default:
                    Class<?> returnType = method.getReturnType();
                    if (returnType.equals(boolean.class)) {
                        return false;
                    }
                    if (returnType.equals(int.class)) {
                        return 0;
                    }
                    if (returnType.equals(long.class)) {
                        return 0L;
                    }
                    return null;
            }
        };

        return (ITestNGMethod) Proxy.newProxyInstance(
                ListenerTest.class.getClassLoader(),
                new Class[]{ITestNGMethod.class},
                handler
        );
    }

    private static org.testng.ITestClass testClass(Class<?> declaringClass) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("getName")) {
                return declaringClass.getName();
            }
            if (method.getName().equals("getRealClass")) {
                return declaringClass;
            }
            if (method.getReturnType().equals(boolean.class)) {
                return false;
            }
            if (method.getReturnType().equals(int.class)) {
                return 0;
            }
            if (method.getReturnType().equals(long.class)) {
                return 0L;
            }
            return null;
        };

        return (org.testng.ITestClass) Proxy.newProxyInstance(
                ListenerTest.class.getClassLoader(),
                new Class[]{org.testng.ITestClass.class},
                handler
        );
    }

    private static class RecordingPublisher implements configs.testRail.TestRailPublisher {
        private final List<PublishedEvent> events = new ArrayList<>();

        @Override
        public void publish(String testRunId, String testCaseId, int status, String comment, String attachmentPath) throws IOException, APIException {
            events.add(new PublishedEvent(testRunId, testCaseId, status, comment, attachmentPath));
        }
    }

    private record PublishedEvent(String testRunId, String testCaseId, int status, String comment, String attachmentPath) {
    }

    private static class ListenerFixture {
        @TestRailCaseId("CASE-SUCCESS")
        public void annotatedSuccess() {
            // fixture method
        }

        @TestRailCaseId("CASE-SKIP")
        public void annotatedSkipped() {
            // fixture method
        }

        public void plainMethod() {
            // fixture method
        }
    }
}
