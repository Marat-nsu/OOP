import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Grade;
import model.GradeBook;
import model.Semester;
import model.Subject;
import model.SubjectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GradeBookTest {
    private GradeBook gradeBook;

    @BeforeEach
    void setUp() {
        gradeBook = new GradeBook("Test Student", false);
    }

    @Test
    void testAverageGradeCalculation() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        semester1.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        gradeBook.addSemester(semester1);

        assertEquals(4.5, gradeBook.calculateAverageGrade(), 0.001);
    }

    @Test
    void testAverageGradeWithOnlyPasses() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        gradeBook.addSemester(semester1);

        assertEquals(0.0, gradeBook.calculateAverageGrade(), 0.001);
    }

    @Test
    void testCannotTransferFromBudget() {
        assertFalse(gradeBook.canTransferToBudget());
    }

    @Test
    void testCannotTransferWithLessThanTwoSemesters() {
        GradeBook paidStudent = new GradeBook("Paid Student", true);
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        paidStudent.addSemester(semester1);

        assertFalse(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithGoodGrades() {
        GradeBook paidStudent = new GradeBook("Paid Student", true);

        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        paidStudent.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("English",
                SubjectType.DIFFERENTIATED_CREDIT, Grade.SATISFACTORY));
        paidStudent.addSemester(semester2);

        assertTrue(paidStudent.canTransferToBudget());
    }

    @Test
    void testCannotTransferWithSatisfactoryInExams() {
        GradeBook paidStudent = new GradeBook("Paid Student", true);

        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.SATISFACTORY));
        paidStudent.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        paidStudent.addSemester(semester2);

        assertFalse(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanGetRedDiplomaWithExcellentGrades() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("English",
                SubjectType.DIFFERENTIATED_CREDIT, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.GOOD));
        gradeBook.addSemester(semester1);

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCannotGetRedDiplomaWithSatisfactory() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.SATISFACTORY));
        gradeBook.addSemester(semester1);

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCannotGetRedDiplomaWithLessThan75PercentExcellent() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("English",
                SubjectType.DIFFERENTIATED_CREDIT, Grade.GOOD));
        gradeBook.addSemester(semester1);

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCannotGetRedDiplomaWithBadThesis() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester1);

        gradeBook.setThesisGrade(Grade.GOOD);

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithExcellentThesis() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester1);

        gradeBook.setThesisGrade(Grade.EXCELLENT);

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarshipWithAllExcellent() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        gradeBook.addSemester(semester1);

        assertTrue(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    void testCannotGetIncreasedScholarshipWithGoodGrade() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.GOOD));
        gradeBook.addSemester(semester1);

        assertFalse(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    void testPaidStudentCannotGetScholarship() {
        GradeBook paidStudent = new GradeBook("Paid Student", true);

        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        paidStudent.addSemester(semester1);

        assertFalse(paidStudent.canGetIncreasedScholarship());
    }

    @Test
    void testIncreasedScholarshipOnlyConsidersLastSemester() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.SATISFACTORY));
        gradeBook.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester2);

        assertTrue(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    void testRedDiplomaExactly75Percent() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math1", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Math2", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("DB", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("Networks", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("English",
                SubjectType.DIFFERENTIATED_CREDIT, Grade.GOOD));
        semester2.addSubject(new Subject("Physics",
                SubjectType.DIFFERENTIATED_CREDIT, Grade.GOOD));
        gradeBook.addSemester(semester2);

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    void testGetStudentName() {
        assertEquals("Test Student", gradeBook.getStudentName());
    }

    @Test
    void testIsPaidEducation() {
        assertFalse(gradeBook.isPaidEducation());
        GradeBook paidStudent = new GradeBook("Paid Student", true);
        assertTrue(paidStudent.isPaidEducation());
    }

    @Test
    void testGetSemesters() {
        Semester semester1 = new Semester(1);
        gradeBook.addSemester(semester1);
        
        assertEquals(1, gradeBook.getSemesters().size());
        assertEquals(1, gradeBook.getSemesters().get(0).getSemesterNumber());
    }

    @Test
    void testGetThesisGrade() {
        assertEquals(null, gradeBook.getThesisGrade());
        
        gradeBook.setThesisGrade(Grade.EXCELLENT);
        assertEquals(Grade.EXCELLENT, gradeBook.getThesisGrade());
    }

    @Test
    void testToStringBasic() {
        String result = gradeBook.toString();
        assertTrue(result.contains("Test Student"));
        assertTrue(result.contains("Бюджетная"));
        assertTrue(result.contains("Средний балл"));
    }

    @Test
    void testToStringWithPaidEducation() {
        GradeBook paidStudent = new GradeBook("Paid Student", true);
        String result = paidStudent.toString();
        assertTrue(result.contains("Paid Student"));
        assertTrue(result.contains("Платная"));
    }

    @Test
    void testToStringWithSemesters() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester1);

        String result = gradeBook.toString();
        assertTrue(result.contains("Всего семестров: 1"));
        assertTrue(result.contains("Семестр 1"));
    }

    @Test
    void testToStringWithThesis() {
        gradeBook.setThesisGrade(Grade.EXCELLENT);
        String result = gradeBook.toString();
        assertTrue(result.contains("Квалификационная работа"));
    }

    @Test
    void testToStringWithFullData() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));
        semester1.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester2);

        gradeBook.setThesisGrade(Grade.EXCELLENT);

        String result = gradeBook.toString();
        assertTrue(result.contains("Зачетная книжка"));
        assertTrue(result.contains("Test Student"));
        assertTrue(result.contains("Всего семестров: 2"));
        assertTrue(result.contains("Средний балл: 5"));
        assertTrue(result.contains("Возможность перевода на бюджет: Нет"));
        assertTrue(result.contains("Возможность красного диплома: Да"));
        assertTrue(result.contains("Возможность повышенной стипендии: Да"));
    }

    @Test
    void testCanGetRedDiplomaWithEmptySubjects() {
        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithOnlyCredits() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("PE", SubjectType.CREDIT, Grade.PASS));
        gradeBook.addSemester(semester1);

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarshipWithEmptySemester() {
        Semester semester1 = new Semester(1);
        gradeBook.addSemester(semester1);

        assertTrue(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    void testRedDiplomaUsesLastGrade() {
        Semester semester1 = new Semester(1);
        semester1.addSubject(new Subject("Math", SubjectType.EXAM, Grade.SATISFACTORY));
        gradeBook.addSemester(semester1);

        Semester semester2 = new Semester(2);
        semester2.addSubject(new Subject("Math", SubjectType.EXAM, Grade.EXCELLENT));

        semester2.addSubject(new Subject("Programming", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("Algorithms", SubjectType.EXAM, Grade.EXCELLENT));
        semester2.addSubject(new Subject("English", SubjectType.EXAM, Grade.EXCELLENT));
        gradeBook.addSemester(semester2);

        gradeBook.setThesisGrade(Grade.EXCELLENT);

        assertTrue(gradeBook.canGetRedDiploma());
    }
}
