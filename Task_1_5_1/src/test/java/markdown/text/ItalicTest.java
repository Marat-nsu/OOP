package markdown.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ItalicTest {

    @Test
    void testToStringFromString() {
        Italic italic = new Italic("italic text");
        assertEquals("*italic text*", italic.toString());
    }

    @Test
    void testToStringFromElement() {
        Italic italic = new Italic(new Text("italic text"));
        assertEquals("*italic text*", italic.toString());
    }

    @Test
    void testEquals() {
        Italic italic1 = new Italic("text");
        Italic italic2 = new Italic(new Text("text"));
        Italic italic3 = new Italic("other");

        assertEquals(italic1, italic2);
        assertEquals(italic1.hashCode(), italic2.hashCode());
        assertNotEquals(italic1, italic3);
    }
}
