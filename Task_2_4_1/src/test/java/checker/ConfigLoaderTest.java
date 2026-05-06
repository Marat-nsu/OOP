package checker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import checker.model.CourseConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {
    @Test
    void loadsIncludedConfigsAndCheckpointsIntoOneCourseConfig() throws Exception {
        Path dir = Files.createTempDirectory("oop-config-test");
        Path common = dir.resolve("common.groovy");
        Files.writeString(common, """
            tasks {
                task(id: "2_1_1", name: "Primes", maxScore: 1,
                     softDeadline: "2026-02-14", hardDeadline: "2026-02-21")
            }

            groups {
                group(name: "24214") {
                    student(github: "student", name: "Student Name", repo: "https://example.com/repo.git")
                }
            }
            """);

        Path root = dir.resolve("oop.groovy");
        Files.writeString(root, """
            include "common.groovy"

            checks {
                check(task: "2_1_1", group: "24214")
            }

            checkpoints {
                checkpoint(name: "KT-1", date: "2026-03-01")
            }
            """);

        CourseConfig config = ConfigLoader.load(root.toFile());

        assertEquals(1, config.getTasks().size());
        assertEquals(1, config.getGroups().size());
        assertEquals(1, config.getChecks().size());
        assertEquals(1, config.getCheckpoints().size());
        assertEquals("KT-1", config.getCheckpoints().get(0).getName());
    }
}
