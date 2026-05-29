package node;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class NodeStorageRestoreTest {
    @TempDir
    Path tempDir;

    @Test
    void restoresAcceptedBlocksFromDisk() {
        int[] numbers = {9};
        Path log = tempDir.resolve("blocks.log");

        BlockchainNode first = new BlockchainNode("first", -1, numbers, log);
        first.mineUntilFinished();

        BlockchainNode restored = new BlockchainNode("restored", -1, numbers, log);

        assertTrue(restored.getBlockchain().containsNonPrime());
    }
}
