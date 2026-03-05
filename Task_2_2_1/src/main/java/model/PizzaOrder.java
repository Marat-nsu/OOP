package model;

import java.util.concurrent.atomic.AtomicInteger;

public class PizzaOrder {
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    private final int id;
    private final String type;
    private volatile OrderStatus status;

    public PizzaOrder(String type) {
        this.id = idCounter.incrementAndGet();
        this.type = type;
        this.status = OrderStatus.QUEUED;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public synchronized OrderStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(OrderStatus status) {
        this.status = status;
    }

    public synchronized void transitionTo(OrderStatus status) {
        this.status = status;
        System.out.println(formattedStatus());
    }

    public synchronized String formattedStatus() {
        return "[" + id + "] [" + status + "]";
    }

    @Override
    public String toString() {
        return "Order " + id + " [Type: " + type + ", Status: " + status + "]";
    }
}