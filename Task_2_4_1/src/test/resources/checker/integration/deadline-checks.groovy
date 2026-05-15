tasks {
    task(id: "2_4_1", name: "Checker", maxScore: 4,
         softDeadline: "2026-02-14", hardDeadline: "2026-02-21")
}

groups {
    group(name: "24214") {
        student(github: "student", name: "Student Name", repo: "%s")
    }
}

checks {
    check(task: "2_4_1", group: "24214")
}

settings {
    workDir = "%s"
}
