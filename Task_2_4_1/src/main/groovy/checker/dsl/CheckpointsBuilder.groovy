package checker.dsl

import checker.dsl.DslValidation
import checker.model.CheckpointConfig
import checker.model.CourseConfig

class CheckpointsBuilder {
    private final CourseConfig config

    CheckpointsBuilder(CourseConfig config) {
        this.config = config
    }

    void checkpoint(Map<String, Object> props) {
        def cp = new CheckpointConfig()
        if (props.name) {
            cp.name = props.name as String
        }
        if (props.date) {
            cp.date = props.date as String
        }
        DslValidation.requiredString(cp.name, "checkpoint.name")
        DslValidation.requiredString(cp.date, "checkpoint.date")
        config.addCheckpoint(cp)
    }

    void checkpoint(Closure cl) {
        def cp = new CheckpointConfig()
        cl.delegate = cp
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
        DslValidation.requiredString(cp.name, "checkpoint.name")
        DslValidation.requiredString(cp.date, "checkpoint.date")
        config.addCheckpoint(cp)
    }
}
