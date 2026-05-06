package checker.engine;

public class StudentTaskResult {
    private boolean buildSuccess = false;
    private boolean docsSuccess = false;
    private boolean styleSuccess = false;
    private TestCounts testCounts = TestCounts.ZERO;
    private int baseScore = 0;
    private int bonusScore = 0;
    private String submissionDate = "";
    private String errorMessage = null;

    public boolean isBuildSuccess() {
        return buildSuccess;
    }
    
    public void setBuildSuccess(boolean v) {
        this.buildSuccess = v;
    }

    public boolean isDocsSuccess() {
        return docsSuccess;
    }

    public void setDocsSuccess(boolean v) {
        this.docsSuccess = v;
    }

    public boolean isStyleSuccess() {
        return styleSuccess;
    }

    public void setStyleSuccess(boolean v) {
        this.styleSuccess = v;
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

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String v) {
        this.submissionDate = v;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String v) {
        this.errorMessage = v;
    }
}
