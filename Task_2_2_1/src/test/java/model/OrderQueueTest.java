package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderQueueTest {

    @Test
    public void testAddOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        PizzaOrder order = new PizzaOrder("Pepperoni");
        queue.addOrder(order);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testTakeOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        PizzaOrder order1 = new PizzaOrder("Pepperoni");
        PizzaOrder order2 = new PizzaOrder("Margherita");
        queue.addOrder(order1);
        queue.addOrder(order2);

        PizzaOrder takenOrder = queue.takeOrder();
        assertEquals(order1.getId(), takenOrder.getId());
        assertEquals(order1.getType(), takenOrder.getType());
    }

    @Test
    public void testQueueEmpty() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        assertTrue(queue.isEmpty());

        PizzaOrder order = new PizzaOrder("Cheese");
        queue.addOrder(order);
        assertFalse(queue.isEmpty());

        queue.takeOrder();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testCloseStopsTake() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        queue.close();

        assertFalse(queue.isAccepting());
        PizzaOrder order = queue.takeOrder();
        assertNull(order);
    }

    @Test
    public void testAddAfterCloseThrows() {
        OrderQueue queue = new OrderQueue();
        queue.close();

        assertThrows(IllegalStateException.class, () -> queue.addOrder(new PizzaOrder("Any")));
    }
}