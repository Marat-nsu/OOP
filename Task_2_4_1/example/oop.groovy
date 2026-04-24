tasks {
    task(id: "2_1_1", name: "Простые числа",          maxScore: 1,
         softDeadline: "2026-14-02", hardDeadline: "2026-21-02")
    task(id: "2_2_1", name: "Пиццерия",               maxScore: 2,
         softDeadline: "2026-03-07", hardDeadline: "2026-03-14")
    task(id: "2_3_1", name: "Змейка",                 maxScore: 2,
         softDeadline: "2026-04-03", hardDeadline: "2026-04-10")
    task(id: "2_4_1", name: "Автопроверка задач ООП", maxScore: 2,
         softDeadline: "2026-04-25", hardDeadline: "2026-05-16")
}

groups {
    group(name: "24214") {
        student(github: "NikRo12",           name: "Романенко Никита Сергеевич",     repo: "https://github.com/NikRo12/OOP")
        student(github: "chebupelka332-pro", name: "Токарев Максим Константинович",  repo: "https://github.com/chebupelka332-pro/OOP")
    }
}

checks {
    check(task: "2_1_1", group: "24214")
}

settings {
    workDir            = System.getProperty("user.home") + "/.oop-checker/repos"
    testTimeoutSeconds = 300
    courseStartDate    = "2026-02-10"
    courseEndDate      = "2026-06-20"

    grade(minScore: 3, value: "3")
    grade(minScore: 5, value: "4")
    grade(minScore: 8, value: "5")
}
