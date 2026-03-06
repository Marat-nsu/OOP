package model;

import java.util.LinkedList;
import java.util.Queue;

public class Warehouse implements PizzaStorageIn, PizzaStorageOut {
    private final int capacity;
    private final Queue<PizzaOrder> storedPizzas;
    private boolean closed = false;

    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.storedPizzas = new LinkedList<>();
    }

    @Override
    public synchronized void storePizza(PizzaOrder order) throws InterruptedException {
        while (storedPizzas.size() >= capacity && !closed) {
            wait();
        }

        if (closed) {
            throw new IllegalStateException("Warehouse is closed for storing pizzas");
        }

        storedPizzas.add(order);
        notifyAll();
    }

    @Override
    public synchronized PizzaOrder takePizza() throws InterruptedException {
        while (storedPizzas.isEmpty() && !closed) {
            wait();
        }
        if (storedPizzas.isEmpty()) {
            return null;
        }
        PizzaOrder pizza = storedPizzas.poll();
        notifyAll();
        return pizza;
    }

    @Override
    public synchronized PizzaOrder tryTakePizza() {
        if (storedPizzas.isEmpty()) {
            return null;
        }
        PizzaOrder pizza = storedPizzas.poll();
        notifyAll();
        return pizza;
    }

    @Override
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public synchronized int getAvailablePizzaCount() {
        return storedPizzas.size();
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "Warehouse: " + storedPizzas.size() + "/" + capacity + " pizzas stored";
    }
}