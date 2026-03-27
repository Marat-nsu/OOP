package model.api;

import model.PizzaOrder;

public interface PizzaStorageOut {
    PizzaOrder takePizza() throws InterruptedException;

    PizzaOrder tryTakePizza();
}
