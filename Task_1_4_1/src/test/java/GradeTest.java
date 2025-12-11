import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Grade;
import org.junit.jupiter.api.Test;

public class GradeTest {

    @Test
    void testGetValueForExcellent() {
        assertEquals(5, Grade.EXCELLENT.getValue());
    }

    @Test
    void testGetValueForGood() {
        assertEquals(4, Grade.GOOD.getValue());
    }

    @Test
    void testGetValueForSatisfactory() {
        assertEquals(3, Grade.SATISFACTORY.getValue());
    }

    @Test
    void testGetValueForPass() {
        assertEquals(0, Grade.PASS.getValue());
    }

    @Test
    void testIsGradedReturnsTrueForExcellent() {
        assertTrue(Grade.EXCELLENT.isGraded());
    }

    @Test
    void testIsGradedReturnsTrueForGood() {
        assertTrue(Grade.GOOD.isGraded());
    }

    @Test
    void testIsGradedReturnsTrueForSatisfactory() {
        assertTrue(Grade.SATISFACTORY.isGraded());
    }

    @Test
    void testIsGradedReturnsFalseForPass() {
        assertFalse(Grade.PASS.isGraded());
    }

    @Test
    void testToStringForExcellent() {
        assertEquals("Отлично (5)", Grade.EXCELLENT.toString());
    }

    @Test
    void testToStringForGood() {
        assertEquals("Хорошо (4)", Grade.GOOD.toString());
    }

    @Test
    void testToStringForSatisfactory() {
        assertEquals("Удовлетворительно (3)", Grade.SATISFACTORY.toString());
    }

    @Test
    void testToStringForPass() {
        assertEquals("Зачет", Grade.PASS.toString());
    }
}
