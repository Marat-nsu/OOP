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

                order.setStatus("Baking in progress by baker " + id);
                System.out.println(order.formattedStatus());

                Thread.sleep(bakingSpeed);

                order.setStatus("Baked by baker " + id);
                System.out.println(order.formattedStatus());

                warehouse.storePizza(order);

                order.setStatus("Stored in warehouse by baker " + id);
                System.out.println(order.formattedStatus());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Baker " + id + " interrupted.");
        }
    }

    public int getId() {
        return id;
    }
}