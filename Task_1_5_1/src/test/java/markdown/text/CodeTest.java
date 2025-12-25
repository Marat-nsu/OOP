package markdown.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CodeTest {

    @Test
    void testToString() {
        Code code = new Code("code");
        assertEquals("`code`", code.toString());
    }

    @Test
    void testEquals() {
        Code code1 = new Code("code");
        Code code2 = new Code("code");
        Code code3 = new Code("other");

        assertEquals(code1, code2);
        assertEquals(code1.hashCode(), code2.hashCode());
        assertNotEquals(code1, code3);
    }
}
