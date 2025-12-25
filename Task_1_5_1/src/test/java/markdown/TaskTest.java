package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Bold;
import markdown.text.Text;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testToStringChecked() {
        Task task = new Task("todo", true);
        assertEquals("- [x] todo", task.toString());
    }

    @Test
    void testToStringUnchecked() {
        Task task = new Task("todo", false);
        assertEquals("- [ ] todo", task.toString());
    }

    @Test
    void testToStringBold() {
        Task task = new Task(new Bold("todo"), true);
        assertEquals("- [x] **todo**", task.toString());
    }

    @Test
    void testEquals() {
        Task t1 = new Task("todo", true);
        Task t2 = new Task(new Text("todo"), true);
        Task t3 = new Task("todo", false);
        Task t4 = new Task("other", true);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
        assertNotEquals(t1, t4);
    }
}
