package checker.dsl

import checker.model.CourseConfig

abstract class CourseScript extends Script {
    CourseConfig config = new CourseConfig()

    void tasks(Closure cl) {
        def builder = new TasksBuilder(config)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void groups(Closure cl) {
        def builder = new GroupsBuilder(config)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checks(Closure cl) {
        def builder = new ChecksBuilder(config)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void settings(Closure cl) {
        def builder = new SettingsBuilder(config.settings)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    CourseConfig getConfig() { config }
}
