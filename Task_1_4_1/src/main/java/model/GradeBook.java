package model;


import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Электронная зачетная книжка студента ФИТ.
 */
@Getter
public class GradeBook {
    private final String studentName;
    private final boolean isPaidEducation;
    private final List<Semester> semesters;
    // Оценка за ВКР.
    @Setter
    private Grade thesisGrade;

    public GradeBook(String studentName, boolean isPaidEducation) {
        this.studentName = studentName;
        this.isPaidEducation = isPaidEducation;
        this.semesters = new ArrayList<>();
        this.thesisGrade = null;
    }

    public void addSemester(Semester semester) {
        semesters.add(semester);
    }

    public List<Semester> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    public double calculateAverageGrade() {
        return semesters.stream()
                .flatMap(semester -> semester.getSubjects().stream())
                .filter(subject -> subject.getGrade().isGraded())
                .mapToInt(subject -> subject.getGrade().getValue())
                .average()
                .orElse(0.0);
    }

    /**
     * Проверяет, может ли студент перевестись с платного на бюджетное обучение.
     * Требования: отсутствие оценок "удовлетворительно"
     * на экзаменах за последние два экзаменационных сессии.
     */
    public boolean canTransferToBudget() {
        if (!isPaidEducation) {
            return false;
        }

        if (semesters.size() < 2) {
            return false;
        }

        int size = semesters.size();
        Semester lastSemester = semesters.get(size - 1);
        Semester secondLastSemester = semesters.get(size - 2);

        return !lastSemester.hasSatisfactoryInExams()
                && !secondLastSemester.hasSatisfactoryInExams();
    }

    /**
     * Проверяет может ли студент получить красный диплом.
     * Требования:
     * 1. 75% итоговых оценок "отлично"
     * 2. Отсутствие оценок "удовлетворительно" 
     * в итоговых оценках (экзамены и дифференцированные зачеты)
     * 3. Оценка за ВКР "отлично" (если выполнена)
     * Примечание:
     * Итоговая оценка = последняя оценка за курс, если курс идет несколько семестров.
     */
    public boolean canGetRedDiploma() {
        Collection<Subject> diplomaSubjects = semesters.stream()
                .sorted(Comparator.comparingInt(Semester::getSemesterNumber))
                .flatMap(semester -> semester.getSubjects().stream())
                .filter(Subject::countsForDiploma)
                .collect(Collectors.toMap(
                        Subject::getName,
                        subject -> subject,
                        (existing, replacement) -> replacement
                ))
                .values();

        if (diplomaSubjects.isEmpty()) {
            return false;
        }

        if (diplomaSubjects.stream()
                .anyMatch(subject -> subject.getGrade() == Grade.SATISFACTORY)) {
            return false;
        }

        long totalCount = diplomaSubjects.stream()
                .filter(subject -> subject.getGrade().isGraded())
                .count();

        long excellentCount = diplomaSubjects.stream()
                .filter(subject -> subject.getGrade().isGraded())
                .filter(subject -> subject.getGrade() == Grade.EXCELLENT)
                .count();

        double excellentPercentage = totalCount > 0
                ? (double) excellentCount / totalCount : 0.0;
        boolean has75PercentExcellent = excellentPercentage >= 0.75;

        if (thesisGrade != null && thesisGrade != Grade.EXCELLENT) {
            return false;
        }

        return has75PercentExcellent;
    }

    /**
     * Проверяет может ли студент получить повышенную стипендию.
     * Требование: все оценки в последнем семестре "отлично".
     */
    public boolean canGetIncreasedScholarship() {
        if (isPaidEducation) {
            return false;
        }

        if (semesters.isEmpty()) {
            return false;
        }

        Semester lastSemester = semesters.get(semesters.size() - 1);

        return lastSemester.getSubjects().stream()
                .filter(subject -> subject.getGrade().isGraded())
                .allMatch(subject -> subject.getGrade() == Grade.EXCELLENT);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Зачетная книжка ===\n");
        sb.append("Студент: ").append(studentName).append("\n");
        sb.append("Форма обучения: ")
                .append(isPaidEducation ? "Платная" : "Бюджетная").append("\n");
        sb.append("Всего семестров: ").append(semesters.size()).append("\n\n");

        semesters.forEach(semester -> {
            sb.append(semester).append("\n");
            semester.getSubjects().forEach(subject -> 
                sb.append("  ").append(subject).append("\n")
            );
            sb.append("\n");
        });

        if (thesisGrade != null) {
            sb.append("Квалификационная работа: ").append(thesisGrade).append("\n\n");
        }

        sb.append("Средний балл: ").append(String.format("%.2f", calculateAverageGrade()))
                .append("\n");
        sb.append("Возможность перевода на бюджет: ")
                .append(canTransferToBudget() ? "Да" : "Нет").append("\n");
        sb.append("Возможность красного диплома: ")
                .append(canGetRedDiploma() ? "Да" : "Нет").append("\n");
        sb.append("Возможность повышенной стипендии: ")
                .append(canGetIncreasedScholarship() ? "Да" : "Нет").append("\n");

        return sb.toString();
    }
}
