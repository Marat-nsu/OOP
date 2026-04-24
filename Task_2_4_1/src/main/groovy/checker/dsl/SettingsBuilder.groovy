package checker.dsl

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

    void grade(Map<String, Object> props) {
        def t = new GradeThreshold()
        if (props.minScore != null) {
            t.minScore = props.minScore as int
        }
        if (props.value) {
            t.grade = props.value as String
        }
        if (props.grade) {
            t.grade = props.grade as String
        }
        settings.addGradeThreshold(t)
    }

    void bonus(Map<String, Object> props) {
        if (props.student && props.task && props.points != null) {
            settings.addBonusPoints(
                props.student as String,
                props.task as String,
                props.points as int
            )
        }
    }
}
