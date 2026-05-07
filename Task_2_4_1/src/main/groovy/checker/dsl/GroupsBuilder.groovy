package checker.dsl

import checker.dsl.DslValidation
import checker.model.CourseConfig
import checker.model.GroupConfig
import checker.model.StudentConfig

class GroupsBuilder {
    private final CourseConfig config

    GroupsBuilder(CourseConfig config) {
        this.config = config
    }

    void group(Map<String, Object> props = [:], Closure cl) {
        def g = new GroupConfig()
        g.name = DslValidation.requiredString(props.name, "group.name")

        def studentAdder = new StudentAdder(g)
        cl.delegate = studentAdder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
        config.addGroup(g)
    }

    private static class StudentAdder {
        private final GroupConfig group

        StudentAdder(GroupConfig group) { this.group = group }

        void setName(String name) { group.name = name }

        void student(Map<String, Object> props) {
            def s = new StudentConfig()
            s.github = DslValidation.requiredString(props.github, "student.github")
            if (props.name) {
                s.fullName = props.name as String
            }
            if (props.fullName) {
                s.fullName = props.fullName as String
            }
            if (props.repo) {
                s.repoUrl = props.repo as String
            }
            if (props.repoUrl) {
                s.repoUrl = props.repoUrl as String
            }
            group.addStudent(s)
        }

        void student(Closure cl) {
            def s = new StudentConfig()
            cl.delegate = s
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            DslValidation.requiredString(s.github, "student.github")
            group.addStudent(s)
        }
    }
}
