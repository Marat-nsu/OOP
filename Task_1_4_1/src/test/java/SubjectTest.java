import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Grade;
import model.Subject;
import model.SubjectType;
import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    void testGetName() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertEquals("Math", subject.getName());
    }

    @Test
    void testGetType() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertEquals(SubjectType.EXAM, subject.getType());
    }

    @Test
    void testGetGrade() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, subject.getGrade());
    }

    @Test
    void testIsExamReturnsTrueForExam() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertTrue(subject.isExam());
    }

    @Test
    void testIsExamReturnsFalseForNonExam() {
        Subject subject = new Subject("PE", SubjectType.CREDIT, Grade.PASS);
        assertFalse(subject.isExam());
    }

    @Test
    void testIsDifferentiatedCreditReturnsTrueForDifferentiatedCredit() {
        Subject subject = new Subject("English", SubjectType.DIFFERENTIATED_CREDIT, Grade.GOOD);
        assertTrue(subject.isDifferentiatedCredit());
    }

    @Test
    void testIsDifferentiatedCreditReturnsFalseForNonDifferentiatedCredit() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertFalse(subject.isDifferentiatedCredit());
    }

    @Test
    void testCountsForDiplomaReturnsTrueForExam() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        assertTrue(subject.countsForDiploma());
    }

    @Test
    void testCountsForDiplomaReturnsTrueForDifferentiatedCredit() {
        Subject subject = new Subject("English", SubjectType.DIFFERENTIATED_CREDIT, Grade.GOOD);
        assertTrue(subject.countsForDiploma());
    }

    @Test
    void testCountsForDiplomaReturnsFalseForCredit() {
        Subject subject = new Subject("PE", SubjectType.CREDIT, Grade.PASS);
        assertFalse(subject.countsForDiploma());
    }

    @Test
    void testToString() {
        Subject subject = new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT);
        String result = subject.toString();
        assertTrue(result.contains("Math"));
        assertTrue(result.contains("EXAM"));
        assertTrue(result.contains("Отлично"));
    }
}
