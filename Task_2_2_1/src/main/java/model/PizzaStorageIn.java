package model;

public interface PizzaStorageIn {
    void storePizza(PizzaOrder order) throws InterruptedException;

    void close();
}