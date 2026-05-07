package checker.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StudentTaskResultTest {
    @Test
    void defaultsRepresentUncheckedTask() {
        StudentTaskResult result = new StudentTaskResult();

        assertFalse(result.isBuildSuccess());
        assertFalse(result.isDocsSuccess());
        assertFalse(result.isStyleSuccess());
        assertEquals(TestCounts.ZERO, result.getTestCounts());
        assertEquals(0.0, result.getTotalScore());
        assertNull(result.getErrorMessage());
    }

    @Test
    void totalScoreIncludesBaseAndBonus() {
        StudentTaskResult result = new StudentTaskResult();
        result.setBaseScore(3);
        result.setBonusScore(2);
        result.setBuildSuccess(true);
        result.setDocsSuccess(true);
        result.setStyleSuccess(true);

        assertEquals(5.0, result.getTotalScore());
        assertTrue(result.isBuildSuccess());
        assertTrue(result.isDocsSuccess());
        assertTrue(result.isStyleSuccess());
    }
}
