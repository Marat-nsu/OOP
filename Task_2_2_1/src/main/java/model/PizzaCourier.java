package model;

import java.util.ArrayList;
import java.util.List;

public class PizzaCourier implements Runnable {
    private final int id;
    private final int trunkCapacity; // Max number of pizzas the courier can carry
    private final Warehouse warehouse;

    public PizzaCourier(int id, int trunkCapacity, Warehouse warehouse) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<PizzaOrder> delivery = new ArrayList<>();

                PizzaOrder first = warehouse.takePizza();
                if (first == null) {
                    break; // warehouse closed and empty
                }
                first.transitionTo(OrderStatus.PICKED_UP_BY_COURIER);
                delivery.add(first);

                while (delivery.size() < trunkCapacity) {
                    PizzaOrder next = warehouse.tryTakePizza();
                    if (next == null) {
                        break;
                    }
                    next.transitionTo(OrderStatus.PICKED_UP_BY_COURIER);
                    delivery.add(next);
                }

                Thread.sleep(2000);

                for (PizzaOrder pizza : delivery) {
                    pizza.transitionTo(OrderStatus.DELIVERED);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}