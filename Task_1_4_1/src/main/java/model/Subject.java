package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Subject {
    private final String name;
    private final SubjectType type;
    private final Grade grade;

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
