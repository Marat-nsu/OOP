package checker.engine;

public class StudentTaskResult {
    private boolean buildSuccess = false;
    private TestCounts testCounts = TestCounts.ZERO;
    private int baseScore = 0;
    private int bonusScore = 0;
    private String errorMessage = null;

    public boolean isBuildSuccess() {
        return buildSuccess;
    }
    
    public void setBuildSuccess(boolean v) {
        this.buildSuccess = v;
    }

    public TestCounts getTestCounts() {
        return testCounts;
    }
    
    public void setTestCounts(TestCounts v) {
        this.testCounts = v;
    }

    public int getBaseScore() {
        return baseScore;
    }
    
    public void setBaseScore(int v) {
        this.baseScore = v;
    }

    public int getBonusScore() {
        return bonusScore;
    }
    
    public void setBonusScore(int v) {
        this.bonusScore = v;
    }

    public int getTotalScore() {
        return baseScore + bonusScore;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String v) {
        this.errorMessage = v;
    }
}
