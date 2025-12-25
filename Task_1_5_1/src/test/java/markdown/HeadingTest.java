package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import markdown.text.Bold;
import markdown.text.Text;
import org.junit.jupiter.api.Test;

class HeadingTest {

    @Test
    void testToStringFromString() {
        Heading heading = new Heading(1, "Title");
        assertEquals("# Title", heading.toString());
    }

    @Test
    void testToStringFromElement() {
        Heading heading = new Heading(2, new Bold("Subtitle"));
        assertEquals("## **Subtitle**", heading.toString());
    }

    @Test
    void testInvalidLevel() {
        assertThrows(IllegalArgumentException.class, () -> new Heading(0, "Title"));
        assertThrows(IllegalArgumentException.class, () -> new Heading(7, "Title"));
    }

    @Test
    void testEquals() {
        Heading h1 = new Heading(1, "Title");
        Heading h2 = new Heading(1, new Text("Title"));
        Heading h3 = new Heading(2, "Title");
        Heading h4 = new Heading(1, "Other");

        assertEquals(h1, h2);
        assertNotEquals(h1, h3);
        assertNotEquals(h1, h4);
    }
}
