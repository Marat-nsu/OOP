package node;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class ParallelMiningIntegrationTest {
    @Test
    void honestNodesCanMineInParallel() throws Exception {
        int[] numbers = {9};

        try (BlockchainNode first = new BlockchainNode("first", 0, numbers);
             BlockchainNode second = new BlockchainNode("second", 0, numbers)) {
            first.addPeer("localhost", second.getPort());
            second.addPeer("localhost", first.getPort());

            runInParallel(first::mineUntilFinished, second::mineUntilFinished);

            assertTrue(first.getBlockchain().containsNonPrime());
            assertTrue(second.getBlockchain().containsNonPrime());
        }
    }

    private void runInParallel(Runnable firstTask, Runnable secondTask) throws InterruptedException {
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Thread firstThread = new Thread(capturing(firstTask, failure), "first-miner");
        Thread secondThread = new Thread(capturing(secondTask, failure), "second-miner");

        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();

        if (failure.get() != null) {
            throw new AssertionError("Parallel mining failed", failure.get());
        }
    }

    private Runnable capturing(Runnable task, AtomicReference<Throwable> failure) {
        return () -> {
            try {
                task.run();
            } catch (Throwable throwable) {
                failure.compareAndSet(null, throwable);
            }
        };
    }
}
