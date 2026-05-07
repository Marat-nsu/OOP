package checker.dsl

import checker.dsl.DslValidation
import checker.model.GradeThreshold
import checker.model.Settings

class SettingsBuilder {
    private final Settings settings

    SettingsBuilder(Settings settings) {
        this.settings = settings
    }

    void setWorkDir(String dir) { settings.workDir = dir }
    void setTestTimeoutSeconds(int seconds) { settings.testTimeoutSeconds = seconds }
    void setJavaHome(String path) { settings.javaHome = path }
    void setCourseStartDate(String d) { settings.courseStartDate = d }
    void setCourseEndDate(String d) { settings.courseEndDate = d }
    void setMaxActivityBonus(int v) { settings.maxActivityBonus = v }
    void setRepositoryDownloadParallelism(int v) { settings.repositoryDownloadParallelism = v }

    void grade(Map<String, Object> props) {
        def t = new GradeThreshold()
        t.minScore = DslValidation.requiredInt(props.minScore, "grade.minScore")
        if (props.value) {
            t.grade = props.value as String
        }
        if (props.grade) {
            t.grade = props.grade as String
        }
        DslValidation.requiredString(t.grade, "grade.value")
        settings.addGradeThreshold(t)
    }

    void bonus(Map<String, Object> props) {
        settings.addBonusPoints(
            DslValidation.requiredString(props.student, "bonus.student"),
            DslValidation.requiredString(props.task, "bonus.task"),
            DslValidation.requiredInt(props.points, "bonus.points")
        )
    }
}
