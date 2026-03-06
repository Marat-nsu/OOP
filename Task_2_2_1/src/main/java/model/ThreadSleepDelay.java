package model;

public class ThreadSleepDelay implements DelayStrategy {
    @Override
    public void pause(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}