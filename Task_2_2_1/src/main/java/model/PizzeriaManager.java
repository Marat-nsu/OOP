package model;

import java.util.List;
import model.api.OrderStatusSink;
import model.api.OrdersInbox;
import model.api.PizzaStorageIn;
import model.api.Worker;

public class PizzeriaManager {
    private final OrdersInbox orderQueue;
    private final PizzaStorageIn warehouse;
    private final List<Worker> bakers;
    private final List<Worker> couriers;
    private final OrderStatusSink statusSink;
    private final List<Thread> bakerThreads;
    private final List<Thread> courierThreads;
    private volatile boolean acceptingOrders = true;

    public PizzeriaManager(
            OrdersInbox orderQueue,
            PizzaStorageIn warehouse,
            List<Worker> bakers,
            List<Worker> couriers,
            OrderStatusSink statusSink) {
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.bakers = List.copyOf(bakers);
        this.couriers = List.copyOf(couriers);
        this.statusSink = statusSink;
        this.bakerThreads = new java.util.ArrayList<>();
        this.courierThreads = new java.util.ArrayList<>();
    }

    public void startPizzeria() {
        for (Worker baker : bakers) {
            Thread thread = new Thread(baker, baker.workerName());
            bakerThreads.add(thread);
            thread.start();
        }

        for (Worker courier : couriers) {
            Thread thread = new Thread(courier, courier.workerName());
            courierThreads.add(thread);
            thread.start();
        }
    }

    public void placeOrder(String pizzaType) {
        if (!acceptingOrders) {
            throw new IllegalStateException("Pizzeria is not accepting new orders");
        }
        PizzaOrder order = new PizzaOrder(pizzaType);
        order.transitionTo(OrderStatus.QUEUED);
        statusSink.emit(order);
        orderQueue.addOrder(order);
    }

    public void stopPizzeria() {
        acceptingOrders = false;
        orderQueue.close();

        joinThreads(bakerThreads, "Baker");
        warehouse.close();
        joinThreads(courierThreads, "Courier");
    }

    private void joinThreads(List<Thread> threads, String workerType) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(workerType + " shutdown interrupted", e);
            }

            if (thread.isAlive()) {
                throw new IllegalStateException(workerType + " thread did not terminate: " + thread.getName());
            }
        }
    }
}