package configs.listeners;

import configs.pipeline.PipelineConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

final class ExtentReportSupport {
    private static final Path REPORT_PATH = Paths.get("extent-reports", "extent-report.html")
            .toAbsolutePath()
            .normalize();
    private static final AtomicBoolean REPORT_NOTICE_SENT = new AtomicBoolean(false);

    private ExtentReportSupport() {
    }

    static String getReportPathForReporter() {
        return REPORT_PATH.toString();
    }

    static void openOrSuggestReport() {
        if (!REPORT_NOTICE_SENT.compareAndSet(false, true)) {
            return;
        }

        if (!Files.exists(REPORT_PATH)) {
            System.out.println("Extent report was expected at: " + REPORT_PATH);
            return;
        }

        System.out.println("Extent report generated at: " + REPORT_PATH);

        if (PipelineConfig.autoOpenExtentReport && tryLaunchReportViewer()) {
            System.out.println("Opened the Extent report in your default browser.");
            return;
        }

        System.out.println("Open the Extent report with: " + buildOpenCommandSuggestion());
    }

    private static boolean tryLaunchReportViewer() {
        List<String> command = buildOpenCommand();
        if (command.isEmpty()) {
            return false;
        }

        try {
            new ProcessBuilder(command).start();
            return true;
        } catch (IOException launchError) {
            System.out.println("Unable to auto-open the Extent report: " + launchError.getMessage());
            return false;
        }
    }

    private static String buildOpenCommandSuggestion() {
        String quotedPath = "\"" + REPORT_PATH + "\"";
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);

        if (os.contains("mac")) {
            return "open " + quotedPath;
        }

        if (os.contains("win")) {
            return "start \"\" " + quotedPath;
        }

        return "xdg-open " + quotedPath;
    }

    private static List<String> buildOpenCommand() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        List<String> command = new ArrayList<>();

        if (os.contains("mac")) {
            command.add("open");
            command.add(REPORT_PATH.toString());
            return command;
        }

        if (os.contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add("start");
            command.add("");
            command.add(REPORT_PATH.toString());
            return command;
        }

        command.add("xdg-open");
        command.add(REPORT_PATH.toString());
        return command;
    }
}
