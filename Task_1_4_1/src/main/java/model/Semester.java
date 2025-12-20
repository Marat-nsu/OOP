package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class Semester {
    private final int semesterNumber;
    private final List<Subject> subjects;

    public Semester(int semesterNumber) {
        this.semesterNumber = semesterNumber;
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public List<Subject> getExams() {
        return subjects.stream()
                .filter(Subject::isExam)
                .toList();
    }

    public boolean hasSatisfactoryInExams() {
        return subjects.stream()
                .anyMatch(subject -> subject.isExam() && subject.getGrade() == Grade.SATISFACTORY);
    }

    @Override
    public String toString() {
        return "Семестр " + semesterNumber + ": " + subjects.size() + " предметов";
    }
}
