package model.api;

import model.PizzaOrder;

public interface OrdersOutbox {
    PizzaOrder takeOrder() throws InterruptedException;
}
