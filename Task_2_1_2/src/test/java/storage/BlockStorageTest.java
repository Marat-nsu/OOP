package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.CheckResult;
import blockchain.TaskDefinition;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BlockStorageTest {
    @TempDir
    Path tempDir;

    @Test
    void storesBlocksInAppendOnlyLogAndReadsThemBack() throws Exception {
        Blockchain blockchain = new Blockchain(new int[] {9});
        TaskDefinition task = blockchain.findOpenTask();
        Block block = blockchain.createNextBlock(task, CheckResult.COMPOSITE, "3", "node");
        Path file = tempDir.resolve("blocks.log");

        BlockStorage storage = new BlockStorage(file);
        storage.append(block);

        assertEquals(block.getHash(), storage.readAll().get(0).getHash());
    }
}
