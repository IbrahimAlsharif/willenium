package configs.testRail;

import java.io.IOException;

@FunctionalInterface
public interface TestRailPublisher {
    void publish(String testRunId, String testCaseId, int status, String comment, String attachmentPath)
            throws IOException, APIException;
}
