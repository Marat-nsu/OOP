package checker.engine;

import checker.model.TaskConfig;
import java.time.LocalDate;

class ScoreCalculator {
    double scoreWithDeadlines(TaskConfig task, String firstCommitDate, String lastCommitDate) {
        if (firstCommitDate == null || firstCommitDate.isBlank()) {
            return task.getMaxScore();
        }
        try {
            boolean softFailed = !task.getSoftDeadline().isBlank()
                && LocalDate.parse(firstCommitDate).isAfter(LocalDate.parse(task.getSoftDeadline()));
            boolean hardFailed = !task.getHardDeadline().isBlank()
                && hardDeadlineDate(lastCommitDate, firstCommitDate)
                    .isAfter(LocalDate.parse(task.getHardDeadline()));
            if (softFailed && hardFailed) {
                return 0;
            }
            if (softFailed || hardFailed) {
                return Math.max(0, task.getMaxScore() - 0.5);
            }
        } catch (Exception ignored) { }
        return task.getMaxScore();
    }

    private LocalDate hardDeadlineDate(String lastCommitDate, String fallbackDate) {
        String date = lastCommitDate == null || lastCommitDate.isBlank()
            ? fallbackDate
            : lastCommitDate;
        return LocalDate.parse(date);
    }
}
