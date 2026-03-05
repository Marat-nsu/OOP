package model;

public class PizzaBaker implements Runnable {
    private final int id;
    private final int bakingSpeed; // Time (in ms) to bake one pizza
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;

    public PizzaBaker(int id, int bakingSpeed, OrderQueue orderQueue, Warehouse warehouse) {
        this.id = id;
        this.bakingSpeed = bakingSpeed;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                PizzaOrder order = orderQueue.takeOrder();
                if (order == null) {
                    break; // queue closed and drained
                }

                order.transitionTo(OrderStatus.BAKING);

                Thread.sleep(bakingSpeed);

                order.transitionTo(OrderStatus.RESERVING_WAREHOUSE_SLOT);

                warehouse.storePizza(order);

                order.transitionTo(OrderStatus.STORED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }
}