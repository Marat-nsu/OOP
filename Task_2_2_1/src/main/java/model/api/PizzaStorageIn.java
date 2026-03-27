package model.api;

import model.PizzaOrder;

public interface PizzaStorageIn {
    void storePizza(PizzaOrder order) throws InterruptedException;

    void close();
}
