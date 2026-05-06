package checker.engine;

import checker.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.concurrent.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;


public class CheckEngine {
    private final CourseConfig config;
    private final Path workDir;
    private final Map<Path, Set<String>> gradleTasksCache = new HashMap<>();

    public CheckEngine(CourseConfig config) {
        this.config = config;
        this.workDir = Path.of(config.getSettings().getWorkDir());
    }

    public CheckResults run() throws Exception {
        Files.createDirectories(workDir);
        CheckResults results = new CheckResults();
        Set<String> analyzedStudents = new HashSet<>();

        for (CheckEntry check : config.getChecks()) {
            GroupConfig group = config.findGroup(check.getGroupName());
            TaskConfig task = config.findTask(check.getTaskId());
            if (group == null || task == null) {
                continue;
            }

            for (StudentConfig student : group.getStudents()) {
                System.err.println("[" + student.getGithub() + "] Checking task " + task.getId());
                StudentTaskResult result = checkStudentTask(student, task);
                results.putResult(check.getGroupName(), task.getId(), student.getGithub(), result);

                if (!analyzedStudents.contains(student.getGithub())) {
                    analyzedStudents.add(student.getGithub());
                    StudentActivity act = analyzeActivity(student);
                    results.putActivity(student.getGithub(), act);
                }
            }
        }
        return results;
    }

    private StudentTaskResult checkStudentTask(StudentConfig student, TaskConfig task) {
        StudentTaskResult result = new StudentTaskResult();
        try {
            Path repoPath = cloneOrUpdateRepo(student);
            if (repoPath == null) {
                result.setErrorMessage("Failed to clone repository");
                return result;
            }

            Path taskDir = repoPath.resolve("Task_" + task.getId());
            if (!Files.exists(taskDir)) {
                result.setErrorMessage("Task directory not found: Task_" + task.getId());
                return result;
            }

            result.setSubmissionDate(lastTaskCommitDate(repoPath, "Task_" + task.getId()));

            String compileError = runGradleTask(taskDir, "compileJava");
            result.setBuildSuccess(compileError == null);
            if (compileError != null) {
                result.setErrorMessage(compileError);
                return result;
            }

            String docsError = runGradleTask(taskDir, "javadoc");
            result.setDocsSuccess(docsError == null);
            if (docsError != null) {
                result.setErrorMessage(docsError);
                return result;
            }

            String styleError = runGradleTask(taskDir, "checkstyleMain");
            result.setStyleSuccess(styleError == null);
            if (styleError != null) {
                result.setErrorMessage(styleError);
                return result;
            }

            deleteDirectory(taskDir.resolve("build/test-results"));
            String testError = runGradleTask(taskDir, "test");
            if (testError != null) {
                result.setErrorMessage(testError);
            }
            result.setTestCounts(parseTestResults(taskDir));

            int bonus = config.getSettings().getBonusPoints(student.getGithub(), task.getId());
            result.setBonusScore(bonus);

            TestCounts tc = result.getTestCounts();
            if (tc.total() > 0 && tc.passed() > 0 && tc.failed() == 0) {
                result.setBaseScore(scoreWithDeadlines(task, result.getSubmissionDate()));
            }
        } catch (Exception e) {
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    private StudentActivity analyzeActivity(StudentConfig student) {
        Settings settings = config.getSettings();
        String startStr = settings.getCourseStartDate();
        String endStr   = settings.getCourseEndDate();
        if (startStr == null || endStr == null || startStr.isBlank() || endStr.isBlank()) {
            return StudentActivity.UNKNOWN;
        }
        LocalDate courseStart;
        LocalDate courseEnd;
        try {
            courseStart = LocalDate.parse(startStr);
            courseEnd   = LocalDate.parse(endStr);
        } catch (Exception e) {
            return StudentActivity.UNKNOWN;
        }

        Path repoPath = workDir.resolve(student.getGithub());
        if (!Files.exists(repoPath.resolve(".git"))) {
            return StudentActivity.UNKNOWN;
        }

        List<String> commitDates = new ArrayList<>();
        try {
            ProcessResult pr = runCommand(repoPath, 30,
                "git", "log", "--format=%ad", "--date=short");
            if (pr.exitCode() == 0) {
                for (String line : pr.output().split("\n")) {
                    if (!line.isBlank()) {
                        commitDates.add(line.trim());
                    }
                }
            }
        } catch (Exception e) {
            return StudentActivity.UNKNOWN;
        }

        Set<String> activeWeekKeys = new HashSet<>();
        for (String dateStr : commitDates) {
            try {
                LocalDate date = LocalDate.parse(dateStr);
                if (!date.isBefore(courseStart) && !date.isAfter(courseEnd)) {
                    int year = date.get(IsoFields.WEEK_BASED_YEAR);
                    int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    activeWeekKeys.add(year + "-W" + week);
                }
            } catch (Exception ignored) {}
        }

        Set<String> totalWeekKeys = new HashSet<>();
        LocalDate d = courseStart;
        while (!d.isAfter(courseEnd)) {
            int year = d.get(IsoFields.WEEK_BASED_YEAR);
            int week = d.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            totalWeekKeys.add(year + "-W" + week);
            d = d.plusWeeks(1);
        }

        int activeWeeks = activeWeekKeys.size();
        int totalWeeks  = totalWeekKeys.size();
        int maxBonus    = settings.getMaxActivityBonus();
        int activityBonus = totalWeeks > 0 ? (maxBonus * activeWeeks / totalWeeks) : 0;

        return new StudentActivity(activeWeeks, totalWeeks, activityBonus);
    }

    private Path cloneOrUpdateRepo(StudentConfig student) throws IOException, InterruptedException {
        Path repoPath = workDir.resolve(student.getGithub());
        if (Files.exists(repoPath.resolve(".git"))) {
            checkoutMainBranch(repoPath);
            runCommand(repoPath, 60, "git", "pull");
        } else {
            if (student.getRepoUrl() == null || student.getRepoUrl().isBlank()) {
                return null;
            }
            ProcessResult pr = runCommand(workDir, 120, "git", "clone", student.getRepoUrl(), student.getGithub());
            if (pr.exitCode() != 0) {
                return null;
            }
            checkoutMainBranch(repoPath);
        }
        makeGradlewExecutable(repoPath);
        return repoPath;
    }

    private void makeGradlewExecutable(Path repoPath) {
        try (var stream = Files.walk(repoPath)) {
            stream.filter(p -> p.getFileName().toString().equals("gradlew"))
                  .forEach(p -> p.toFile().setExecutable(true));
        } catch (IOException e) {
            System.err.println("Warning: could not chmod gradlew: " + e.getMessage());
        }
    }

    private void checkoutMainBranch(Path repoPath) throws IOException, InterruptedException {
        if (runCommand(repoPath, 20, "git", "rev-parse", "--verify", "main").exitCode() == 0) {
            runCommand(repoPath, 30, "git", "checkout", "main");
        } else if (runCommand(repoPath, 20, "git", "rev-parse", "--verify", "master").exitCode() == 0) {
            runCommand(repoPath, 30, "git", "checkout", "master");
        }
    }

    private String runGradleTask(Path taskDir, String taskName) throws IOException, InterruptedException {
        if (!gradleTaskExists(taskDir, taskName)) {
            return "Gradle task not found: " + taskName;
        }
        ProcessResult pr = runCommand(taskDir, config.getSettings().getTestTimeoutSeconds(),
            gradleCmd(taskDir), taskName);
        if (pr.exitCode() == 0) {
            return null;
        }
        return extractBuildError(pr.output());
    }

    private boolean gradleTaskExists(Path taskDir, String taskName) throws IOException, InterruptedException {
        Set<String> tasks = gradleTasksCache.get(taskDir);
        if (tasks == null) {
            ProcessResult pr = runCommand(taskDir, config.getSettings().getTestTimeoutSeconds(),
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
            gradleTasksCache.put(taskDir, tasks);
        }
        return tasks.contains(taskName);
    }

    private String lastTaskCommitDate(Path repoPath, String taskPath) {
        try {
            ProcessResult pr = runCommand(repoPath, 30,
                "git", "log", "-1", "--format=%ad", "--date=short", "--", taskPath);
            return pr.exitCode() == 0 ? pr.output().strip() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private int scoreWithDeadlines(TaskConfig task, String submissionDate) {
        if (submissionDate == null || submissionDate.isBlank()) {
            return task.getMaxScore();
        }
        try {
            LocalDate submitted = LocalDate.parse(submissionDate);
            if (!task.getHardDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getHardDeadline()))) {
                return 0;
            }
            if (!task.getSoftDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getSoftDeadline()))) {
                return Math.max(1, task.getMaxScore() / 2);
            }
        } catch (Exception ignored) { }
        return task.getMaxScore();
    }

    private void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }
        try (var stream = Files.walk(dir)) {
            for (Path path : stream.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private TestCounts parseTestResults(Path taskDir) {
        Path testResultsDir = taskDir.resolve("build/test-results");
        if (!Files.exists(testResultsDir)) {
            return TestCounts.ZERO;
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try (var stream = Files.walk(testResultsDir)) {
                List<Path> xmlFiles = stream.filter(p -> p.toString().endsWith(".xml")).toList();
                for (Path xmlFile : xmlFiles) {
                    try {
                        var doc = builder.parse(xmlFile.toFile());
                        var suites = doc.getElementsByTagName("testsuite");
                        for (int i = 0; i < suites.getLength(); i++) {
                            var suite = (Element) suites.item(i);
                            int tests = parseInt(suite.getAttribute("tests"));
                            int fail  = parseInt(suite.getAttribute("failures"))
                                      + parseInt(suite.getAttribute("errors"));
                            int skip  = parseInt(suite.getAttribute("skipped"));
                            passed  += Math.max(0, tests - fail - skip);
                            failed  += fail;
                            skipped += skip;
                        }
                    } catch (Exception ignored) { }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse test results: " + e.getMessage());
        }
        return new TestCounts(passed, failed, skipped);
    }

    private String resolveJavaHome() {
        String configured = config.getSettings().getJavaHome();
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        String[] candidates = {
            "/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home",
            "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home",
            "/usr/lib/jvm/java-21-openjdk-amd64",
            "/usr/lib/jvm/java-17-openjdk-amd64",
        };
        for (String c : candidates) {
            if (Files.isDirectory(Path.of(c))) {
                return c;
            }
        }
        return null;
    }

    private ProcessResult runCommand(Path dir, int timeoutSeconds, String... command)
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

    private String readOutput(Future<String> outputFuture) {
        try {
            return outputFuture.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            return "";
        }
    }

    private String gradleCmd(Path dir) {
        boolean win = System.getProperty("os.name").toLowerCase().contains("windows");
        return Files.exists(dir.resolve("gradlew")) ? (win ? "gradlew.bat" : "./gradlew") : "gradle";
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

    private static int parseInt(String s) {
        try { 
            return Integer.parseInt(s);
        } catch (NumberFormatException e) { 
            return 0;
        }
    }

    private record ProcessResult(int exitCode, String output, boolean completed) {}
}
