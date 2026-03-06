package model;

public interface OrdersInbox {
    void addOrder(PizzaOrder order);

    void close();
}