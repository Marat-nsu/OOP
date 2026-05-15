package checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import checker.dsl.DslConfigException;
import checker.model.CheckEntry;
import checker.model.CheckpointConfig;
import checker.model.CourseConfig;
import checker.model.GroupConfig;
import checker.model.StudentConfig;
import checker.model.TaskConfig;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DslBuildersTest {
    @TempDir
    Path tempDir;

    @Test
    void loadsMapAndClosureFormsForAllTopLevelBlocks() throws Exception {
        Path script = writeScript("all-top-level-blocks.groovy");

        CourseConfig config = ConfigLoader.load(script.toFile());

        assertEquals(2, config.getTasks().size());
        TaskConfig closureTask = config.findTask("2_2_1");
        assertEquals("Closure Task", closureTask.getName());
        assertEquals(3, closureTask.getMaxScore());
        assertEquals("2026-03-14", closureTask.getHardDeadline());

        GroupConfig group = config.findGroup("24214");
        assertEquals(2, group.getStudents().size());
        StudentConfig mapStudent = group.getStudents().get(0);
        assertEquals("mapStudent", mapStudent.getGithub());
        assertEquals("Map Student", mapStudent.getFullName());
        assertEquals("https://example.com/map.git", mapStudent.getRepoUrl());

        StudentConfig closureStudent = group.getStudents().get(1);
        assertEquals("closureStudent", closureStudent.getGithub());
        assertEquals("Closure Student", closureStudent.getFullName());
        assertEquals("https://example.com/closure.git", closureStudent.getRepoUrl());

        assertEquals(2, config.getChecks().size());
        CheckEntry closureCheck = config.getChecks().get(1);
        assertEquals("2_2_1", closureCheck.getTaskId());
        assertEquals("24214", closureCheck.getGroupName());

        assertEquals(2, config.getCheckpoints().size());
        CheckpointConfig closureCheckpoint = config.getCheckpoints().get(1);
        assertEquals("KT-2", closureCheckpoint.getName());
        assertEquals("2026-04-01", closureCheckpoint.getDate());
    }

    @Test
    void supportsStudentAndCheckPropertyAliases() throws Exception {
        Path script = writeScript("aliases.groovy");

        CourseConfig config = ConfigLoader.load(script.toFile());

        GroupConfig group = config.findGroup("24214");
        assertEquals("Full Name", group.getStudents().get(0).getFullName());
        assertEquals("https://example.com/full.git", group.getStudents().get(0).getRepoUrl());
        assertEquals("Short Name", group.getStudents().get(1).getFullName());
        assertEquals("https://example.com/short.git", group.getStudents().get(1).getRepoUrl());
        assertEquals("2_1_1", config.getChecks().get(0).getTaskId());
        assertEquals("24214", config.getChecks().get(0).getGroupName());
        assertEquals("2_1_1", config.getChecks().get(1).getTaskId());
        assertEquals("24214", config.getChecks().get(1).getGroupName());
        assertEquals(2, config.getChecks().get(1).getStudentGithubs().size());
        assertEquals("full", config.getChecks().get(1).getStudentGithubs().get(0));
    }

    @Test
    void loadsAllSettingsAndBonusDslMethods() throws Exception {
        Path script = writeScript("settings-and-bonus.groovy");

        CourseConfig config = ConfigLoader.load(script.toFile());

        assertEquals("/tmp/oop-test", config.getSettings().getWorkDir());
        assertEquals(42, config.getSettings().getTestTimeoutSeconds());
        assertEquals("/tmp/java", config.getSettings().getJavaHome());
        assertEquals("2026-02-10", config.getSettings().getCourseStartDate());
        assertEquals("2026-06-20", config.getSettings().getCourseEndDate());
        assertEquals(5, config.getSettings().getMaxActivityBonus());
        assertEquals(1, config.getSettings().getRepositoryDownloadParallelism());
        assertEquals(3, config.getSettings().getTaskCheckThreadCount());
        assertEquals("satisfactory", config.getSettings().computeGrade(3));
        assertEquals("good", config.getSettings().computeGrade(5));
        assertEquals(2, config.getSettings().getBonusPoints("student", "2_1_1"));
    }

    @Test
    void importConfigAliasResolvesRelativeToImportingScript() throws Exception {
        Path nested = tempDir.resolve("nested");
        Files.createDirectories(nested);
        writeScript("import/common.groovy", nested.resolve("common.groovy"));
        Path root = writeScript("import/root.groovy", nested.resolve("oop.groovy"));

        CourseConfig config = ConfigLoader.load(root.toFile());

        assertEquals(1, config.getTasks().size());
        assertEquals("Imported Task", config.findTask("2_1_1").getName());
    }

    @Test
    void reportsMissingRequiredFieldAsDslException() throws Exception {
        Path script = writeScript("invalid-task.groovy");

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("task.id"));
    }

    @Test
    void reportsUnknownDslMethodAsDslException() throws Exception {
        Path script = writeScript("unknown-method.groovy");

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("Unknown DSL method"));
        assertTrue(ex.getMessage().contains("unknownBlock"));
    }

    @Test
    void reportsMissingIncludedFileAsDslException() throws Exception {
        Path script = writeScript("missing-include.groovy");

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("DSL config file not found"));
        assertTrue(ex.getMessage().contains("missing.groovy"));
    }

    private Path writeScript(String resourceName) throws IOException {
        return writeScript(resourceName, tempDir.resolve(Path.of(resourceName).getFileName()));
    }

    private Path writeScript(String resourceName, Path target) throws IOException {
        String resourcePath = "/checker/dsl/" + resourceName;
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new AssertionError("Missing test resource: " + resourcePath);
            }
            Files.writeString(target, new String(input.readAllBytes(), StandardCharsets.UTF_8));
            return target;
        }
    }
}
