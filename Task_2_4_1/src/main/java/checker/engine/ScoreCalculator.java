package checker.engine;

import checker.model.TaskConfig;
import java.time.LocalDate;

class ScoreCalculator {
    double scoreWithDeadlines(TaskConfig task, String submissionDate) {
        if (submissionDate == null || submissionDate.isBlank()) {
            return task.getMaxScore();
        }
        try {
            LocalDate submitted = LocalDate.parse(submissionDate);
            boolean softFailed = !task.getSoftDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getSoftDeadline()));
            boolean hardFailed = !task.getHardDeadline().isBlank()
                && submitted.isAfter(LocalDate.parse(task.getHardDeadline()));
            if (softFailed && hardFailed) {
                return 0;
            }
            if (softFailed || hardFailed) {
                return Math.max(0, task.getMaxScore() - 0.5);
            }
        } catch (Exception ignored) { }
        return task.getMaxScore();
    }
}
