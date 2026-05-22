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
import network.Message;
import network.MessageType;
import network.PeerClient;
import network.PeerServer;
import storage.BlockStorage;
import lombok.Getter;

public class BlockchainNode implements AutoCloseable {
    private final String nodeId;
    @Getter
    private final Blockchain blockchain;
    private final List<Peer> peers = Collections.synchronizedList(new ArrayList<>());
    private final List<String> rejectionLog = Collections.synchronizedList(new ArrayList<>());
    private final BlockStorage storage;
    private final PeerClient peerClient = new PeerClient();
    private PeerServer peerServer;

    public BlockchainNode(String nodeId, int port, int[] numbers) {
        this(nodeId, port, numbers, null);
    }

    public BlockchainNode(String nodeId, int port, int[] numbers, Path storagePath) {
        this.nodeId = nodeId;
        this.blockchain = new Blockchain(numbers);
        this.storage = storagePath == null ? null : new BlockStorage(storagePath);
        restoreBlocks();
        if (port >= 0) {
            startServer(port);
        }
    }

    public void addPeer(String host, int port) {
        peers.add(new Peer(host, port));
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
            broadcast(block);
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
        return peerServer == null ? 0 : peerServer.getPort();
    }

    @Override
    public void close() {
        if (peerServer != null) {
            peerServer.close();
        }
    }

    private void startServer(int port) {
        peerServer = new PeerServer(port, "node-" + nodeId, this::handleMessage, rejectionLog::add);
    }

    private void handleMessage(Message message) {
        if (message.getType() == MessageType.BLOCK) {
            try {
                receiveBlock(Block.parse(message.getPayload()));
            } catch (RuntimeException e) {
                rejectionLog.add("parse:" + e.getMessage());
            }
        }
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

    private void broadcast(Block block) {
        synchronized (peers) {
            for (Peer peer : peers) {
                sendBlock(peer, block);
            }
        }
    }

    private void sendBlock(Peer peer, Block block) {
        try {
            peerClient.sendBlock(peer, block);
        } catch (IOException ignored) {
            rejectionLog.add("network:peer unavailable " + peer.getHost() + ":" + peer.getPort());
        }
    }
}
