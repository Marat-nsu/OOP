package model;

public interface OrdersOutbox {
    PizzaOrder takeOrder() throws InterruptedException;
}