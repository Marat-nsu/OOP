package markdown.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class StrikeTest {

    @Test
    void testToStringFromString() {
        Strike strike = new Strike("strike text");
        assertEquals("~~strike text~~", strike.toString());
    }

    @Test
    void testToStringFromElement() {
        Strike strike = new Strike(new Text("strike text"));
        assertEquals("~~strike text~~", strike.toString());
    }

    @Test
    void testEquals() {
        Strike strike1 = new Strike("text");
        Strike strike2 = new Strike(new Text("text"));
        Strike strike3 = new Strike("other");

        assertEquals(strike1, strike2);
        assertEquals(strike1.hashCode(), strike2.hashCode());
        assertNotEquals(strike1, strike3);
    }
}
