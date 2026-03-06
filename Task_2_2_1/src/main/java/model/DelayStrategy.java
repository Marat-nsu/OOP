package model;

public interface DelayStrategy {
    void pause(long millis) throws InterruptedException;
}