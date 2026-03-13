package model.api;

import model.PizzaOrder;

public interface OrderStatusSink {
    void emit(PizzaOrder order);
}
