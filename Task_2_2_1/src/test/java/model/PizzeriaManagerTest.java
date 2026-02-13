package model;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
