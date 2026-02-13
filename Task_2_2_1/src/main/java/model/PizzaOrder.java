package model;

import java.util.concurrent.atomic.AtomicInteger;

public class PizzaOrder {
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    private final int id;
    private final String type;
    private String status;

    public PizzaOrder(String type) {
        this.id = idCounter.incrementAndGet();
        this.type = type;
        this.status = "Pending";
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String formattedStatus() {
        return "[" + id + "] " + status;
    }

    @Override
    public String toString() {
        return "Order " + id + " [Type: " + type + ", Status: " + status + "]";
    }
}