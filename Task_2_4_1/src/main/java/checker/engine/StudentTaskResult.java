package checker.engine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentTaskResult {
    private boolean buildSuccess = false;
    private boolean docsSuccess = false;
    private boolean styleSuccess = false;
    private TestCounts testCounts = TestCounts.ZERO;
    private int baseScore = 0;
    private int bonusScore = 0;
    private String submissionDate = "";
    private String errorMessage = null;

    public int getTotalScore() {
        return baseScore + bonusScore;
    }
}
