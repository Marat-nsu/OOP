package checker.engine;

import checker.logging.CheckerLogger;
import checker.model.CheckEntry;
import checker.model.CourseConfig;
import checker.model.GroupConfig;
import checker.model.StudentConfig;
import checker.model.TaskConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CheckEngine {
    private final CourseConfig config;
    private final Path workDir;
    private final CheckerLogger log;
    private final RepositoryManager repositories;
    private final GradleTaskRunner gradle;
    private final StyleChecker styleChecker = new StyleChecker();
    private final TestResultParser testResults;
    private final ActivityAnalyzer activityAnalyzer;
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

    public CheckEngine(CourseConfig config) {
        this(config, CheckerLogger.stderr());
    }

    CheckEngine(CourseConfig config, CheckerLogger log) {
        this.config = config;
        this.workDir = Path.of(config.getSettings().getWorkDir());
        this.log = log;

        CommandRunner commands = new CommandRunner(config.getSettings());
        this.repositories = new RepositoryManager(workDir, commands, log);
        this.gradle = new GradleTaskRunner(config.getSettings(), commands);
        this.testResults = new TestResultParser(log);
        this.activityAnalyzer = new ActivityAnalyzer(config.getSettings(), workDir, commands);
    }

    public CheckResults run() throws Exception {
        Files.createDirectories(workDir);
        CheckResults results = new CheckResults();
        Map<String, RepositoryCheckout> checkouts = repositories.checkoutAll(
            checkedStudents().values(),
            config.getSettings().getRepositoryDownloadParallelism()
        );
        Set<String> analyzedStudents = new HashSet<>();

        for (CheckEntry check : config.getChecks()) {
            GroupConfig group = config.findGroup(check.getGroupName());
            TaskConfig task = config.findTask(check.getTaskId());
            if (group == null || task == null) {
                continue;
            }

            for (StudentConfig student : group.getStudents()) {
                log.info("[" + student.getGithub() + "] Checking task " + task.getId());
                StudentTaskResult result = checkStudentTask(
                    student,
                    task,
                    checkouts.get(student.getGithub())
                );
                results.putResult(check.getGroupName(), task.getId(), student.getGithub(), result);

                if (analyzedStudents.add(student.getGithub())) {
                    results.putActivity(student.getGithub(), activityAnalyzer.analyze(student));
                }
            }
        }
        return results;
    }

    private Map<String, StudentConfig> checkedStudents() {
        Map<String, StudentConfig> students = new LinkedHashMap<>();
        for (CheckEntry check : config.getChecks()) {
            GroupConfig group = config.findGroup(check.getGroupName());
            TaskConfig task = config.findTask(check.getTaskId());
            if (group == null || task == null) {
                continue;
            }
            for (StudentConfig student : group.getStudents()) {
                students.putIfAbsent(student.getGithub(), student);
            }
        }
        return students;
    }

    private StudentTaskResult checkStudentTask(StudentConfig student, TaskConfig task,
                                               RepositoryCheckout checkout) {
        StudentTaskResult result = new StudentTaskResult();
        try {
            Path repoPath = repoPathOrSetError(checkout, result);
            if (repoPath == null) {
                return result;
            }

            Path taskDir = repoPath.resolve("Task_" + task.getId());
            if (!Files.exists(taskDir)) {
                result.setErrorMessage("Task directory not found: Task_" + task.getId());
                return result;
            }

            result.setSubmissionDate(repositories.lastCommitDate(repoPath, "Task_" + task.getId()));
            if (!runPreTestChecks(repoPath, taskDir, result)) {
                return result;
            }

            gradle.deleteTestResults(taskDir);
            String testError = gradle.runTask(taskDir, "test");
            if (testError != null) {
                result.setErrorMessage(testError);
            }
            result.setTestCounts(testResults.parse(taskDir));
            applyScore(student, task, result);
        } catch (Exception e) {
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    private Path repoPathOrSetError(RepositoryCheckout checkout, StudentTaskResult result) {
        if (checkout != null && checkout.repoPath() != null) {
            return checkout.repoPath();
        }

        String error = checkout == null ? null : checkout.errorMessage();
        result.setErrorMessage(error == null || error.isBlank()
            ? "Failed to clone repository"
            : error);
        return null;
    }

    private boolean runPreTestChecks(Path repoPath, Path taskDir, StudentTaskResult result) 
            throws Exception {
        String compileError = gradle.runTask(taskDir, "compileJava");
        result.setBuildSuccess(compileError == null);
        if (compileError != null) {
            result.setErrorMessage(compileError);
            return false;
        }

        String docsError = gradle.runTask(taskDir, "javadoc");
        result.setDocsSuccess(docsError == null);
        if (docsError != null) {
            result.setErrorMessage(docsError);
            return false;
        }

        String styleError = styleChecker.check(repoPath, taskDir);
        result.setStyleSuccess(styleError == null);
        if (styleError != null) {
            result.setErrorMessage(styleError);
            return false;
        }
        return true;
    }

    private void applyScore(StudentConfig student, TaskConfig task, StudentTaskResult result) {
        result.setBonusScore(
            config.getSettings().getBonusPoints(student.getGithub(),
            task.getId())
        );
        TestCounts tc = result.getTestCounts();
        if (tc.total() > 0 && tc.passed() > 0 && tc.failed() == 0) {
            result.setBaseScore(
                scoreCalculator.scoreWithDeadlines(task, result.getSubmissionDate())
            );
        }
    }
}
