tasks {
    task(id: "2_1_1", name: "Task", maxScore: 1)
}

groups {
    group(name: "24214") {
        student(github: "full", fullName: "Full Name", repoUrl: "https://example.com/full.git")
        student(github: "short", name: "Short Name", repo: "https://example.com/short.git")
    }
}

checks {
    check(taskId: "2_1_1", groupName: "24214")
    check(task: "2_1_1", group: "24214", students: ["full", "short"])
}
