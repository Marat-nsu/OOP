package checker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import checker.model.CourseConfig;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CheckEngineIntegrationTest {
    @TempDir
    Path tempDir;

    @Test
    void checksLocalGitRepositoryAndGeneratesHtmlReport() throws Exception {
        Path sourceRepo = tempDir.resolve("source-repo");
        createStudentRepository(sourceRepo, 1, 1);

        Path workDir = tempDir.resolve("work");
        Path configFile = writeConfig(
            "checks-local-repository.groovy",
            sourceRepo.toUri(),
            workDir
        );

        CourseConfig config = ConfigLoader.load(configFile.toFile());
        String html = Checker.check(config);

        assertTrue(Files.exists(workDir.resolve("student/.git")));
        assertTrue(html.contains("Student Name"));
        assertTrue(html.contains("Компиляция"));
        assertTrue(html.contains("Документация"));
        assertTrue(html.contains("Google Style"));
        assertTrue(html.contains("2/1/1"));
        assertTrue(html.contains("Final"));
        assertTrue(html.contains("Итоговая оценка"));
    }

    @Test
    void awardsTaskAndActivityPointsWhenAllChecksPass() throws Exception {
        Path sourceRepo = tempDir.resolve("passing-repo");
        createStudentRepository(sourceRepo, 0, 0);

        Path workDir = tempDir.resolve("passing-work");
        Path configFile = writeConfig("passing-checks.groovy", sourceRepo.toUri(), workDir);

        CourseConfig config = ConfigLoader.load(configFile.toFile());
        String html = Checker.check(config);

        assertTrue(html.contains("<td>4</td>"));
        assertTrue(html.contains("<td>5</td>"));
    }

    @Test
    void softDeadlineUsesFirstCommitAndHardDeadlineUsesLastCommit() throws Exception {
        Path sourceRepo = tempDir.resolve("deadline-repo");
        createStudentRepositoryWithTwoTaskCommits(sourceRepo);

        Path workDir = tempDir.resolve("deadline-work");
        Path configFile = writeConfig("deadline-checks.groovy", sourceRepo.toUri(), workDir);

        String html = Checker.check(ConfigLoader.load(configFile.toFile()));

        assertTrue(html.contains("2026-02-10..2026-02-22"));
        assertTrue(html.contains("<td>3.5</td>"));
    }

    @Test
    void downloadsAllCheckedRepositoriesBeforeRunningChecks() throws Exception {
        Path firstRepo = tempDir.resolve("first-repo");
        Path secondRepo = tempDir.resolve("second-repo");
        createStudentRepository(firstRepo, 0, 0);
        createStudentRepository(secondRepo, 0, 0);

        Path workDir = tempDir.resolve("multi-work");
        Path configFile = writeConfig(
            "multiple-repositories.groovy",
            firstRepo.toUri(),
            secondRepo.toUri(),
            workDir
        );

        String html = Checker.check(ConfigLoader.load(configFile.toFile()));

        assertTrue(Files.exists(workDir.resolve("first/.git")));
        assertTrue(Files.exists(workDir.resolve("second/.git")));
        assertTrue(html.contains("First Student"));
        assertTrue(html.contains("Second Student"));
    }

    @Test
    void checksOnlySelectedStudentsFromGroup() throws Exception {
        Path selectedRepo = tempDir.resolve("selected-repo");
        Path skippedRepo = tempDir.resolve("skipped-repo");
        createStudentRepository(selectedRepo, 0, 0);
        createStudentRepository(skippedRepo, 0, 0);

        Path workDir = tempDir.resolve("selected-work");
        Path configFile = writeConfig(
            "selected-students.groovy",
            selectedRepo.toUri(),
            skippedRepo.toUri(),
            workDir
        );

        String html = Checker.check(ConfigLoader.load(configFile.toFile()));

        assertTrue(Files.exists(workDir.resolve("selected/.git")));
        assertFalse(Files.exists(workDir.resolve("skipped/.git")));
        assertTrue(html.contains("Selected Student"));
        assertFalse(html.contains("Skipped Student"));
    }

    private Path writeConfig(String resourceName, Object... args) throws IOException {
        String resourcePath = "/checker/integration/" + resourceName;
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new AssertionError("Missing test resource: " + resourcePath);
            }
            Path configFile = tempDir.resolve(resourceName);
            String template = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Files.writeString(configFile, template.formatted(args));
            return configFile;
        }
    }

    private void createStudentRepository(Path repo, int failures, int skipped) throws Exception {
        Path taskDir = repo.resolve("Task_2_4_1");
        Files.createDirectories(taskDir);
        Files.createDirectories(repo.resolve(".github"));
        Files.writeString(repo.resolve(".github/google_checks.xml"), """
            <?xml version="1.0"?>
            <!DOCTYPE module PUBLIC
                      "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
                      "https://checkstyle.org/dtds/configuration_1_3.dtd">
            <module name="Checker"/>
            """);
        Path gradlew = taskDir.resolve("gradlew");
        Files.writeString(gradlew, """
            #!/bin/sh
            case "$1" in
              tasks)
                printf 'compileJava - Compiles main Java source.\\n'
                printf 'javadoc - Generates Javadoc API documentation.\\n'
                printf 'checkstyleMain - Runs Checkstyle analysis for main classes.\\n'
                printf 'test - Runs the test suite.\\n'
                exit 0
                ;;
              compileJava|javadoc|checkstyleMain)
                exit 0
                ;;
              test)
                mkdir -p build/test-results/test
                cat > build/test-results/test/TEST-local.xml <<'XML'
            <?xml version="1.0" encoding="UTF-8"?>
            <testsuite name="local" tests="4" failures="%d" errors="0" skipped="%d">
              <testcase classname="LocalTest" name="passesOne"/>
              <testcase classname="LocalTest" name="passesTwo"/>
              <testcase classname="LocalTest" name="failsOne"><failure message="failure"/></testcase>
              <testcase classname="LocalTest" name="skipsOne"><skipped/></testcase>
            </testsuite>
            XML
                exit 0
                ;;
              *)
                exit 1
                ;;
            esac
            """.formatted(failures, skipped));
        gradlew.toFile().setExecutable(true);
        Files.writeString(taskDir.resolve("README.md"), "test task\n");

        run(repo.getParent(), "git", "init", repo.getFileName().toString());
        run(repo, "git", "checkout", "-b", "main");
        run(repo, "git", "add", ".");
        run(repo, "git", "-c", "user.name=Test User", "-c", "user.email=test@example.com",
            "commit", "-m", "Initial task");
    }

    private void createStudentRepositoryWithTwoTaskCommits(Path repo) throws Exception {
        createStudentRepository(repo, 0, 0);
        run(repo, "git", "-c", "user.name=Test User", "-c", "user.email=test@example.com",
            "commit", "--amend", "--no-edit",
            "--date", "2026-02-10T12:00:00+07:00");

        Files.writeString(repo.resolve("Task_2_4_1/README.md"), "updated task\n");
        run(repo, "git", "add", ".");
        run(repo, "git", "-c", "user.name=Test User", "-c", "user.email=test@example.com",
            "commit", "--date", "2026-02-22T12:00:00+07:00", "-m", "Update task");
    }

    private void run(Path dir, String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir.toFile());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new AssertionError(String.join(" ", command) + " failed:\n" + output);
        }
    }
}
