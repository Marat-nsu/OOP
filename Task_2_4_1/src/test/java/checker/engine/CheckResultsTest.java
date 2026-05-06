package checker.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class CheckResultsTest {
    @Test
    void storesTaskResultsByGroupTaskAndStudent() {
        CheckResults results = new CheckResults();
        StudentTaskResult taskResult = new StudentTaskResult();
        taskResult.setBaseScore(2);

        results.putResult("24214", "2_4_1", "student", taskResult);

        assertSame(taskResult, results.getTaskResults("24214", "2_4_1").get("student"));
        assertEquals(2, results.getTaskResults("24214", "2_4_1").get("student").getTotalScore());
    }

    @Test
    void returnsUnknownActivityWhenStudentWasNotAnalyzed() {
        CheckResults results = new CheckResults();

        StudentActivity activity = results.getActivity("missing");

        assertEquals(0, activity.getActiveWeeks());
        assertEquals(0, activity.getTotalWeeks());
        assertEquals(0, activity.getActivityBonus());
    }
}
