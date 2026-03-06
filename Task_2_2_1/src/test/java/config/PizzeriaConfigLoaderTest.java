package config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PizzeriaConfigLoaderTest {

    @Test
    void loadsConfigValues() throws Exception {
        PizzeriaConfigSource configSource = new PizzeriaConfigLoader("src/main/resources/pizzeria_config.json");
        PizzeriaConfig config = configSource.load();

        assertEquals(3, config.bakersCount());
        assertEquals(2, config.couriersCount());
        assertEquals(10, config.warehouseCapacity());
    }

    @Test
    void throwsWhenMismatchedArrays(@TempDir Path tempDir) throws IOException {
        String badConfig = """
            {"bakersCount":2,"bakingSpeeds":[1000],"couriersCount":1,"trunkCapacities":[2],"warehouseCapacity":3}
            """;
        Path file = tempDir.resolve("bad.json");
        Files.writeString(file, badConfig);

        assertThrows(IllegalArgumentException.class, () -> new PizzeriaConfigLoader(file.toString()).load());
    }

    @Test
    void throwsWhenNonPositiveValues(@TempDir Path tempDir) throws IOException {
        String badConfig = """
            {"bakersCount":1,"bakingSpeeds":[0],"couriersCount":1,"trunkCapacities":[-1],"warehouseCapacity":0}
            """;
        Path file = tempDir.resolve("non-positive.json");
        Files.writeString(file, badConfig);

        assertThrows(IllegalArgumentException.class, () -> new PizzeriaConfigLoader(file.toString()).load());
    }
}
