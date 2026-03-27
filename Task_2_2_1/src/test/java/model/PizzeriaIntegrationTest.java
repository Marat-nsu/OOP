package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import config.PizzeriaConfig;
import config.PizzeriaConfigLoader;
import config.PizzeriaConfigSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import model.api.OrderStatusSink;

class PizzeriaIntegrationTest {

    private static final int ORDER_COUNT = 100;

    @Test
    void processesHundredOrdersAndShutsDownGracefully(@TempDir Path tempDir) throws Exception {
        String configJson = """
            {
              "bakersCount": 4,
              "bakingSpeeds": [1, 1, 1, 1],
              "couriersCount": 1,
              "trunkCapacities": [100],
              "warehouseCapacity": 100
            }
            """;

        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, configJson);

        PizzeriaConfigSource configSource = new PizzeriaConfigLoader(configFile.toString());
        PizzeriaConfig config = configSource.load();

        CollectingOrderStatusSink statusSink = new CollectingOrderStatusSink();
        PizzeriaFactory factory = new PizzeriaFactory();
        PizzeriaManager manager = factory.create(config, statusSink);

        manager.startPizzeria();
        try {
            for (int i = 0; i < ORDER_COUNT; i++) {
                manager.placeOrder("Pizza-" + i);
            }

            boolean allDelivered = waitUntilAllDelivered(statusSink, ORDER_COUNT, 15000);
            assertTrue(allDelivered, "Timed out waiting for all orders to be delivered");
        } finally {
            manager.stopPizzeria();
        }

        assertEquals(ORDER_COUNT, statusSink.deliveredCount(), "All orders must be delivered exactly once");
        assertThrows(IllegalStateException.class, () -> manager.placeOrder("AfterStop"));
    }

    private boolean waitUntilAllDelivered(CollectingOrderStatusSink sink, int expectedDelivered, long timeoutMs)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (sink.deliveredCount() == expectedDelivered) {
                return true;
            }
            Thread.sleep(20);
        }
        return sink.deliveredCount() == expectedDelivered;
    }

    private static final class CollectingOrderStatusSink implements OrderStatusSink {
        private final Set<Integer> deliveredOrderIds = ConcurrentHashMap.newKeySet();

        @Override
        public void emit(PizzaOrder order) {
            if (order.getStatus() == OrderStatus.DELIVERED) {
                deliveredOrderIds.add(order.getId());
            }
        }

        int deliveredCount() {
            return deliveredOrderIds.size();
        }
    }
}
