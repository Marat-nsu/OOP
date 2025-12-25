package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Bold;
import markdown.text.Text;
import org.junit.jupiter.api.Test;

class LinkTest {

    @Test
    void testToString() {
        Link link = new Link("Google", "https://google.com");
        assertEquals("[Google](https://google.com)", link.toString());
    }

    @Test
    void testToStringWithTitle() {
        Link link = new Link("Google", "https://google.com", "Search");
        assertEquals("[Google](https://google.com \"Search\")", link.toString());
    }

    @Test
    void testToStringWithElement() {
        Link link = new Link(new Bold("Google"), "https://google.com");
        assertEquals("[**Google**](https://google.com)", link.toString());
    }

    @Test
    void testEquals() {
        Link l1 = new Link("Google", "https://google.com");
        Link l2 = new Link(new Text("Google"), "https://google.com");
        Link l3 = new Link("Google", "https://google.com", "Title");
        Link l4 = new Link("Yahoo", "https://yahoo.com");

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
        assertNotEquals(l1, l4);
    }
}
