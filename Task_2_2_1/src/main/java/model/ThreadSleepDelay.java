package model;

import model.api.DelayStrategy;

public class ThreadSleepDelay implements DelayStrategy {
    @Override
    public void pause(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}