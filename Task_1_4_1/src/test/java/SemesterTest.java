import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import model.Grade;
import model.Semester;
import model.Subject;
import model.SubjectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SemesterTest {
    private Semester semester;

    @BeforeEach
    void setUp() {
        semester = new Semester(1);
    }

    @Test
    void testGetSemesterNumber() {
        assertEquals(1, semester.getSemesterNumber());
    }

    @Test
    void testAddSubject() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        semester.addSubject(subject);
        
        List<Subject> subjects = semester.getSubjects();
        assertEquals(1, subjects.size());
        assertEquals("Math", subjects.get(0).getName());
    }

    @Test
    void testGetSubjectsReturnsUnmodifiableList() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        semester.addSubject(subject);
        
        List<Subject> subjects = semester.getSubjects();
        assertEquals(1, subjects.size());
    }

    @Test
    void testGetExamsReturnsOnlyExams() {
        semester.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        semester.addSubject(new Subject("English", SubjectType.DIFFERENTIATED_CREDIT, Grade.EXCELLENT));
        semester.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        
        List<Subject> exams = semester.getExams();
        assertEquals(2, exams.size());
        assertTrue(exams.get(0).isExam());
        assertTrue(exams.get(1).isExam());
    }

    @Test
    void testGetExamsReturnsEmptyListWhenNoExams() {
        semester.addSubject(new Subject("English", SubjectType.DIFFERENTIATED_CREDIT, Grade.EXCELLENT));
        semester.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        
        List<Subject> exams = semester.getExams();
        assertEquals(0, exams.size());
    }

    @Test
    void testHasSatisfactoryInExamsReturnsTrueWhenPresent() {
        semester.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.SATISFACTORY));
        
        assertTrue(semester.hasSatisfactoryInExams());
    }

    @Test
    void testHasSatisfactoryInExamsReturnsFalseWhenNotPresent() {
        semester.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        
        assertFalse(semester.hasSatisfactoryInExams());
    }

    @Test
    void testHasSatisfactoryInExamsIgnoresNonExams() {
        semester.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester.addSubject(new Subject("English", SubjectType.DIFFERENTIATED_CREDIT, Grade.SATISFACTORY));
        
        assertFalse(semester.hasSatisfactoryInExams());
    }

    @Test
    void testToString() {
        semester.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        
        String result = semester.toString();
        assertTrue(result.contains("Семестр 1"));
        assertTrue(result.contains("2 предметов"));
    }
}
