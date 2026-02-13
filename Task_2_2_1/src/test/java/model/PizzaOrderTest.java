package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PizzaOrderTest {

    @Test
    public void testOrderCreation() {
        PizzaOrder order = new PizzaOrder("Pepperoni");
        assertEquals("Pepperoni", order.getType());
        assertEquals("Pending", order.getStatus());
    }

    @Test
    public void testOrderStatusUpdate() {
        PizzaOrder order = new PizzaOrder("Margherita");
        order.setStatus("In Progress");
        assertEquals("In Progress", order.getStatus());
    }

    @Test
    public void testOrderIdIncrements() {
        PizzaOrder order1 = new PizzaOrder("Cheese");
        PizzaOrder order2 = new PizzaOrder("Veggie");
        assertTrue(order2.getId() > order1.getId());
    }

    @Test
    public void testFormattedStatus() {
        PizzaOrder order = new PizzaOrder("Four Cheese");
        order.setStatus("Ready");
        assertEquals("[" + order.getId() + "] Ready", order.formattedStatus());
    }
}