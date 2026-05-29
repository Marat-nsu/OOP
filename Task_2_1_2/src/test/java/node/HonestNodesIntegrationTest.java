package node;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class HonestNodesIntegrationTest {
    @Test
    void honestNodesShareValidBlocksOverTcp() throws Exception {
        int[] numbers = {9};

        try (BlockchainNode first = new BlockchainNode("first", 0, numbers);
             BlockchainNode second = new BlockchainNode("second", 0, numbers)) {
            first.addPeer("localhost", second.getPort());

            first.mineOneBlock();

            waitForResult(second);

            assertTrue(first.getBlockchain().containsNonPrime());
            assertTrue(second.getBlockchain().containsNonPrime());
        }
    }

    private void waitForResult(BlockchainNode node) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(2).toNanos();
        while (System.nanoTime() < deadline && !node.getBlockchain().containsNonPrime()) {
            Thread.sleep(10);
        }
    }
}
