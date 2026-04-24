package checker.model;

public class TaskConfig {
    private String id = "";
    private String name = "";
    private int maxScore = 1;
    private String softDeadline = "";
    private String hardDeadline = "";

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getSoftDeadline() {
        return softDeadline;
    }
    
    public void setSoftDeadline(String softDeadline) {
        this.softDeadline = softDeadline;
    }

    public String getHardDeadline() {
        return hardDeadline;
    }
    
    public void setHardDeadline(String hardDeadline) {
        this.hardDeadline = hardDeadline;
    }
}
