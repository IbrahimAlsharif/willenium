package configs.pipeline;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class RemoteExecutionConfigTest {

    @Test
    public void verifyDefaultConfigurationUsesPortableAutoMode() {
        RemoteExecutionConfig config = RemoteExecutionConfig.fromMap(Map.of());

        Assert.assertEquals(config.getExecutionMode(), "auto", "Default execution mode should be auto");
        Assert.assertTrue(config.isAutoMode(), "Default configuration should use auto mode");
        Assert.assertFalse(config.isRemoteConfigured(), "Remote should be disabled by default");
        Assert.assertEquals(config.getRemotePlatform(), "", "Generic remote fallback should not force a platform");
    }

    @Test
    public void verifyExplicitRemoteOverridesAreLoaded() {
        Map<String, String> env = new HashMap<>();
        env.put("WILLENIUM_EXECUTION_MODE", "remote");
        env.put("WILLENIUM_REMOTE_URL", "https://grid.example.test/wd/hub");
        env.put("WILLENIUM_REMOTE_PLATFORM", "macOS 14");
        env.put("WILLENIUM_REMOTE_BROWSER_VERSION", "131");
        env.put("WILLENIUM_REMOTE_PROJECT", "Framework");
        env.put("WILLENIUM_REMOTE_BUILD", "Build 42");

        RemoteExecutionConfig config = RemoteExecutionConfig.fromMap(env);

        Assert.assertTrue(config.isRemoteOnly(), "Execution mode should switch to remote");
        Assert.assertTrue(config.isRemoteConfigured(), "Remote URL should enable remote execution");
        Assert.assertEquals(config.getRemoteUrl(), "https://grid.example.test/wd/hub");
        Assert.assertEquals(config.getRemotePlatform(), "macOS 14");
        Assert.assertEquals(config.getRemoteBrowserVersion(), "131");
        Assert.assertEquals(config.getRemoteProject(), "Framework");
        Assert.assertEquals(config.getRemoteBuild(), "Build 42");
        Assert.assertFalse(config.isLambdaTest(), "Generic remote grids should not be treated as LambdaTest");
    }

    @Test
    public void verifyLambdaTestDetectionSupportsProviderAndUrl() {
        RemoteExecutionConfig byProvider = RemoteExecutionConfig.fromMap(Map.of(
                "WILLENIUM_REMOTE_URL", "https://grid.example.test/wd/hub",
                "WILLENIUM_REMOTE_PROVIDER", "lambdatest",
                "WILLENIUM_REMOTE_USERNAME", "user",
                "WILLENIUM_REMOTE_ACCESS_KEY", "key"
        ));
        RemoteExecutionConfig byUrl = RemoteExecutionConfig.fromMap(Map.of(
                "WILLENIUM_REMOTE_URL", "https://hub.lambdatest.com/wd/hub",
                "WILLENIUM_REMOTE_USERNAME", "user",
                "WILLENIUM_REMOTE_ACCESS_KEY", "key"
        ));

        Assert.assertTrue(byProvider.isLambdaTest(), "Provider override should mark LambdaTest grids");
        Assert.assertTrue(byProvider.hasRemoteCredentials(), "Explicit LambdaTest credentials should be detected");
        Assert.assertTrue(byUrl.isLambdaTest(), "LambdaTest URLs should be auto-detected");
        Assert.assertTrue(byUrl.hasRemoteCredentials(), "URL-based LambdaTest config should still require credentials");
    }
}
