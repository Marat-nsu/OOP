package checker.dsl

import checker.ConfigLoader
import checker.model.CourseConfig

abstract class CourseScript extends Script {
    CourseConfig config = new CourseConfig()

    private CourseConfig cfg() {
        if (binding != null && binding.hasVariable("__config")) {
            config = binding.getVariable("__config") as CourseConfig
        }
        return config
    }

    void tasks(Closure cl) {
        def builder = new TasksBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void groups(Closure cl) {
        def builder = new GroupsBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checks(Closure cl) {
        def builder = new ChecksBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checkpoints(Closure cl) {
        def builder = new CheckpointsBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void settings(Closure cl) {
        def builder = new SettingsBuilder(cfg().settings)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void include(String relativePath) {
        File base = binding.hasVariable("__scriptDir")
            ? new File(binding.getVariable("__scriptDir") as String)
            : new File(".")
        ConfigLoader.loadInto(new File(base, relativePath), cfg())
    }

    void importConfig(String relativePath) {
        include(relativePath)
    }

    CourseConfig getConfig() { cfg() }
}
