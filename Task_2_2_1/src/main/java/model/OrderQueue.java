package model;

import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {
    private final Queue<PizzaOrder> orders;
    private boolean accepting = true;

    public OrderQueue() {
        this.orders = new LinkedList<>();
    }

    public synchronized void addOrder(PizzaOrder order) {
        if (!accepting) {
            throw new IllegalStateException("Pizzeria is not accepting new orders");
        }
        orders.add(order);
        notifyAll();
    }

    public synchronized PizzaOrder takeOrder() throws InterruptedException {
        while (orders.isEmpty() && accepting) {
            wait();
        }
        if (orders.isEmpty()) {
            return null; // closed and empty
        }
        return orders.poll();
    }

    public synchronized boolean isEmpty() {
        return orders.isEmpty();
    }

    public synchronized void close() {
        accepting = false;
        notifyAll();
    }

    public synchronized boolean isAccepting() {
        return accepting;
    }

    @Override
    public String toString() {
        return "OrderQueue{" + "orders=" + orders + '}';
    }
}