package model;

public interface PizzaStorageOut {
    PizzaOrder takePizza() throws InterruptedException;

    PizzaOrder tryTakePizza();
}