package checker.report;

import checker.engine.CheckResults;
import checker.engine.StudentTaskResult;
import checker.model.*;

import java.time.LocalDate;
import java.util.*;

public class HtmlReporter {

    public String generateHtml(CourseConfig config, CheckResults results) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            <!DOCTYPE html>
            <html lang="ru">
            <head>
            <meta charset="UTF-8">
            <title>OOP Course Report</title>
            <style>
              body { font-family: monospace; padding: 20px; }
              table { border-collapse: collapse; margin-bottom: 24px; }
              th, td { border: 1px solid #aaa; padding: 6px 12px; text-align: left; }
              th { background: #eee; }
              h2 { margin-top: 32px; }
              h3 { margin-top: 20px; color: #333; }
              .pass { color: green; font-weight: bold; }
              .fail { color: red; }
              .err  { color: #c00; font-style: italic; font-size: 0.9em; }
            </style>
            </head>
            <body>
            """);

        Set<String> checkedGroups = new LinkedHashSet<>();
        Map<String, List<String>> groupToTasks = new LinkedHashMap<>();
        for (CheckEntry check : config.getChecks()) {
            checkedGroups.add(check.getGroupName());
            groupToTasks.computeIfAbsent(check.getGroupName(), k -> new ArrayList<>());
            if (!groupToTasks.get(check.getGroupName()).contains(check.getTaskId())) {
                groupToTasks.get(check.getGroupName()).add(check.getTaskId());
            }
        }

        for (String groupName : checkedGroups) {
            GroupConfig group = config.findGroup(groupName);
            if (group == null) {
                continue;
            }

            List<String> taskIds = groupToTasks.getOrDefault(groupName, List.of());

            sb.append("<h2>Группа ").append(esc(groupName)).append("</h2>\n");

            // Таблица по задачам
            for (String taskId : taskIds) {
                TaskConfig task = config.findTask(taskId);
                String taskName = task != null ? task.getName() : taskId;
                sb.append("<h3>").append(esc(taskId)).append(" — ")
                    .append(esc(taskName)).append("</h3>\n");
                sb.append("<table>\n");
                sb.append("<tr><th>Студент</th><th>Компиляция</th><th>Документация</th><th>Google Style</th>")
                  .append("<th>Тесты (прошли/упали/пропущены)</th><th>Сдача</th><th>Доп. балл</th><th>Балл</th></tr>\n");

                for (StudentConfig student : checkedStudentsForTask(config, group, taskId)) {
                    StudentTaskResult r = results.getTaskResults(groupName, taskId)
                        .get(student.getGithub());
                    if (r == null) {
                        r = new StudentTaskResult();
                    }
                    sb.append("<tr>");
                    sb.append("<td>").append(esc(student.getFullName())).append("</td>");
                    if (r.getErrorMessage() != null) {
                        sb.append("<td>").append(mark(r.isBuildSuccess())).append("</td>");
                        sb.append("<td>").append(mark(r.isDocsSuccess())).append("</td>");
                        sb.append("<td>").append(mark(r.isStyleSuccess())).append("</td>");
                        sb.append("<td colspan=\"4\" class=\"err\">")
                            .append(esc(r.getErrorMessage())).append("</td>");
                    } else {
                        sb.append("<td>").append(mark(r.isBuildSuccess())).append("</td>");
                        sb.append("<td>").append(mark(r.isDocsSuccess())).append("</td>");
                        sb.append("<td>").append(mark(r.isStyleSuccess())).append("</td>");
                        sb.append("<td>").append(esc(r.getTestCounts().toString())).append("</td>");
                        sb.append("<td>")
                            .append(esc(blankAsDash(r.getSubmissionDate())))
                            .append("</td>");
                        sb.append("<td>").append(score(r.getBonusScore())).append("</td>");
                        sb.append("<td>").append(score(r.getTotalScore())).append("</td>");
                    }
                    sb.append("</tr>\n");
                }
                sb.append("</table>\n");
            }

            // Общая таблица
            sb.append("<h3>Итого — ").append(esc(groupName)).append("</h3>\n");
            sb.append("<table>\n");
            sb.append("<tr><th>Студент</th>");
            for (String tid : taskIds) {
                sb.append("<th>").append(esc(tid)).append("</th>");
            }
            for (CheckpointConfig cp : config.getCheckpoints()) {
                sb.append("<th>").append(esc(cp.getName())).append("</th>");
            }
            sb.append("<th>Активность (нед.)</th><th>Бонус за активность</th><th>Сумма</th><th>Итоговая оценка</th></tr>\n");

            for (StudentConfig student : checkedStudentsForGroup(config, group)) {
                sb.append("<tr><td>").append(esc(student.getFullName())).append("</td>");
                double total = 0;
                for (String tid : taskIds) {
                    StudentTaskResult r = results.getTaskResults(groupName, tid)
                        .get(student.getGithub());
                    double taskScore = r != null ? r.getTotalScore() : 0;
                    sb.append("<td>").append(score(taskScore)).append("</td>");
                    total += taskScore;
                }
                for (CheckpointConfig cp : config.getCheckpoints()) {
                    double checkpointScore = checkpointScore(
                        config,
                        results,
                        groupName,
                        taskIds, 
                        student,
                        cp
                    );
                    sb.append("<td>")
                        .append(config.getSettings().computeGrade(checkpointScore)).append("</td>");
                }
                checker.engine.StudentActivity act = results.getActivity(student.getGithub());
                String activityLabel = act.getTotalWeeks() > 0
                    ? act.getActiveWeeks() + "/" + act.getTotalWeeks()
                    : "—";
                total += act.getActivityBonus();
                sb.append("<td>").append(activityLabel).append("</td>");
                sb.append("<td>").append(score(act.getActivityBonus())).append("</td>");
                sb.append("<td>").append(score(total)).append("</td>");
                sb.append("<td>").append(config.getSettings().computeGrade(total)).append("</td>");
                sb.append("</tr>\n");
            }
            sb.append("</table>\n");
        }

        sb.append("</body>\n</html>\n");
        return sb.toString();
    }

    private String mark(boolean ok) {
        return ok ? "<span class=\"pass\">+</span>" : "<span class=\"fail\">-</span>";
    }

    private List<StudentConfig> checkedStudentsForTask(CourseConfig config, GroupConfig group,
                                                       String taskId) {
        return group.getStudents().stream()
            .filter(student -> config.getChecks().stream()
                .filter(check -> check.getGroupName().equals(group.getName()))
                .filter(check -> check.getTaskId().equals(taskId))
                .anyMatch(check -> check.includesStudent(student)))
            .toList();
    }

    private List<StudentConfig> checkedStudentsForGroup(CourseConfig config, GroupConfig group) {
        return group.getStudents().stream()
            .filter(student -> config.getChecks().stream()
                .filter(check -> check.getGroupName().equals(group.getName()))
                .anyMatch(check -> check.includesStudent(student)))
            .toList();
    }

    private String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String blankAsDash(String s) {
        return s == null || s.isBlank() ? "-" : s;
    }

    private String score(double value) {
        return value == Math.rint(value)
            ? Long.toString(Math.round(value))
            : Double.toString(value);
    }

    private double checkpointScore(CourseConfig config, CheckResults results, String groupName,
                                List<String> taskIds, StudentConfig student, CheckpointConfig cp) {
        LocalDate checkpointDate;
        try {
            checkpointDate = LocalDate.parse(cp.getDate());
        } catch (Exception e) {
            return 0;
        }

        double score = 0;
        for (String taskId : taskIds) {
            TaskConfig task = config.findTask(taskId);
            if (task == null || task.getHardDeadline().isBlank()) {
                continue;
            }
            try {
                if (LocalDate.parse(task.getHardDeadline()).isAfter(checkpointDate)) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }
            StudentTaskResult r = results
                .getTaskResults(groupName, taskId).get(student.getGithub());
            score += r != null ? r.getTotalScore() : 0;
        }
        return score;
    }
}
