package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PizzeriaManagerTest {

    @Test
    void placeOrderRejectedAfterStop() throws Exception {
        PizzeriaManager manager = new PizzeriaManager("src/main/resources/pizzeria_config.json");
        manager.stopPizzeria();

        assertThrows(IllegalStateException.class, () -> manager.placeOrder("Pepperoni"));
    }

    @Test
    void startAndProcessOrders(@TempDir Path tempDir) throws Exception {
        String config = """
            {"bakersCount":1,"bakingSpeeds":[1],"couriersCount":1,"trunkCapacities":[1],"warehouseCapacity":2}
            """;
        Path file = tempDir.resolve("config.json");
        Files.writeString(file, config);

        PizzeriaManager manager = new PizzeriaManager(file.toString());
        manager.startPizzeria();
        manager.placeOrder("TestOrder");

        Thread.sleep(2500); // allow baker and courier to complete one cycle
        manager.stopPizzeria();
    }

    @Test
    void logsUseStrictBracketedFormat(@TempDir Path tempDir) throws Exception {
        String config = """
            {"bakersCount":1,"bakingSpeeds":[1],"couriersCount":1,"trunkCapacities":[1],"warehouseCapacity":1}
            """;
        Path file = tempDir.resolve("config.json");
        Files.writeString(file, config);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outBuffer, true, StandardCharsets.UTF_8));

        try {
            PizzeriaManager manager = new PizzeriaManager(file.toString());
            manager.startPizzeria();
            manager.placeOrder("Test");
            Thread.sleep(3000);
            manager.stopPizzeria();
        } finally {
            System.setOut(originalOut);
        }

        String output = outBuffer.toString(StandardCharsets.UTF_8);
        String[] lines = output.split("\\R");

        assertTrue(lines.length > 0);
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            assertTrue(line.matches("^\\\\[\\\\d+] \\\\[([A-Z_]+)]$"), "Unexpected log line: " + line);
        }

        assertTrue(output.contains("[QUEUED]"));
        assertTrue(output.contains("[BAKING]"));
        assertTrue(output.contains("[RESERVING_WAREHOUSE_SLOT]"));
        assertTrue(output.contains("[STORED]"));
        assertTrue(output.contains("[PICKED_UP_BY_COURIER]"));
        assertTrue(output.contains("[DELIVERED]"));
    }
}
