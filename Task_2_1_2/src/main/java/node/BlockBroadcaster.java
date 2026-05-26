package node;

import blockchain.Block;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import network.PeerClient;

class BlockBroadcaster {
    private final List<Peer> peers = Collections.synchronizedList(new ArrayList<>());
    private final PeerClient peerClient;
    private final Consumer<String> errorHandler;

    BlockBroadcaster(PeerClient peerClient, Consumer<String> errorHandler) {
        this.peerClient = peerClient;
        this.errorHandler = errorHandler;
    }

    void addPeer(String host, int port) {
        peers.add(new Peer(host, port));
    }

    void broadcast(Block block) {
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
            errorHandler.accept("network:peer unavailable " + peer.getHost() + ":" + peer.getPort());
        }
    }
}
