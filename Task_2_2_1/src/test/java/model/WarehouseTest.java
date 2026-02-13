package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WarehouseTest {

    @Test
    void testInitialization() {
        Warehouse warehouse = new Warehouse(10);
        assertEquals(0, warehouse.getAvailablePizzaCount());
    }

    @Test
    void testStorePizza() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);
        PizzaOrder order = new PizzaOrder("Pepperoni");
        warehouse.storePizza(order);

        assertEquals(1, warehouse.getAvailablePizzaCount());
    }

    @Test
    void testTakePizza() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);
        PizzaOrder order = new PizzaOrder("Margherita");
        warehouse.storePizza(order);

        PizzaOrder takenOrder = warehouse.takePizza();
        assertEquals(order.getId(), takenOrder.getId());
        assertEquals(order.getType(), takenOrder.getType());

        assertEquals(0, warehouse.getAvailablePizzaCount());
    }

    @Test
    void testCloseReturnsNullWhenEmpty() throws InterruptedException {
        Warehouse warehouse = new Warehouse(2);
        warehouse.close();

        PizzaOrder order = warehouse.takePizza();
        assertNull(order);
    }

    @Test
    void tryTakePizzaDrainsWithoutBlocking() throws InterruptedException {
        Warehouse warehouse = new Warehouse(2);
        PizzaOrder order = new PizzaOrder("Test");
        warehouse.storePizza(order);

        PizzaOrder taken = warehouse.tryTakePizza();
        assertEquals(order.getId(), taken.getId());
        assertEquals(0, warehouse.getAvailablePizzaCount());
    }
}