package checker.engine;

import checker.model.TaskConfig;
import java.time.LocalDate;

class ScoreCalculator {
    int scoreWithDeadlines(TaskConfig task, String submissionDate) {
        if (submissionDate == null || submissionDate.isBlank()) {
            return task.getMaxScore();
        }
        try {
            LocalDate submitted = LocalDate.parse(submissionDate);
            if (!task.getHardDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getHardDeadline()))) {
                return 0;
            }
            if (!task.getSoftDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getSoftDeadline()))) {
                return Math.max(1, task.getMaxScore() / 2);
            }
        } catch (Exception ignored) { }
        return task.getMaxScore();
    }
}
