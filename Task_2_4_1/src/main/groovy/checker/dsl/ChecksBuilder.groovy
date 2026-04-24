package checker.dsl

import checker.model.CheckEntry
import checker.model.CourseConfig

class ChecksBuilder {
    private final CourseConfig config

    ChecksBuilder(CourseConfig config) {
        this.config = config
    }

    void check(Map<String, Object> props) {
        def entry = new CheckEntry()
        if (props.task) {
            entry.taskId = props.task as String
        }
        if (props.taskId) {
            entry.taskId = props.taskId as String
        }
        if (props.group) {
            entry.groupName = props.group as String
        }
        if (props.groupName) {
            entry.groupName = props.groupName as String
        }
        config.addCheck(entry)
    }

    void check(Closure cl) {
        def entry = new CheckEntry()
        cl.delegate = entry
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
        config.addCheck(entry)
    }
}
