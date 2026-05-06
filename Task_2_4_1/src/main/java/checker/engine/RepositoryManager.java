package checker.engine;

import checker.logging.CheckerLogger;
import checker.model.StudentConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class RepositoryManager {
    private final Path workDir;
    private final CommandRunner commands;
    private final CheckerLogger log;

    RepositoryManager(Path workDir, CommandRunner commands, CheckerLogger log) {
        this.workDir = workDir;
        this.commands = commands;
        this.log = log;
    }

    Map<String, RepositoryCheckout> checkoutAll(Collection<StudentConfig> students, int parallelism)
            throws InterruptedException {
        if (students.isEmpty()) {
            return Map.of();
        }

        int threads = Math.min(students.size(), parallelism);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Map<String, Future<RepositoryCheckout>> futures = new LinkedHashMap<>();
        for (StudentConfig student : students) {
            futures.put(student.getGithub(), executor.submit(() -> checkout(student)));
        }

        Map<String, RepositoryCheckout> repositories = new LinkedHashMap<>();
        try {
            for (Map.Entry<String, Future<RepositoryCheckout>> entry : futures.entrySet()) {
                try {
                    repositories.put(entry.getKey(), entry.getValue().get());
                } catch (ExecutionException e) {
                    repositories.put(entry.getKey(),
                        new RepositoryCheckout(null, e.getCause().getMessage()));
                }
            }
        } finally {
            executor.shutdownNow();
        }
        return repositories;
    }

    String lastCommitDate(Path repoPath, String taskPath) {
        try {
            ProcessResult pr = commands.run(repoPath, 30,
                "git", "log", "-1", "--format=%ad", "--date=short", "--", taskPath);
            return pr.exitCode() == 0 ? pr.output().strip() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private RepositoryCheckout checkout(StudentConfig student) {
        try {
            Path repoPath = cloneOrUpdate(student);
            if (repoPath == null) {
                return new RepositoryCheckout(null, "Failed to clone repository");
            }
            return new RepositoryCheckout(repoPath, null);
        } catch (Exception e) {
            return new RepositoryCheckout(null, e.getMessage());
        }
    }

    private Path cloneOrUpdate(StudentConfig student) throws IOException, InterruptedException {
        Path repoPath = workDir.resolve(student.getGithub());
        if (Files.exists(repoPath.resolve(".git"))) {
            checkoutMainBranch(repoPath);
            commands.run(repoPath, 60, "git", "pull");
        } else {
            if (student.getRepoUrl() == null || student.getRepoUrl().isBlank()) {
                return null;
            }
            ProcessResult pr = commands.run(workDir, 120,
                "git", "clone", student.getRepoUrl(), student.getGithub());
            if (pr.exitCode() != 0) {
                return null;
            }
            checkoutMainBranch(repoPath);
        }
        makeGradlewExecutable(repoPath);
        return repoPath;
    }

    private void checkoutMainBranch(Path repoPath) throws IOException, InterruptedException {
        if (commands.run(repoPath, 20, "git", "rev-parse", "--verify", "main").exitCode() == 0) {
            commands.run(repoPath, 30, "git", "checkout", "main");
        } else if (
            commands.run(repoPath, 20, "git", "rev-parse", "--verify", "master").exitCode() == 0) {
            commands.run(repoPath, 30, "git", "checkout", "master");
        }
    }

    private void makeGradlewExecutable(Path repoPath) {
        try (var stream = Files.walk(repoPath)) {
            stream.filter(p -> p.getFileName().toString().equals("gradlew"))
                  .forEach(p -> p.toFile().setExecutable(true));
        } catch (IOException e) {
            log.warn("Could not chmod gradlew: " + e.getMessage());
        }
    }
}
