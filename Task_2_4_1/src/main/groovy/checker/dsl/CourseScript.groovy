package checker.dsl

import checker.ConfigLoader
import checker.dsl.DslConfigException
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
        requireClosure(cl, "tasks")
        def builder = new TasksBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void groups(Closure cl) {
        requireClosure(cl, "groups")
        def builder = new GroupsBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checks(Closure cl) {
        requireClosure(cl, "checks")
        def builder = new ChecksBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checkpoints(Closure cl) {
        requireClosure(cl, "checkpoints")
        def builder = new CheckpointsBuilder(cfg())
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void settings(Closure cl) {
        requireClosure(cl, "settings")
        def builder = new SettingsBuilder(cfg().settings)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void include(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            throw new DslConfigException("include path must not be blank")
        }
        File base = binding.hasVariable("__scriptDir")
            ? new File(binding.getVariable("__scriptDir") as String)
            : new File(".")
        ConfigLoader.loadInto(new File(base, relativePath), cfg())
    }

    void importConfig(String relativePath) {
        include(relativePath)
    }

    private void requireClosure(Closure cl, String blockName) {
        if (cl == null) {
            throw new DslConfigException("DSL block '" + blockName + "' requires a closure")
        }
    }

    CourseConfig getConfig() { cfg() }
}
