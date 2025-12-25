package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Text;
import org.junit.jupiter.api.Test;


class ImageTest {

    @Test
    void testToString() {
        Image image = new Image("Alt", "url.png");
        assertEquals("![Alt](url.png)", image.toString());
    }

    @Test
    void testToStringWithTitle() {
        Image image = new Image("Alt", "url.png", "Title");
        assertEquals("![Alt](url.png \"Title\")", image.toString());
    }

    @Test
    void testFromElement() {
        Image image = new Image(new Text("Alt"), "url.png");
        assertEquals("![Alt](url.png)", image.toString());
    }

    @Test
    void testEquals() {
        Image i1 = new Image("Alt", "url.png");
        Image i2 = new Image("Alt", "url.png");
        Image i3 = new Image("Alt", "url.png", "Title");
        Image i4 = new Image("Other", "url.png");

        assertEquals(i1, i2);
        assertNotEquals(i1, i3);
        assertNotEquals(i1, i4);
    }
}
