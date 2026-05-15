package checker.engine;

import checker.model.Settings;
import checker.model.StudentConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ActivityAnalyzer {
    private final Settings settings;
    private final Path workDir;
    private final CommandRunner commands;

    ActivityAnalyzer(Settings settings, Path workDir, CommandRunner commands) {
        this.settings = settings;
        this.workDir = workDir;
        this.commands = commands;
    }

    StudentActivity analyze(StudentConfig student) {
        String startStr = settings.getCourseStartDate();
        String endStr = settings.getCourseEndDate();
        if (startStr == null || endStr == null || startStr.isBlank() || endStr.isBlank()) {
            return StudentActivity.UNKNOWN;
        }

        LocalDate courseStart;
        LocalDate courseEnd;
        try {
            courseStart = LocalDate.parse(startStr);
            courseEnd = LocalDate.parse(endStr);
        } catch (Exception e) {
            return StudentActivity.UNKNOWN;
        }

        Path repoPath = workDir.resolve(student.getGithub());
        if (!Files.exists(repoPath.resolve(".git"))) {
            return StudentActivity.UNKNOWN;
        }

        Set<String> activeWeekKeys = activeWeeks(repoPath, courseStart, courseEnd);
        Set<String> totalWeekKeys = totalWeeks(courseStart, courseEnd);
        int activeWeeks = activeWeekKeys.size();
        int totalWeeks = totalWeekKeys.size();
        int activityBonus = totalWeeks > 0
            ? (settings.getMaxActivityBonus() * activeWeeks / totalWeeks)
            : 0;

        return new StudentActivity(activeWeeks, totalWeeks, activityBonus);
    }

    private Set<String> activeWeeks(Path repoPath, LocalDate courseStart, LocalDate courseEnd) {
        List<String> commitDates = new ArrayList<>();
        try {
            ProcessResult pr = commands.run(repoPath, 30,
                "git", "log", "--format=%ad", "--date=short");
            if (pr.exitCode() == 0) {
                for (String line : pr.output().split("\n")) {
                    if (!line.isBlank()) {
                        commitDates.add(line.trim());
                    }
                }
            }
        } catch (Exception e) {
            return Set.of();
        }

        Set<String> activeWeekKeys = new HashSet<>();
        for (String dateStr : commitDates) {
            try {
                LocalDate date = LocalDate.parse(dateStr);
                if (!date.isBefore(courseStart) && !date.isAfter(courseEnd)) {
                    activeWeekKeys.add(weekKey(date));
                }
            } catch (Exception ignored) { }
        }
        return activeWeekKeys;
    }

    private Set<String> totalWeeks(LocalDate courseStart, LocalDate courseEnd) {
        Set<String> totalWeekKeys = new HashSet<>();
        LocalDate date = courseStart;
        while (!date.isAfter(courseEnd)) {
            totalWeekKeys.add(weekKey(date));
            date = date.plusWeeks(1);
        }
        return totalWeekKeys;
    }

    private String weekKey(LocalDate date) {
        int year = date.get(IsoFields.WEEK_BASED_YEAR);
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return year + "-W" + week;
    }
}
