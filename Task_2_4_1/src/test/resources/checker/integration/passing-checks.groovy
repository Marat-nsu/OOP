tasks {
    task(id: "2_4_1", name: "Checker", maxScore: 4,
         softDeadline: "2026-12-30", hardDeadline: "2026-12-31")
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
    courseStartDate = "2026-05-01"
    courseEndDate = "2026-05-31"
    maxActivityBonus = 4
    grade(minScore: 4, value: "5")
}
