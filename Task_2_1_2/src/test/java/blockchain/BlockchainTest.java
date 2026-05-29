package blockchain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BlockchainTest {
    @Test
    void rejectsBlockWithInvalidPrimeProof() {
        Blockchain blockchain = new Blockchain(new int[] {9});
        TaskDefinition task = blockchain.findOpenTask();

        Block fake = blockchain.createNextBlock(task, CheckResult.PRIME, "3", "evil");

        assertFalse(blockchain.addBlock(fake).isAccepted());
    }

    @Test
    void rejectsBlockWithInvalidCompositeProof() {
        Blockchain blockchain = new Blockchain(new int[] {7});
        TaskDefinition task = blockchain.findOpenTask();

        Block fake = blockchain.createNextBlock(task, CheckResult.COMPOSITE, "2", "evil");

        assertFalse(blockchain.addBlock(fake).isAccepted());
    }

    @Test
    void acceptsValidCompositeBlockAndDerivesResult() {
        Blockchain blockchain = new Blockchain(new int[] {9});
        TaskDefinition task = blockchain.findOpenTask();

        Block valid = blockchain.createNextBlock(task, CheckResult.COMPOSITE, "3", "honest");

        assertTrue(blockchain.addBlock(valid).isAccepted());
        assertTrue(blockchain.containsNonPrime());
    }

    @Test
    void minerIdChangesPreferredOpenTask() {
        Blockchain blockchain = new Blockchain(new int[] {2, 3, 5, 7});

        TaskDefinition first = blockchain.findOpenTask("a");
        TaskDefinition second = blockchain.findOpenTask("b");

        assertNotEquals(first.getTaskId(), second.getTaskId());
    }

    @Test
    void rejectsDuplicateTaskInSameChain() {
        Blockchain blockchain = new Blockchain(new int[] {13});
        TaskDefinition task = blockchain.findOpenTask();
        Block first = blockchain.createNextBlock(task, CheckResult.PRIME, "3", "n1");
        assertTrue(blockchain.addBlock(first).isAccepted());

        Block duplicate = Block.mine(2, first.getHash(), task, CheckResult.PRIME, "3", "n2");

        assertFalse(blockchain.addBlock(duplicate).isAccepted());
    }
}
