package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PizzaBakerTest {
    @Test
    void testBakerInitialization() {
        OrderQueue orderQueue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzaBaker baker = new PizzaBaker(1, 1000, orderQueue, warehouse);

        assertEquals(1, baker.getId());
    }

    @Test
    void bakerProcessesOrderAndStopsWhenQueueClosed() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Warehouse warehouse = new Warehouse(5);
        PizzaBaker baker = new PizzaBaker(1, 1, orderQueue, warehouse);

        orderQueue.addOrder(new PizzaOrder("Test"));
        orderQueue.close();

        Thread worker = new Thread(baker);
        worker.start();
        worker.join(1000);

        assertFalse(worker.isAlive(), "Baker thread should finish after queue is closed and drained");
        assertEquals(1, warehouse.getAvailablePizzaCount());
    }
}