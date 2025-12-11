package model;

public class Subject {
    private final String name;
    private final SubjectType type;
    private final Grade grade;

    public Subject(String name, SubjectType type, Grade grade) {
        this.name = name;
        this.type = type;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public SubjectType getType() {
        return type;
    }

    public Grade getGrade() {
        return grade;
    }

    public boolean isExam() {
        return type == SubjectType.EXAM;
    }

    public boolean isDifferentiatedCredit() {
        return type == SubjectType.DIFFERENTIATED_CREDIT;
    }

    public boolean countsForDiploma() {
        return type == SubjectType.EXAM || type == SubjectType.DIFFERENTIATED_CREDIT;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", name, type, grade);
    }
}
