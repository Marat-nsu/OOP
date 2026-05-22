package blockchain;

import lombok.Getter;

@Getter
public class BlockValidationResult {
    private final boolean accepted;
    private final String reason;

    public static BlockValidationResult accepted() {
        return new BlockValidationResult(true, "accepted");
    }

    public static BlockValidationResult rejected(String reason) {
        return new BlockValidationResult(false, reason);
    }

    private BlockValidationResult(boolean accepted, String reason) {
        this.accepted = accepted;
        this.reason = reason;
    }
}
