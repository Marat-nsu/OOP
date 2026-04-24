package checker.model;

public class GradeThreshold {
    private int minScore = 0;
    private String grade = "";

    public int getMinScore() { 
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public String getGrade() { 
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
