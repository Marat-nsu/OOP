package model;

import config.PizzeriaConfigLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PizzeriaManager {
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<PizzaBaker> bakers;
    private final List<PizzaCourier> couriers;
    private final List<Thread> bakerThreads;
    private final List<Thread> courierThreads;
    private volatile boolean acceptingOrders = true;

    public PizzeriaManager(String configFilePath) throws IOException {
        PizzeriaConfigLoader config = new PizzeriaConfigLoader(configFilePath);

        this.orderQueue = new OrderQueue();
        this.warehouse = new Warehouse(config.getWarehouseCapacity());

        this.bakers = new ArrayList<>();
        for (int i = 0; i < config.getBakersCount(); i++) {
            bakers.add(new PizzaBaker(i + 1, config.getBakingSpeeds()[i], orderQueue, warehouse));
        }

        this.couriers = new ArrayList<>();
        for (int i = 0; i < config.getCourierCount(); i++) {
            couriers.add(new PizzaCourier(i + 1, config.getTrunkCapacities()[i], warehouse));
        }

        this.bakerThreads = new ArrayList<>();
        this.courierThreads = new ArrayList<>();
    }

    public void startPizzeria() {
        for (PizzaBaker baker : bakers) {
            Thread thread = new Thread(baker, "Baker-" + baker.getId());
            bakerThreads.add(thread);
            thread.start();
        }

        for (int i = 0; i < couriers.size(); i++) {
            PizzaCourier courier = couriers.get(i);
            Thread thread = new Thread(courier, "Courier-" + (i + 1));
            courierThreads.add(thread);
            thread.start();
        }
    }

    public void placeOrder(String pizzaType) {
        if (!acceptingOrders) {
            throw new IllegalStateException("Pizzeria is not accepting new orders");
        }
        PizzaOrder order = new PizzaOrder(pizzaType);
        order.setStatus(OrderStatus.QUEUED);
        System.out.println(order.formattedStatus());
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