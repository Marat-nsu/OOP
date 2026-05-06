package checker.engine;

import checker.model.Settings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GradleTaskRunner {
    private final Settings settings;
    private final CommandRunner commands;
    private final Map<Path, Set<String>> tasksCache = new HashMap<>();

    GradleTaskRunner(Settings settings, CommandRunner commands) {
        this.settings = settings;
        this.commands = commands;
    }

    String runTask(Path taskDir, String taskName) throws IOException, InterruptedException {
        if (!taskExists(taskDir, taskName)) {
            return "Gradle task not found: " + taskName;
        }
        ProcessResult pr = commands.run(taskDir, settings.getTestTimeoutSeconds(),
            gradleCmd(taskDir), taskName);
        if (pr.exitCode() == 0) {
            return null;
        }
        return extractBuildError(pr.output());
    }

    void deleteTestResults(Path taskDir) throws IOException {
        Path dir = taskDir.resolve("build/test-results");
        if (!Files.exists(dir)) {
            return;
        }
        try (var stream = Files.walk(dir)) {
            for (Path path : stream.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private boolean taskExists(Path taskDir, String taskName) 
        throws IOException, InterruptedException {
        Set<String> tasks = tasksCache.get(taskDir);
        if (tasks == null) {
            ProcessResult pr = commands.run(taskDir, settings.getTestTimeoutSeconds(),
                gradleCmd(taskDir), "tasks", "--all");
            tasks = new HashSet<>();
            if (pr.exitCode() == 0) {
                for (String line : pr.output().split("\n")) {
                    String trimmed = line.stripLeading();
                    int separator = trimmed.indexOf(' ');
                    String name = separator >= 0 ? trimmed.substring(0, separator) : trimmed;
                    if (!name.isBlank()) {
                        tasks.add(name);
                    }
                }
            }
            tasksCache.put(taskDir, tasks);
        }
        return tasks.contains(taskName);
    }

    private String gradleCmd(Path dir) {
        boolean win = System.getProperty("os.name").toLowerCase().contains("windows");
        return Files.exists(dir.resolve("gradlew")) 
            ? (win ? "gradlew.bat" : "./gradlew") : "gradle";
    }

    private static String extractBuildError(String output) {
        if (output == null || output.isBlank()) {
            return "(no output)";
        }
        int start = output.indexOf("* What went wrong:");
        if (start >= 0) {
            int end = output.indexOf("* Try:", start);
            return (end > start ? output.substring(start, end) : output.substring(start)).strip();
        }
        String[] lines = output.split("\n");
        int from = Math.max(0, lines.length - 10);
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < lines.length; i++) {
            sb.append(lines[i]).append("\n");
        }
        return sb.toString().strip();
    }
}
