package node;

import blockchain.Block;
import blockchain.BlockValidationResult;
import blockchain.Blockchain;
import blockchain.PrimalityProof;
import blockchain.TaskDefinition;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import network.PeerClient;
import storage.BlockStorage;

public class BlockchainNode implements AutoCloseable {
    private final String nodeId;
    @Getter
    private final Blockchain blockchain;
    private final List<String> rejectionLog = Collections.synchronizedList(new ArrayList<>());
    private final BlockStorage storage;
    private final BlockBroadcaster blockBroadcaster;
    private BlockServer blockServer;

    public BlockchainNode(String nodeId, int port, int[] numbers) {
        this(nodeId, port, numbers, null);
    }

    public BlockchainNode(String nodeId, int port, int[] numbers, Path storagePath) {
        this.nodeId = nodeId;
        this.blockchain = new Blockchain(numbers);
        this.storage = storagePath == null ? null : new BlockStorage(storagePath);
        this.blockBroadcaster = new BlockBroadcaster(new PeerClient(), rejectionLog::add);
        restoreBlocks();
        if (port >= 0) {
            startServer(port);
        }
    }

    public void addPeer(String host, int port) {
        blockBroadcaster.addPeer(host, port);
    }

    public void mineUntilFinished() {
        while (!blockchain.containsNonPrime() && !blockchain.isComplete()) {
            mineOneBlock();
        }
    }

    public Block mineOneBlock() {
        TaskDefinition task = blockchain.findOpenTask();
        if (task == null) {
            return null;
        }
        PrimalityProof primalityProof = PrimalityProof.forNumber(task.getNumber());
        Block block = blockchain.createNextBlock(task, primalityProof.getResult(), primalityProof.getProof(), nodeId);
        BlockValidationResult validation = blockchain.addBlock(block);
        if (validation.isAccepted()) {
            store(block);
            blockBroadcaster.broadcast(block);
            return block;
        }
        rejectionLog.add(block.getHash() + ":" + validation.getReason());
        return null;
    }

    public BlockValidationResult receiveBlock(Block block) {
        BlockValidationResult validation = blockchain.addBlock(block);
        if (validation.isAccepted()) {
            store(block);
        }
        if (!validation.isAccepted()) {
            rejectionLog.add(block.getHash() + ":" + validation.getReason());
        }
        return validation;
    }

    public List<String> getRejectionLog() {
        return List.copyOf(rejectionLog);
    }

    public int getPort() {
        return blockServer == null ? 0 : blockServer.getPort();
    }

    @Override
    public void close() {
        if (blockServer != null) {
            blockServer.close();
        }
    }

    private void startServer(int port) {
        blockServer = new BlockServer(nodeId, port, this::receiveBlock, rejectionLog::add);
    }

    private void restoreBlocks() {
        if (storage == null) {
            return;
        }
        try {
            for (Block block : storage.readAll()) {
                BlockValidationResult validation = blockchain.addBlock(block);
                if (!validation.isAccepted()) {
                    rejectionLog.add(block.getHash() + ":" + validation.getReason());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to restore blocks", e);
        }
    }

    private void store(Block block) {
        if (storage == null) {
            return;
        }
        try {
            storage.append(block);
        } catch (IOException e) {
            rejectionLog.add(block.getHash() + ":storage failed");
        }
    }

}
