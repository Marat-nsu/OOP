package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Bold;
import markdown.text.Italic;
import markdown.text.Text;
import org.junit.jupiter.api.Test;

class MListTest {

    @Test
    void testUnorderedList() {
        MList list = new MList.Builder()
                .ordered(false)
                .addItem("Item 1")
                .addItem(new Text("Item 2"))
                .build();

        String expected = "- Item 1\n- Item 2";
        assertEquals(expected, list.toString());
    }

    @Test
    void testOrderedList() {
        MList list = new MList.Builder()
                .ordered(true)
                .addItem("Item 1")
                .addItem("Item 2")
                .build();

        String expected = "1. Item 1\n2. Item 2";
        assertEquals(expected, list.toString());
    }

    @Test
    void testListFromElements() {
        MList list = new MList.Builder()
            .ordered(false)
            .addItem(new Bold("Element 1"))
            .addItem(new Italic("Element 2"))
            .build();
        
        String expected = "- **Element 1**\n- *Element 2*";
        assertEquals(expected, list.toString());
    }

    @Test
    void testEquals() {
        MList l1 = new MList.Builder().addItem("A").build();
        MList l2 = new MList.Builder().addItem("A").build();
        MList l3 = new MList.Builder().ordered(true).addItem("A").build();
        MList l4 = new MList.Builder().addItem("B").build();

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
        assertNotEquals(l1, l4);
    }
}
