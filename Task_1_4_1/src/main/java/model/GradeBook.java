package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Электронная зачетная книжка студента ФИТ.
 */
public class GradeBook {
    private final String studentName;
    private final boolean isPaidEducation;
    private final List<Semester> semesters;
    // Оценка за ВКР.
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

    public void setThesisGrade(Grade grade) {
        this.thesisGrade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public boolean isPaidEducation() {
        return isPaidEducation;
    }

    public List<Semester> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    public Grade getThesisGrade() {
        return thesisGrade;
    }

    public double calculateAverageGrade() {
        int totalGrade = 0;
        int count = 0;

        for (Semester semester : semesters) {
            for (Subject subject : semester.getSubjects()) {
                if (subject.getGrade().isGraded()) {
                    totalGrade += subject.getGrade().getValue();
                    count++;
                }
            }
        }

        return count > 0 ? (double) totalGrade / count : 0.0;
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
     */
    public boolean canGetRedDiploma() {
        List<Subject> diplomaSubjects = new ArrayList<>();

        for (Semester semester : semesters) {
            for (Subject subject : semester.getSubjects()) {
                if (subject.countsForDiploma()) {
                    diplomaSubjects.add(subject);
                }
            }
        }

        if (diplomaSubjects.isEmpty()) {
            return false;
        }

        int excellentCount = 0;
        int totalCount = 0;

        for (Subject subject : diplomaSubjects) {
            if (subject.getGrade() == Grade.SATISFACTORY) {
                return false;
            }
            if (subject.getGrade().isGraded()) {
                totalCount++;
                if (subject.getGrade() == Grade.EXCELLENT) {
                    excellentCount++;
                }
            }
        }

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

        for (Subject subject : lastSemester.getSubjects()) {
            Grade grade = subject.getGrade();
            if (grade.isGraded() && grade != Grade.EXCELLENT) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Зачетная книжка ===\n");
        sb.append("Студент: ").append(studentName).append("\n");
        sb.append("Форма обучения: ")
                .append(isPaidEducation ? "Платная" : "Бюджетная").append("\n");
        sb.append("Всего семестров: ").append(semesters.size()).append("\n\n");

        for (Semester semester : semesters) {
            sb.append(semester).append("\n");
            for (Subject subject : semester.getSubjects()) {
                sb.append("  ").append(subject).append("\n");
            }
            sb.append("\n");
        }

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
