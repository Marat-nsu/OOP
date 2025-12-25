package markdown.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class BoldTest {

    @Test
    void testToStringFromString() {
        Bold bold = new Bold("bold text");
        assertEquals("**bold text**", bold.toString());
    }

    @Test
    void testToStringFromElement() {
        Bold bold = new Bold(new Text("bold text"));
        assertEquals("**bold text**", bold.toString());
    }

    @Test
    void testNested() {
        Bold bold = new Bold(new Italic("bold and italic"));
        assertEquals("***bold and italic***", bold.toString());
    }

    @Test
    void testEquals() {
        Bold bold1 = new Bold("text");
        Bold bold2 = new Bold(new Text("text"));
        Bold bold3 = new Bold("other");

        assertEquals(bold1, bold2);
        assertEquals(bold1.hashCode(), bold2.hashCode());
        assertNotEquals(bold1, bold3);
    }
}
