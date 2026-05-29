package node;

import lombok.Value;

@Value
public class Peer {
    String host;
    int port;
}
