package blockchain;

import lombok.Getter;

@Getter
public class TaskDefinition {
    private final int index;
    private final int number;
    private final String taskId;

    TaskDefinition(int index, int number, String genesisHash) {
        this.index = index;
        this.number = number;
        this.taskId = taskId(index, number, genesisHash);
    }

    private static String taskId(int index, int number, String genesisHash) {
        return HashUtils.sha256(genesisHash + ":" + index + ":" + number);
    }
}
