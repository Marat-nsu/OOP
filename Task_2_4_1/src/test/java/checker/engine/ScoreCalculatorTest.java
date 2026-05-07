package checker.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import checker.model.TaskConfig;
import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {
    private final ScoreCalculator calculator = new ScoreCalculator();

    @Test
    void givesFullScoreBeforeSoftDeadline() {
        assertEquals(2.0, calculator.scoreWithDeadlines(task(), "2026-02-14"));
    }

    @Test
    void subtractsHalfPointWhenOnlySoftDeadlineFailed() {
        assertEquals(1.5, calculator.scoreWithDeadlines(task(), "2026-02-20"));
    }

    @Test
    void returnsZeroWhenBothDeadlinesFailed() {
        assertEquals(0.0, calculator.scoreWithDeadlines(task(), "2026-02-22"));
    }

    private TaskConfig task() {
        TaskConfig task = new TaskConfig();
        task.setMaxScore(2);
        task.setSoftDeadline("2026-02-14");
        task.setHardDeadline("2026-02-21");
        return task;
    }
}
