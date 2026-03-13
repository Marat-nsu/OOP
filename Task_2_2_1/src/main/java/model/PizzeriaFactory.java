package model;

import config.PizzeriaConfig;
import java.util.ArrayList;
import java.util.List;
import model.api.DelayStrategy;
import model.api.OrderStatusSink;
import model.api.Worker;

public class PizzeriaFactory {
    public PizzeriaManager create(PizzeriaConfig config, OrderStatusSink statusSink) {
        OrderQueue orderQueue = new OrderQueue();
        Warehouse warehouse = new Warehouse(config.warehouseCapacity());
        DelayStrategy delayStrategy = new ThreadSleepDelay();

        List<Worker> bakers = new ArrayList<>();
        for (int i = 0; i < config.bakersCount(); i++) {
            bakers.add(new PizzaBaker(
                    i + 1,
                    config.bakingSpeeds()[i],
                    orderQueue,
                    warehouse,
                    statusSink,
                    delayStrategy));
        }

        List<Worker> couriers = new ArrayList<>();
        for (int i = 0; i < config.couriersCount(); i++) {
            couriers.add(new PizzaCourier(
                    i + 1,
                    config.trunkCapacities()[i],
                    warehouse,
                    statusSink,
                    delayStrategy));
        }

        return new PizzeriaManager(orderQueue, warehouse, bakers, couriers, statusSink);
    }
}
