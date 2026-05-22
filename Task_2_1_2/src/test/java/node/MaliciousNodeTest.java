package node;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import blockchain.Block;
import blockchain.MaliciousNode;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class MaliciousNodeTest {
    @Test
    void honestNodeRejectsFakeProofsAndKeepsWorking() throws Exception {
        int[] numbers = {7, 9};

        try (BlockchainNode honest = new BlockchainNode("honest", 0, numbers)) {
            MaliciousNode malicious = new MaliciousNode("evil", numbers);

            Block fakeComposite = malicious.fakeCompositeBlock(0, 2);
            malicious.send(fakeComposite, "localhost", honest.getPort());

            Block fakePrime = malicious.fakePrimeBlock(1);
            malicious.send(fakePrime, "localhost", honest.getPort());

            waitForRejectedBlocks(honest, 2);

            assertTrue(honest.getRejectionLog().size() >= 2);

            honest.mineUntilFinished();

            assertTrue(honest.getBlockchain().containsNonPrime());
        }
    }

    private void waitForRejectedBlocks(BlockchainNode node, int count) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(2).toNanos();
        while (System.nanoTime() < deadline && node.getRejectionLog().size() < count) {
            Thread.sleep(10);
        }
    }
}
