settings {
    workDir = "/tmp/oop-test"
    testTimeoutSeconds = 42
    javaHome = "/tmp/java"
    courseStartDate = "2026-02-10"
    courseEndDate = "2026-06-20"
    maxActivityBonus = 5
    repositoryDownloadParallelism = 0
    taskCheckThreadCount = 3

    grade(minScore: 3, value: "satisfactory")
    grade(minScore: 5, grade: "good")
    bonus(student: "student", task: "2_1_1", points: 2)
}
