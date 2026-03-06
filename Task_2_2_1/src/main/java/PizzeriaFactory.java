import config.PizzeriaConfig;
import java.util.ArrayList;
import java.util.List;
import model.DelayStrategy;
import model.OrderQueue;
import model.OrderStatusSink;
import model.PizzeriaManager;
import model.PizzaBaker;
import model.PizzaCourier;
import model.ThreadSleepDelay;
import model.Warehouse;
import model.Worker;

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
