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
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DslBuildersTest {
    @TempDir
    Path tempDir;

    @Test
    void loadsMapAndClosureFormsForAllTopLevelBlocks() throws Exception {
        Path script = tempDir.resolve("oop.groovy");
        Files.writeString(script, """
            tasks {
                task(id: "2_1_1", name: "Map Task", maxScore: 1,
                     softDeadline: "2026-02-14", hardDeadline: "2026-02-21")
                task {
                    id = "2_2_1"
                    name = "Closure Task"
                    maxScore = 3
                    softDeadline = "2026-03-07"
                    hardDeadline = "2026-03-14"
                }
            }

            groups {
                group(name: "24214") {
                    student(github: "mapStudent", name: "Map Student", repo: "https://example.com/map.git")
                    student {
                        github = "closureStudent"
                        fullName = "Closure Student"
                        repoUrl = "https://example.com/closure.git"
                    }
                }
            }

            checks {
                check(task: "2_1_1", group: "24214")
                check {
                    taskId = "2_2_1"
                    groupName = "24214"
                }
            }

            checkpoints {
                checkpoint(name: "KT-1", date: "2026-03-01")
                checkpoint {
                    name = "KT-2"
                    date = "2026-04-01"
                }
            }
            """);

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
        Path script = tempDir.resolve("aliases.groovy");
        Files.writeString(script, """
            tasks {
                task(id: "2_1_1", name: "Task", maxScore: 1)
            }

            groups {
                group(name: "24214") {
                    student(github: "full", fullName: "Full Name", repoUrl: "https://example.com/full.git")
                    student(github: "short", name: "Short Name", repo: "https://example.com/short.git")
                }
            }

            checks {
                check(taskId: "2_1_1", groupName: "24214")
                check(task: "2_1_1", group: "24214")
            }
            """);

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
    }

    @Test
    void loadsAllSettingsAndBonusDslMethods() throws Exception {
        Path script = tempDir.resolve("settings.groovy");
        Files.writeString(script, """
            settings {
                workDir = "/tmp/oop-test"
                testTimeoutSeconds = 42
                javaHome = "/tmp/java"
                courseStartDate = "2026-02-10"
                courseEndDate = "2026-06-20"
                maxActivityBonus = 5
                repositoryDownloadParallelism = 0

                grade(minScore: 3, value: "satisfactory")
                grade(minScore: 5, grade: "good")
                bonus(student: "student", task: "2_1_1", points: 2)
            }
            """);

        CourseConfig config = ConfigLoader.load(script.toFile());

        assertEquals("/tmp/oop-test", config.getSettings().getWorkDir());
        assertEquals(42, config.getSettings().getTestTimeoutSeconds());
        assertEquals("/tmp/java", config.getSettings().getJavaHome());
        assertEquals("2026-02-10", config.getSettings().getCourseStartDate());
        assertEquals("2026-06-20", config.getSettings().getCourseEndDate());
        assertEquals(5, config.getSettings().getMaxActivityBonus());
        assertEquals(1, config.getSettings().getRepositoryDownloadParallelism());
        assertEquals("satisfactory", config.getSettings().computeGrade(3));
        assertEquals("good", config.getSettings().computeGrade(5));
        assertEquals(2, config.getSettings().getBonusPoints("student", "2_1_1"));
    }

    @Test
    void importConfigAliasResolvesRelativeToImportingScript() throws Exception {
        Path nested = tempDir.resolve("nested");
        Files.createDirectories(nested);
        Files.writeString(nested.resolve("common.groovy"), """
            tasks {
                task(id: "2_1_1", name: "Imported Task", maxScore: 1)
            }
            """);
        Path root = nested.resolve("oop.groovy");
        Files.writeString(root, """
            importConfig "common.groovy"
            """);

        CourseConfig config = ConfigLoader.load(root.toFile());

        assertEquals(1, config.getTasks().size());
        assertEquals("Imported Task", config.findTask("2_1_1").getName());
    }

    @Test
    void reportsMissingRequiredFieldAsDslException() throws Exception {
        Path script = tempDir.resolve("invalid-task.groovy");
        Files.writeString(script, """
            tasks {
                task(name: "Task without id", maxScore: 1)
            }
            """);

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("task.id"));
    }

    @Test
    void reportsUnknownDslMethodAsDslException() throws Exception {
        Path script = tempDir.resolve("unknown-method.groovy");
        Files.writeString(script, """
            unknownBlock {
            }
            """);

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("Unknown DSL method"));
        assertTrue(ex.getMessage().contains("unknownBlock"));
    }

    @Test
    void reportsMissingIncludedFileAsDslException() throws Exception {
        Path script = tempDir.resolve("missing-include.groovy");
        Files.writeString(script, """
            include "missing.groovy"
            """);

        DslConfigException ex = assertThrows(
            DslConfigException.class,
            () -> ConfigLoader.load(script.toFile())
        );

        assertTrue(ex.getMessage().contains("DSL config file not found"));
        assertTrue(ex.getMessage().contains("missing.groovy"));
    }
}
