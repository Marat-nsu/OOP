package markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CodeBlockTest {

    @Test
    void testToString() {
        CodeBlock codeBlock = new CodeBlock("print('hello')", "python");
        String expected = "```python\nprint('hello')\n```";
        assertEquals(expected, codeBlock.toString());
    }

    @Test
    void testToStringNoLanguage() {
        CodeBlock codeBlock = new CodeBlock("code", null);
        String expected = "```\ncode\n```";
        assertEquals(expected, codeBlock.toString());
    }

    @Test
    void testEquals() {
        CodeBlock c1 = new CodeBlock("code", "java");
        CodeBlock c2 = new CodeBlock("code", "java");
        CodeBlock c3 = new CodeBlock("code", "python");
        CodeBlock c4 = new CodeBlock("other", "java");

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
    }
}
