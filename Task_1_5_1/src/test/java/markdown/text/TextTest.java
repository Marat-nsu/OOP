package markdown.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TextTest {

    @Test
    void testToString() {
        Text text = new Text("Hello World");
        assertEquals("Hello World", text.toString());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Test
    void testEquals() {
        Text text1 = new Text("Hello");
        Text text2 = new Text("Hello");
        Text text3 = new Text("World");

        assertEquals(text1, text2);
        assertEquals(text1.hashCode(), text2.hashCode());
        assertNotEquals(text1, text3);
    }
}
