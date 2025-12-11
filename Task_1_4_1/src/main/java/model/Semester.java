package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Semester {
    private final int semesterNumber;
    private final List<Subject> subjects;

    public Semester(int semesterNumber) {
        this.semesterNumber = semesterNumber;
        this.subjects = new ArrayList<>();
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public List<Subject> getExams() {
        List<Subject> exams = new ArrayList<>();
        for (Subject subject : subjects) {
            if (subject.isExam()) {
                exams.add(subject);
            }
        }
        return exams;
    }

    public boolean hasSatisfactoryInExams() {
        for (Subject subject : subjects) {
            if (subject.isExam() && subject.getGrade() == Grade.SATISFACTORY) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Семестр " + semesterNumber + ": " + subjects.size() + " предметов";
    }
}
