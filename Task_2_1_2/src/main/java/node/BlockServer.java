package node;

import blockchain.Block;
import java.util.function.Consumer;
import network.Message;
import network.MessageType;
import network.PeerServer;

class BlockServer implements AutoCloseable {
    private final Consumer<Block> blockHandler;
    private final Consumer<String> errorHandler;
    private PeerServer peerServer;

    BlockServer(String nodeId, int port, Consumer<Block> blockHandler, Consumer<String> errorHandler) {
        this.blockHandler = blockHandler;
        this.errorHandler = errorHandler;
        peerServer = new PeerServer(port, "node-" + nodeId, this::handleMessage, errorHandler);
    }

    int getPort() {
        return peerServer.getPort();
    }

    @Override
    public void close() {
        if (peerServer != null) {
            peerServer.close();
        }
    }

    private void handleMessage(Message message) {
        if (message.getType() == MessageType.BLOCK) {
            try {
                blockHandler.accept(Block.parse(message.getPayload()));
            } catch (RuntimeException e) {
                errorHandler.accept("parse:" + e.getMessage());
            }
        }
    }
}
