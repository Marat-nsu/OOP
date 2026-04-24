package checker.dsl

import checker.model.CourseConfig
import checker.model.TaskConfig

class TasksBuilder {
    private final CourseConfig config

    TasksBuilder(CourseConfig config) {
        this.config = config
    }

    void task(Map<String, Object> props) {
        def t = new TaskConfig()
        if (props.id) {
            t.id = props.id as String
        }
        if (props.name) {
            t.name = props.name as String
        }
        if (props.maxScore != null) {
            t.maxScore = props.maxScore as int
        }
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
        config.addTask(t)
    }
}
