package model;

public class ConsoleOrderStatusSink implements OrderStatusSink {
    @Override
    public void emit(PizzaOrder order) {
        System.out.println(order.formattedStatus());
    }
}