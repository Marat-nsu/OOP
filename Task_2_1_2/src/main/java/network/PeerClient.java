package network;

import blockchain.Block;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import node.Peer;

public class PeerClient {
    public void sendBlock(Peer peer, Block block) throws IOException {
        try (Socket socket = new Socket(peer.getHost(), peer.getPort());
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            writer.write(Message.block(block.serialize()).serialize());
            writer.newLine();
            writer.flush();
        }
    }
}
