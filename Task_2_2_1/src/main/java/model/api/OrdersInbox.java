package model.api;

import model.PizzaOrder;

public interface OrdersInbox {
    void addOrder(PizzaOrder order);

    void close();
}
