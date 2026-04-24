package checker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    private String workDir = System.getProperty("java.io.tmpdir") + "/oop-checker";
    private int testTimeoutSeconds = 300;
    /** JAVA_HOME для запуска студенческих gradle-сборок (null = системный). */
    private String javaHome = null;
    private List<GradeThreshold> gradeThresholds = new ArrayList<>();
    /** studentGithub, taskId, bonus points. */
    private Map<String, Map<String, Integer>> bonusPoints = new HashMap<>();
    private String courseStartDate = null;
    private String courseEndDate = null;
    /** Max bonus points awarded for 100% weekly activity. */
    private int maxActivityBonus = 0;

    public String getWorkDir() {
        return workDir;
    }
    
    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public int getTestTimeoutSeconds() {
        return testTimeoutSeconds;
    }
    
    public void setTestTimeoutSeconds(int v) {
        this.testTimeoutSeconds = v;
    }

    public String getJavaHome() {
        return javaHome;
    }
    
    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public List<GradeThreshold> getGradeThresholds() {
        return gradeThresholds;
    }
    
    public void addGradeThreshold(GradeThreshold t) {
        this.gradeThresholds.add(t);
    }

    public void addBonusPoints(String studentGithub, String taskId, int points) {
        bonusPoints.computeIfAbsent(studentGithub, k -> new HashMap<>()).put(taskId, points);
    }

    public int getBonusPoints(String studentGithub, String taskId) {
        return bonusPoints.getOrDefault(studentGithub, Map.of()).getOrDefault(taskId, 0);
    }

    public String getCourseStartDate() { return courseStartDate; }
    public void setCourseStartDate(String d) { this.courseStartDate = d; }

    public String getCourseEndDate() { return courseEndDate; }
    public void setCourseEndDate(String d) { this.courseEndDate = d; }

    public int getMaxActivityBonus() { return maxActivityBonus; }
    public void setMaxActivityBonus(int v) { this.maxActivityBonus = v; }

    public String computeGrade(int totalScore) {
        String grade = "-";
        for (GradeThreshold t : gradeThresholds) {
            if (totalScore >= t.getMinScore()) {
                grade = t.getGrade();
            }
        }
        return grade;
    }
}
