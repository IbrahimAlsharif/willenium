package configs.testRail;

import java.io.IOException;

@FunctionalInterface
public interface TestRailRunCreator {
    String createRun() throws IOException, APIException;
}
