package blockchain;

import java.util.Objects;
import lombok.Value;

@Value
public class Block {
    public static final int DIFFICULTY = 2;

    int height;
    String previousHash;
    String taskId;
    int taskIndex;
    int number;
    CheckResult result;
    String proof;
    String nodeId;
    long timestamp;
    long nonce;
    String hash;

    public static Block mine(int height, String previousHash, TaskDefinition task, CheckResult result,
                             String proof, String nodeId) {
        long timestamp = System.currentTimeMillis();
        long nonce = 0;
        while (true) {
            String hash = calculateHash(height, previousHash, task.getTaskId(), task.getIndex(),
                task.getNumber(), result, proof, nodeId, timestamp, nonce);
            if (hasValidWork(hash)) {
                return new Block(height, previousHash, task.getTaskId(), task.getIndex(),
                    task.getNumber(), result, proof, nodeId, timestamp, nonce, hash);
            }
            nonce++;
        }
    }

    static boolean hasValidWork(String hash) {
        return hash.startsWith("0".repeat(DIFFICULTY));
    }

    public String serialize() {
        return payload(height, previousHash, taskId, taskIndex, number, result, proof,
            nodeId, timestamp, nonce) + "|" + hash;
    }

    public static Block parse(String data) {
        String[] parts = data.split("\\|", -1);
        if (parts.length != 11) {
            throw new IllegalArgumentException("Invalid block field count");
        }
        return new Block(
            Integer.parseInt(parts[0]),
            parts[1],
            parts[2],
            Integer.parseInt(parts[3]),
            Integer.parseInt(parts[4]),
            CheckResult.valueOf(parts[5]),
            parts[6],
            parts[7],
            Long.parseLong(parts[8]),
            Long.parseLong(parts[9]),
            parts[10]
        );
    }

    public boolean hasConsistentHash() {
        return Objects.equals(hash, calculateHash(height, previousHash, taskId, taskIndex,
            number, result, proof, nodeId, timestamp, nonce));
    }

    private static String calculateHash(int height, String previousHash, String taskId, int taskIndex,
                                        int number, CheckResult result, String proof, String nodeId,
                                        long timestamp, long nonce) {
        return HashUtils.sha256(payload(height, previousHash, taskId, taskIndex, number,
            result, proof, nodeId, timestamp, nonce));
    }

    private static String payload(int height, String previousHash, String taskId, int taskIndex,
                                  int number, CheckResult result, String proof, String nodeId,
                                  long timestamp, long nonce) {
        return height + "|" + previousHash + "|" + taskId + "|" + taskIndex + "|"
            + number + "|" + result + "|" + proof + "|" + nodeId + "|" + timestamp + "|" + nonce;
    }
}
