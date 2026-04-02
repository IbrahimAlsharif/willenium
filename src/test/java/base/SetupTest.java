package base;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SetupTest {

    @Test
    public void verifyUnsupportedLocalBrowserFailsFastInsteadOfFallingBackToFirefox() {
        Setup setup = new Setup();

        IllegalArgumentException exception = Assert.expectThrows(
                IllegalArgumentException.class,
                () -> setup.setUpLocalDriver("english", "production", "edge")
        );

        Assert.assertEquals(
                exception.getMessage(),
                "Unsupported local browser: edge",
                "Unknown browser values should fail fast so the suite does not run on the wrong browser"
        );
    }

    @Test
    public void verifySafariUsesTheManagedLocalDriverPath() throws Exception {
        Setup setup = new Setup();
        Method resolver = Setup.class.getDeclaredMethod("getLocalDriverRequest", String.class);
        resolver.setAccessible(true);

        Object localDriverRequest = resolver.invoke(setup, "safari");
        Method browserNameAccessor = localDriverRequest.getClass().getDeclaredMethod("browserName");
        Method localDriverFactoryAccessor = localDriverRequest.getClass().getDeclaredMethod("localDriverFactory");
        browserNameAccessor.setAccessible(true);
        localDriverFactoryAccessor.setAccessible(true);

        Assert.assertEquals(
                browserNameAccessor.invoke(localDriverRequest),
                "Safari",
                "Safari should resolve through the same managed local-driver request flow as the other supported browsers"
        );
        Assert.assertNotNull(
                localDriverFactoryAccessor.invoke(localDriverRequest),
                "Safari should contribute a managed driver factory instead of bypassing the shared startup path"
        );
    }

    @Test
    public void verifyFrameworkSetupMethodsAreNotPublishedAsTestRailCoverage() throws Exception {
        Assert.assertNull(
                Setup.class.getMethod("setUpLocalDriver", String.class, String.class, String.class)
                        .getAnnotation(configs.testRail.TestRailCase.class),
                "Local driver bootstrap should stay framework-internal instead of appearing as product coverage"
        );
        Assert.assertNull(
                Setup.class.getMethod("openWebsite", String.class)
                        .getAnnotation(configs.testRail.TestRailCase.class),
                "Opening the website is setup plumbing and should not be reported as a business test case"
        );
        Assert.assertNull(
                Setup.class.getMethod("setUpRemoteDriver", String.class, String.class, String.class)
                        .getAnnotation(configs.testRail.TestRailCase.class),
                "Remote driver bootstrap should stay framework-internal instead of appearing as product coverage"
        );
    }

    @Test
    public void verifyPostDriverInitializationRunsOnlyAfterLocalDriverStarts() throws Exception {
        Setup setup = new Setup();
        Class<?> localDriverFactoryType = Class.forName("base.Setup$LocalDriverFactory");
        Method initializer = Setup.class.getDeclaredMethod(
                "startManagedLocalDriver",
                localDriverFactoryType,
                Runnable.class,
                String.class
        );
        initializer.setAccessible(true);

        AtomicInteger successHookCalls = new AtomicInteger();
        Object successfulFactory = Proxy.newProxyInstance(
                localDriverFactoryType.getClassLoader(),
                new Class<?>[]{localDriverFactoryType},
                (proxy, method, args) -> {
                    if ("create".equals(method.getName())) {
                        return Proxy.newProxyInstance(
                                org.openqa.selenium.WebDriver.class.getClassLoader(),
                                new Class<?>[]{org.openqa.selenium.WebDriver.class},
                                (driverProxy, driverMethod, driverArgs) -> switch (driverMethod.getName()) {
                                    case "toString" -> "stub-web-driver";
                                    default -> null;
                                }
                        );
                    }
                    return null;
                }
        );

        initializer.invoke(setup, successfulFactory, (Runnable) successHookCalls::incrementAndGet, "Chrome");
        Assert.assertEquals(
                successHookCalls.get(),
                1,
                "Framework post-startup actions should run once after the driver is actually created"
        );

        AtomicBoolean failureHookTriggered = new AtomicBoolean(false);
        Object failingFactory = Proxy.newProxyInstance(
                localDriverFactoryType.getClassLoader(),
                new Class<?>[]{localDriverFactoryType},
                (proxy, method, args) -> {
                    if ("create".equals(method.getName())) {
                        throw new RuntimeException("driver creation failed");
                    }
                    return null;
                }
        );

        Throwable thrown = Assert.expectThrows(
                Throwable.class,
                () -> initializer.invoke(setup, failingFactory, (Runnable) () -> failureHookTriggered.set(true), "Chrome")
        );
        Throwable rootCause = thrown.getCause() != null ? thrown.getCause() : thrown;

        Assert.assertTrue(
                rootCause instanceof IllegalStateException && rootCause.getMessage().contains("driver creation failed"),
                "Local driver failures should still surface the startup reason"
        );
        Assert.assertFalse(
                failureHookTriggered.get(),
                "Framework post-startup actions must not run when driver creation fails"
        );
    }
}
