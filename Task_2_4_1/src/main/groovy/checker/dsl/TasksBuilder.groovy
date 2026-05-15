package checker.dsl

import checker.dsl.DslValidation
import checker.model.CourseConfig
import checker.model.TaskConfig

class TasksBuilder {
    private final CourseConfig config

    TasksBuilder(CourseConfig config) {
        this.config = config
    }

    void task(Map<String, Object> props) {
        def t = new TaskConfig()
        t.id = DslValidation.requiredString(props.id, "task.id")
        if (props.name) {
            t.name = props.name as String
        }
        t.maxScore = DslValidation.requiredInt(props.maxScore, "task.maxScore")
        if (props.softDeadline) {
            t.softDeadline = props.softDeadline as String
        }
        if (props.hardDeadline) {
            t.hardDeadline = props.hardDeadline as String
        }
        config.addTask(t)
    }

    void task(Closure cl) {
        def t = new TaskConfig()
        cl.delegate = t
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
        DslValidation.requiredString(t.id, "task.id")
        config.addTask(t)
    }
}
