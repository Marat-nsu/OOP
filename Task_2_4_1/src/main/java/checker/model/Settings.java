package checker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private int repositoryDownloadParallelism = 4;
    private int taskCheckThreadCount = Runtime.getRuntime().availableProcessors();

    public void addGradeThreshold(GradeThreshold t) {
        this.gradeThresholds.add(t);
    }

    public void addBonusPoints(String studentGithub, String taskId, int points) {
        bonusPoints.computeIfAbsent(studentGithub, k -> new HashMap<>()).put(taskId, points);
    }

    public int getBonusPoints(String studentGithub, String taskId) {
        return bonusPoints.getOrDefault(studentGithub, Map.of()).getOrDefault(taskId, 0);
    }

    public void setRepositoryDownloadParallelism(int v) {
        this.repositoryDownloadParallelism = Math.max(1, v);
    }

    public void setTaskCheckThreadCount(int v) {
        this.taskCheckThreadCount = Math.max(1, v);
    }

    public String computeGrade(double totalScore) {
        String grade = "-";
        for (GradeThreshold t : gradeThresholds) {
            if (totalScore >= t.getMinScore()) {
                grade = t.getGrade();
            }
        }
        return grade;
    }
}
