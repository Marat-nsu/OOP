tasks {
    task(id: "2_1_1", name: "Map Task", maxScore: 1,
         softDeadline: "2026-02-14", hardDeadline: "2026-02-21")
    task {
        id = "2_2_1"
        name = "Closure Task"
        maxScore = 3
        softDeadline = "2026-03-07"
        hardDeadline = "2026-03-14"
    }
}

groups {
    group(name: "24214") {
        student(github: "mapStudent", name: "Map Student", repo: "https://example.com/map.git")
        student {
            github = "closureStudent"
            fullName = "Closure Student"
            repoUrl = "https://example.com/closure.git"
        }
    }
}

checks {
    check(task: "2_1_1", group: "24214")
    check {
        taskId = "2_2_1"
        groupName = "24214"
    }
}

checkpoints {
    checkpoint(name: "KT-1", date: "2026-03-01")
    checkpoint {
        name = "KT-2"
        date = "2026-04-01"
    }
}
