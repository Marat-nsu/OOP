package model;

import org.junit.jupiter.api.Test;
import model.api.DelayStrategy;
import model.api.OrderStatusSink;

import static org.junit.jupiter.api.Assertions.*;

class PizzaCourierTest {

    @Test
    void testCourierInitialization() {
        Warehouse warehouse = new Warehouse(10);
        OrderStatusSink statusSink = order -> {
        };
        DelayStrategy delayStrategy = millis -> {
        };
        PizzaCourier courier = new PizzaCourier(1, 5, warehouse, statusSink, delayStrategy);

        assertNotNull(courier);
    }

    @Test
    void courierDeliversAndStopsWhenWarehouseClosed() throws Exception {
        Warehouse warehouse = new Warehouse(3);
        PizzaOrder first = new PizzaOrder("A");
        PizzaOrder second = new PizzaOrder("B");
        warehouse.storePizza(first);
        warehouse.storePizza(second);
        warehouse.close();

        OrderStatusSink statusSink = order -> {
        };
        DelayStrategy delayStrategy = millis -> {
        };
        PizzaCourier courier = new PizzaCourier(1, 2, warehouse, statusSink, delayStrategy);
        Thread worker = new Thread(courier);
        worker.start();
        worker.join(1000);

        assertFalse(worker.isAlive(), "Courier thread should finish after warehouse empties and closes");
        assertEquals(OrderStatus.DELIVERED, first.getStatus());
        assertEquals(OrderStatus.DELIVERED, second.getStatus());
    }
}