package model;

public class PizzaBaker implements Worker {
    private final int id;
    private final int bakingSpeed; // Time (in ms) to bake one pizza
    private final OrdersOutbox orderQueue;
    private final PizzaStorageIn warehouse;
    private final OrderStatusSink statusSink;
    private final DelayStrategy delayStrategy;

    public PizzaBaker(
            int id,
            int bakingSpeed,
            OrdersOutbox orderQueue,
            PizzaStorageIn warehouse,
            OrderStatusSink statusSink,
            DelayStrategy delayStrategy) {
        this.id = id;
        this.bakingSpeed = bakingSpeed;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.statusSink = statusSink;
        this.delayStrategy = delayStrategy;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                PizzaOrder order = orderQueue.takeOrder();
                if (order == null) {
                    break;
                }

                order.transitionTo(OrderStatus.BAKING);
                statusSink.emit(order);

                delayStrategy.pause(bakingSpeed);

                order.transitionTo(OrderStatus.RESERVING_WAREHOUSE_SLOT);
                statusSink.emit(order);

                warehouse.storePizza(order);

                order.transitionTo(OrderStatus.STORED);
                statusSink.emit(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String workerName() {
        return "Baker-" + id;
    }

    public int getId() {
        return id;
    }
}