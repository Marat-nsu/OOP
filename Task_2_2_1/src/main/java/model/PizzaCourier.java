package model;

import java.util.ArrayList;
import java.util.List;
import model.api.DelayStrategy;
import model.api.OrderStatusSink;
import model.api.PizzaStorageOut;
import model.api.Worker;

public class PizzaCourier implements Worker {
    private final int id;
    private final int trunkCapacity; // Max number of pizzas the courier can carry
    private final PizzaStorageOut warehouse;
    private final OrderStatusSink statusSink;
    private final DelayStrategy delayStrategy;

    public PizzaCourier(
            int id,
            int trunkCapacity,
            PizzaStorageOut warehouse,
            OrderStatusSink statusSink,
            DelayStrategy delayStrategy) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.warehouse = warehouse;
        this.statusSink = statusSink;
        this.delayStrategy = delayStrategy;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<PizzaOrder> delivery = new ArrayList<>();

                PizzaOrder first = warehouse.takePizza();
                if (first == null) {
                    break;
                }
                first.transitionTo(OrderStatus.PICKED_UP_BY_COURIER);
                statusSink.emit(first);
                delivery.add(first);

                while (delivery.size() < trunkCapacity) {
                    PizzaOrder next = warehouse.tryTakePizza();
                    if (next == null) {
                        break;
                    }
                    next.transitionTo(OrderStatus.PICKED_UP_BY_COURIER);
                    statusSink.emit(next);
                    delivery.add(next);
                }

                delayStrategy.pause(2000);

                for (PizzaOrder pizza : delivery) {
                    pizza.transitionTo(OrderStatus.DELIVERED);
                    statusSink.emit(pizza);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String workerName() {
        return "Courier-" + id;
    }
}