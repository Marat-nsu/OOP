package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Strike;
import markdown.text.Text;
import org.junit.jupiter.api.Test;

class QuoteTest {

    @Test
    void testToStringFromString() {
        Quote quote = new Quote("quote");
        assertEquals("> quote", quote.toString());
    }

    @Test
    void testToStringFromElement() {
        Quote quote = new Quote(new Strike("quote"));
        assertEquals("> ~~quote~~", quote.toString());
    }

    @Test
    void testEquals() {
        Quote q1 = new Quote("text");
        Quote q2 = new Quote(new Text("text"));
        Quote q3 = new Quote("other");

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
    }
}
