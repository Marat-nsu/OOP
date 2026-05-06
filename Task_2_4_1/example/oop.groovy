include "tasks.groovy"
include "groups.groovy"

checks {
    check(task: "2_1_1", group: "24214")
}

checkpoints {
    checkpoint(name: "КТ-1", date: "2026-03-01")
    checkpoint(name: "КТ-2", date: "2026-04-15")
    checkpoint(name: "Итог", date: "2026-06-20")
}

settings {
    workDir            = System.getProperty("user.home") + "/.oop-checker/repos"
    testTimeoutSeconds = 300
    courseStartDate    = "2026-02-10"
    courseEndDate      = "2026-06-20"
    maxActivityBonus   = 2

    grade(minScore: 3, value: "3")
    grade(minScore: 5, value: "4")
    grade(minScore: 8, value: "5")
}
