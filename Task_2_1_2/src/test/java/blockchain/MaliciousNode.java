package blockchain;

import java.io.IOException;
import java.util.Arrays;
import network.PeerClient;
import node.Peer;
import primes.PrimeUtils;

public class MaliciousNode {
    private final String nodeId;
    private final int[] numbers;
    private final Blockchain view;
    private final PeerClient peerClient = new PeerClient();

    public MaliciousNode(String nodeId, int[] numbers) {
        this.nodeId = nodeId;
        this.numbers = Arrays.copyOf(numbers, numbers.length);
        this.view = new Blockchain(numbers);
    }

    public Block fakePrimeBlock(int taskIndex) {
        TaskDefinition task = taskAt(taskIndex);
        return view.createNextBlock(task, CheckResult.PRIME,
            String.valueOf(PrimeUtils.sqrtFloor(task.getNumber())), nodeId);
    }

    public Block fakeCompositeBlock(int taskIndex, int fakeDivisor) {
        TaskDefinition task = taskAt(taskIndex);
        return view.createNextBlock(task, CheckResult.COMPOSITE, String.valueOf(fakeDivisor), nodeId);
    }

    public void send(Block block, String host, int port) throws IOException {
        peerClient.sendBlock(new Peer(host, port), block);
    }

    private TaskDefinition taskAt(int taskIndex) {
        return new TaskDefinition(taskIndex, numbers[taskIndex], HashUtils.sha256(Arrays.toString(numbers)));
    }
}
