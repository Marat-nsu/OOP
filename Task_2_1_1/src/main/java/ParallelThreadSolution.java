import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelThreadSolution {
    public boolean containsNonPrime(int[] numbers, int threadCount) throws InterruptedException {
        int len = numbers.length;
        if (len == 0) {
            return false;
        }
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }

        final AtomicBoolean result = new AtomicBoolean(false);
        Thread[] threads = new Thread[threadCount];

        int chunkSize = (len + threadCount - 1) / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, len);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (result.get()) {
                        return;
                    }
                    if (!PrimeUtils.isPrime(numbers[j])) {
                        result.set(true);
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            if (t != null) {
                t.join();
            }
        }
        
        return result.get();
    }
}
