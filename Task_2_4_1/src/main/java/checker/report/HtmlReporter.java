package checker.report;

import checker.engine.CheckResults;
import checker.engine.StudentTaskResult;
import checker.model.*;

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
            groupToTasks.computeIfAbsent(check.getGroupName(), k -> new ArrayList<>())
                        .add(check.getTaskId());
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
                sb.append("<h3>").append(esc(taskId)).append(" — ").append(esc(taskName)).append("</h3>\n");
                sb.append("<table>\n");
                sb.append("<tr><th>Студент</th><th>Сборка</th><th>Тесты (прошли/упали/пропущены)</th><th>Доп. балл</th><th>Балл</th></tr>\n");

                for (StudentConfig student : group.getStudents()) {
                    StudentTaskResult r = results.getTaskResults(groupName, taskId).get(student.getGithub());
                    if (r == null) {
                        r = new StudentTaskResult();
                    }
                    sb.append("<tr>");
                    sb.append("<td>").append(esc(student.getFullName())).append("</td>");
                    if (r.getErrorMessage() != null) {
                        sb.append("<td>").append(mark(false)).append("</td>");
                        sb.append("<td colspan=\"3\" class=\"err\">").append(esc(r.getErrorMessage())).append("</td>");
                    } else {
                        sb.append("<td>").append(mark(r.isBuildSuccess())).append("</td>");
                        sb.append("<td>").append(esc(r.getTestCounts().toString())).append("</td>");
                        sb.append("<td>").append(r.getBonusScore()).append("</td>");
                        sb.append("<td>").append(r.getTotalScore()).append("</td>");
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
            sb.append("<th>Активность (нед.)</th><th>Бонус за активность</th><th>Сумма</th><th>Оценка</th></tr>\n");

            for (StudentConfig student : group.getStudents()) {
                sb.append("<tr><td>").append(esc(student.getFullName())).append("</td>");
                int total = 0;
                for (String tid : taskIds) {
                    StudentTaskResult r = results.getTaskResults(groupName, tid).get(student.getGithub());
                    int score = r != null ? r.getTotalScore() : 0;
                    sb.append("<td>").append(score).append("</td>");
                    total += score;
                }
                checker.engine.StudentActivity act = results.getActivity(student.getGithub());
                String activityLabel = act.getTotalWeeks() > 0
                    ? act.getActiveWeeks() + "/" + act.getTotalWeeks()
                    : "—";
                total += act.getActivityBonus();
                sb.append("<td>").append(activityLabel).append("</td>");
                sb.append("<td>").append(act.getActivityBonus()).append("</td>");
                sb.append("<td>").append(total).append("</td>");
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

    private String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
