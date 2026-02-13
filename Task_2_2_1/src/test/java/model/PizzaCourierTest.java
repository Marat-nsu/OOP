package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PizzaCourierTest {

    @Test
    void testCourierInitialization() {
        Warehouse warehouse = new Warehouse(10);
        PizzaCourier courier = new PizzaCourier(1, 5, warehouse);

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

        PizzaCourier courier = new PizzaCourier(1, 2, warehouse);
        Thread worker = new Thread(courier);
        worker.start();
        worker.join(4000);

        assertFalse(worker.isAlive(), "Courier thread should finish after warehouse empties and closes");
        assertEquals("Delivered by courier 1", first.getStatus());
        assertEquals("Delivered by courier 1", second.getStatus());
    }
}