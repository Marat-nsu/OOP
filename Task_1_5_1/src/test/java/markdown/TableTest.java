package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import markdown.text.Bold;
import org.junit.jupiter.api.Test;

class TableTest {

    @Test
    void testTableBuilder() {
        Table table = new Table.Builder()
                .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
                .withRowLimit(2)
                .addRow("ID", "Name")
                .addRow(1, new Bold("John"))
                .build();

        String expected = "| ID  | Name     |\n" +
                          "| --: | -------- |\n" +
                          "| 1   | **John** |";
        assertEquals(expected.trim(), table.toString().trim());
    }
    
    @Test
    void testSimpleTable() {
        Table table = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT)
                .addRow("A")
                .build();
         
        assertEquals("| A   |\n| --- |", table.toString().trim());
    }

    @Test
    void testEquals() {
        Table t1 = new Table.Builder().addRow("A").build();
        Table t2 = new Table.Builder().addRow("A").build();
        Table t3 = new Table.Builder().addRow("B").build();

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
    }
}
