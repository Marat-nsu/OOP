package model;

import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue implements OrdersInbox, OrdersOutbox {
    private final Queue<PizzaOrder> orders;
    private boolean accepting = true;

    public OrderQueue() {
        this.orders = new LinkedList<>();
    }

    @Override
    public synchronized void addOrder(PizzaOrder order) {
        if (!accepting) {
            throw new IllegalStateException("Pizzeria is not accepting new orders");
        }
        orders.add(order);
        notifyAll();
    }

    @Override
    public synchronized PizzaOrder takeOrder() throws InterruptedException {
        while (orders.isEmpty() && accepting) {
            wait();
        }
        if (orders.isEmpty()) {
            return null;
        }
        return orders.poll();
    }

    public synchronized boolean isEmpty() {
        return orders.isEmpty();
    }

    @Override
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