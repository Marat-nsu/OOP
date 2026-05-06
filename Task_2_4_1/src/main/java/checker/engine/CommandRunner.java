package checker.engine;

import checker.model.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CommandRunner {
    private final Settings settings;

    CommandRunner(Settings settings) {
        this.settings = settings;
    }

    ProcessResult run(Path dir, int timeoutSeconds, String... command)
            throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir.toFile());
        pb.redirectErrorStream(true);
        pb.environment().put("GIT_TERMINAL_PROMPT", "0");

        String javaHome = resolveJavaHome();
        if (javaHome != null) {
            Map<String, String> env = pb.environment();
            env.put("JAVA_HOME", javaHome);
            String binDir = javaHome + File.separator + "bin";
            env.put("PATH", binDir + File.pathSeparator + env.getOrDefault("PATH", ""));
        }

        Process process = pb.start();
        ExecutorService outputReader = Executors.newSingleThreadExecutor();
        Future<String> outputFuture = outputReader.submit(() -> {
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            return output.toString();
        });

        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            String output = readOutput(outputFuture);
            outputReader.shutdownNow();
            return new ProcessResult(-1, output, false);
        }
        String output = readOutput(outputFuture);
        outputReader.shutdownNow();
        return new ProcessResult(process.exitValue(), output, true);
    }

    private String resolveJavaHome() {
        String configured = settings.getJavaHome();
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        String[] candidates = {
            "/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home",
            "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home",
            "/usr/lib/jvm/java-21-openjdk-amd64",
            "/usr/lib/jvm/java-17-openjdk-amd64",
        };
        for (String candidate : candidates) {
            if (Files.isDirectory(Path.of(candidate))) {
                return candidate;
            }
        }
        return null;
    }

    private String readOutput(Future<String> outputFuture) {
        try {
            return outputFuture.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            return "";
        }
    }
}
