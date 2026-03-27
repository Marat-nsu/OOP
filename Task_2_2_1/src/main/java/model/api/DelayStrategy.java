package model.api;

public interface DelayStrategy {
    void pause(long millis) throws InterruptedException;
}
