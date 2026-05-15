tasks {
    task(id: "2_4_1", name: "Checker", maxScore: 4,
         softDeadline: "2026-12-30", hardDeadline: "2026-12-31")
}

groups {
    group(name: "24214") {
        student(github: "first", name: "First Student", repo: "%s")
        student(github: "second", name: "Second Student", repo: "%s")
    }
}

checks {
    check(task: "2_4_1", group: "24214")
}

settings {
    workDir = "%s"
    repositoryDownloadParallelism = 2
    taskCheckThreadCount = 2
    courseStartDate = "2026-05-01"
    courseEndDate = "2026-05-31"
}
