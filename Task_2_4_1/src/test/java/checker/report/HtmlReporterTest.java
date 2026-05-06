package checker.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import checker.engine.CheckResults;
import checker.engine.StudentActivity;
import checker.engine.StudentTaskResult;
import checker.engine.TestCounts;
import checker.model.CheckEntry;
import checker.model.CheckpointConfig;
import checker.model.CourseConfig;
import checker.model.GradeThreshold;
import checker.model.GroupConfig;
import checker.model.StudentConfig;
import checker.model.TaskConfig;
import org.junit.jupiter.api.Test;

class HtmlReporterTest {
    @Test
    void rendersTaskStagesActivityCheckpointsAndEscapesHtml() {
        CourseConfig config = courseConfig();
        CheckResults results = new CheckResults();
        StudentTaskResult taskResult = new StudentTaskResult();
        taskResult.setBuildSuccess(true);
        taskResult.setDocsSuccess(true);
        taskResult.setStyleSuccess(true);
        taskResult.setTestCounts(new TestCounts(3, 0, 1));
        taskResult.setBaseScore(2);
        taskResult.setBonusScore(1);
        taskResult.setSubmissionDate("2026-02-20");
        results.putResult("24214", "2_4_1", "student", taskResult);
        results.putActivity("student", new StudentActivity(2, 4, 1));

        String html = new HtmlReporter().generateHtml(config, results);

        assertTrue(html.contains("Компиляция"));
        assertTrue(html.contains("Документация"));
        assertTrue(html.contains("Google Style"));
        assertTrue(html.contains("3/0/1"));
        assertTrue(html.contains("2/4"));
        assertTrue(html.contains("КТ-1"));
        assertTrue(html.contains("Итоговая оценка"));
        assertTrue(html.contains("Student &lt;One&gt;"));
    }

    private CourseConfig courseConfig() {
        CourseConfig config = new CourseConfig();

        TaskConfig task = new TaskConfig();
        task.setId("2_4_1");
        task.setName("Checker");
        task.setMaxScore(2);
        task.setSoftDeadline("2026-02-14");
        task.setHardDeadline("2026-02-21");
        config.addTask(task);

        GroupConfig group = new GroupConfig();
        group.setName("24214");
        StudentConfig student = new StudentConfig();
        student.setGithub("student");
        student.setFullName("Student <One>");
        student.setRepoUrl("https://example.com/student.git");
        group.addStudent(student);
        config.addGroup(group);

        CheckEntry check = new CheckEntry();
        check.setTaskId("2_4_1");
        check.setGroupName("24214");
        config.addCheck(check);

        CheckpointConfig checkpoint = new CheckpointConfig();
        checkpoint.setName("КТ-1");
        checkpoint.setDate("2026-03-01");
        config.addCheckpoint(checkpoint);

        config.getSettings().addGradeThreshold(threshold(1, "3"));
        config.getSettings().addGradeThreshold(threshold(3, "4"));
        config.getSettings().addGradeThreshold(threshold(4, "5"));
        return config;
    }

    private GradeThreshold threshold(int minScore, String grade) {
        GradeThreshold threshold = new GradeThreshold();
        threshold.setMinScore(minScore);
        threshold.setGrade(grade);
        return threshold;
    }
}
