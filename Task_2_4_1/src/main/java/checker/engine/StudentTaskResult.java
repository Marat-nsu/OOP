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
    private double baseScore = 0;
    private double bonusScore = 0;
    private String submissionDate = "";
    private String lastSubmissionDate = "";
    private String errorMessage = null;

    public double getTotalScore() {
        return baseScore + bonusScore;
    }
}
