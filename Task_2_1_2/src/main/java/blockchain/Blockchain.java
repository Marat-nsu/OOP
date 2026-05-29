package blockchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Blockchain {
    private final String genesisHash;
    private final List<TaskDefinition> tasks = new ArrayList<>();
    private final Map<String, TaskDefinition> tasksById = new HashMap<>();
    private final Map<String, Block> blocksByHash = new HashMap<>();
    private final ProofValidator proofValidator = new ProofValidator();
    private String bestTipHash;

    public Blockchain(int[] numbers) {
        this.genesisHash = HashUtils.sha256(Arrays.toString(numbers));
        this.bestTipHash = genesisHash;
        for (int i = 0; i < numbers.length; i++) {
            TaskDefinition task = new TaskDefinition(i, numbers[i], genesisHash);
            tasks.add(task);
            tasksById.put(task.getTaskId(), task);
        }
    }

    public synchronized BlockValidationResult addBlock(Block block) {
        BlockValidationResult validation = validate(block);
        if (!validation.isAccepted()) {
            return validation;
        }
        blocksByHash.put(block.getHash(), block);
        if (isBetterTip(block.getHash(), bestTipHash)) {
            bestTipHash = block.getHash();
        }
        return BlockValidationResult.accepted();
    }

    public synchronized Block createNextBlock(TaskDefinition task, CheckResult result, String proof, String nodeId) {
        return Block.mine(heightOf(bestTipHash) + 1, bestTipHash, task, result, proof, nodeId);
    }

    public TaskDefinition findOpenTask() {
        return findOpenTask(null);
    }

    public synchronized TaskDefinition findOpenTask(String minerId) {
        Set<String> closed = closedTasks(bestChain());
        int startIndex = startIndexFor(minerId);
        for (int offset = 0; offset < tasks.size(); offset++) {
            TaskDefinition task = tasks.get((startIndex + offset) % tasks.size());
            if (!closed.contains(task.getTaskId())) {
                return task;
            }
        }
        return null;
    }

    public synchronized boolean containsNonPrime() {
        return bestChainContainsComposite();
    }

    public synchronized boolean isComplete() {
        return closedTasks(bestChain()).size() == tasks.size();
    }

    private List<Block> bestChain() {
        return chainTo(bestTipHash);
    }

    private boolean bestChainContainsComposite() {
        return bestChain().stream().anyMatch(block -> block.getResult() == CheckResult.COMPOSITE);
    }

    private List<Block> chainTo(String tipHash) {
        List<Block> result = new ArrayList<>();
        String cursor = tipHash;
        while (!cursor.equals(genesisHash)) {
            Block block = blocksByHash.get(cursor);
            if (block == null) {
                break;
            }
            result.add(0, block);
            cursor = block.getPreviousHash();
        }
        return result;
    }

    private BlockValidationResult validate(Block block) {
        if (blocksByHash.containsKey(block.getHash())) {
            return BlockValidationResult.rejected("duplicate block");
        }
        if (!block.hasConsistentHash()) {
            return BlockValidationResult.rejected("invalid hash");
        }
        if (!Block.hasValidWork(block.getHash())) {
            return BlockValidationResult.rejected("invalid proof of work");
        }
        if (!block.getPreviousHash().equals(genesisHash) && !blocksByHash.containsKey(block.getPreviousHash())) {
            return BlockValidationResult.rejected("unknown previous hash");
        }
        if (block.getHeight() != heightOf(block.getPreviousHash()) + 1) {
            return BlockValidationResult.rejected("invalid height");
        }
        TaskDefinition task = tasksById.get(block.getTaskId());
        if (task == null) {
            return BlockValidationResult.rejected("unknown task");
        }
        if (task.getIndex() != block.getTaskIndex() || task.getNumber() != block.getNumber()) {
            return BlockValidationResult.rejected("task payload mismatch");
        }
        if (closedTasks(chainTo(block.getPreviousHash())).contains(block.getTaskId())) {
            return BlockValidationResult.rejected("task already closed in this chain");
        }
        if (!proofValidator.isValid(block.getNumber(), block.getResult(), block.getProof())) {
            return BlockValidationResult.rejected("invalid primality proof");
        }
        return BlockValidationResult.accepted();
    }

    private boolean isBetterTip(String candidateHash, String currentHash) {
        int candidateHeight = heightOf(candidateHash);
        int currentHeight = heightOf(currentHash);
        if (candidateHeight != currentHeight) {
            return candidateHeight > currentHeight;
        }
        return candidateHash.compareTo(currentHash) < 0;
    }

    private int heightOf(String hash) {
        if (hash.equals(genesisHash)) {
            return 0;
        }
        Block block = blocksByHash.get(hash);
        return block == null ? -1 : block.getHeight();
    }

    private int startIndexFor(String minerId) {
        if (tasks.isEmpty() || minerId == null || minerId.isEmpty()) {
            return 0;
        }
        return Math.floorMod(minerId.hashCode(), tasks.size());
    }

    private Set<String> closedTasks(List<Block> chain) {
        Set<String> closed = new HashSet<>();
        for (Block block : chain) {
            closed.add(block.getTaskId());
        }
        return closed;
    }
}
