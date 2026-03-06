package model;

public interface OrderStatusSink {
    void emit(PizzaOrder order);
}