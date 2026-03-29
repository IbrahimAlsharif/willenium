package configs.pipeline;

public final class RemoteExecutionConfig {
    private static final String DEFAULT_EXECUTION_MODE = "auto";
    private static final String DEFAULT_REMOTE_PLATFORM = "";
    private static final String DEFAULT_REMOTE_PROJECT = "Willenium";
    private static final String DEFAULT_REMOTE_BUILD = "Local fallback";

    private final String executionMode;
    private final String remoteUrl;
    private final String remotePlatform;
    private final String remoteBrowserVersion;
    private final String remoteProvider;
    private final String remoteUsername;
    private final String remoteAccessKey;
    private final String remoteProject;
    private final String remoteBuild;

    private RemoteExecutionConfig() {
        executionMode = readEnv("WILLENIUM_EXECUTION_MODE", DEFAULT_EXECUTION_MODE).toLowerCase();
        remoteUrl = readEnv("WILLENIUM_REMOTE_URL", "");
        remotePlatform = readEnv("WILLENIUM_REMOTE_PLATFORM", DEFAULT_REMOTE_PLATFORM);
        remoteBrowserVersion = readEnv("WILLENIUM_REMOTE_BROWSER_VERSION", "");
        remoteProvider = readEnv("WILLENIUM_REMOTE_PROVIDER", "");
        remoteUsername = readEnv("WILLENIUM_REMOTE_USERNAME", "");
        remoteAccessKey = readEnv("WILLENIUM_REMOTE_ACCESS_KEY", "");
        remoteProject = readEnv("WILLENIUM_REMOTE_PROJECT", DEFAULT_REMOTE_PROJECT);
        remoteBuild = readEnv("WILLENIUM_REMOTE_BUILD", DEFAULT_REMOTE_BUILD);
    }

    public static RemoteExecutionConfig fromEnvironment() {
        return new RemoteExecutionConfig();
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public boolean isLocalOnly() {
        return "local".equals(executionMode);
    }

    public boolean isRemoteOnly() {
        return "remote".equals(executionMode);
    }

    public boolean isAutoMode() {
        return !isLocalOnly() && !isRemoteOnly();
    }

    public boolean isRemoteConfigured() {
        return !remoteUrl.isBlank();
    }

    public boolean isLambdaTest() {
        if (!remoteProvider.isBlank()) {
            return "lambdatest".equalsIgnoreCase(remoteProvider);
        }
        return remoteUrl.contains("lambdatest");
    }

    public boolean hasRemoteCredentials() {
        return !remoteUsername.isBlank() && !remoteAccessKey.isBlank();
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getRemotePlatform() {
        return remotePlatform;
    }

    public String getRemoteBrowserVersion() {
        return remoteBrowserVersion;
    }

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public String getRemoteAccessKey() {
        return remoteAccessKey;
    }

    public String getRemoteProject() {
        return remoteProject;
    }

    public String getRemoteBuild() {
        return remoteBuild;
    }

    public String describeRemoteRequirement() {
        return "Set WILLENIUM_REMOTE_URL and, for LambdaTest, also set WILLENIUM_REMOTE_USERNAME and WILLENIUM_REMOTE_ACCESS_KEY.";
    }

    private static String readEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null ? defaultValue : value.trim();
    }
}
