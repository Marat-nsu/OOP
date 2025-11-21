import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SubstringSearcherTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @AfterEach
    void cleanup() throws IOException {
        if (testFile != null && Files.exists(testFile)) {
            Files.deleteIfExists(testFile);
        }
    }

    @Test
    void testProvidedExample() throws IOException {
        testFile = tempDir.resolve("input.txt");
        Files.write(testFile, "абракадабра".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "бра");
        assertEquals(Arrays.asList(1, 8), result);
    }

    @Test
    void testSingleCharacter() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "aaa".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "a");
        assertEquals(Arrays.asList(0, 1, 2), result);
    }

    @Test
    void testPatternNotFound() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello world".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "xyz");
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyPattern() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "");
        assertTrue(result.isEmpty());
    }

    @Test
    void testNullPattern() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testOverlappingMatches() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "aaaa".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "aa");
        assertEquals(Arrays.asList(0, 1, 2), result);
    }

    @Test
    void testPatternAtStart() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello world".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "hello");
        assertEquals(Arrays.asList(0), result);
    }

    @Test
    void testPatternAtEnd() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "hello world".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "world");
        assertEquals(Arrays.asList(6), result);
    }

    @Test
    void testMultipleMatches() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "abc abc abc".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "abc");
        assertEquals(Arrays.asList(0, 4, 8), result);
    }

    @Test
    void testEmptyFile() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, new byte[0]);

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "test");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            SubstringSearcher.find("nonexistent_file.txt", "test");
        });
    }

    @Test
    void testPatternLongerThanContent() throws IOException {
        testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "ab".getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "abcd");
        assertTrue(result.isEmpty());
    }

    @Test
    void testWholeFileAsPattern() throws IOException {
        testFile = tempDir.resolve("test.txt");
        String content = "hello world";
        Files.write(testFile, content.getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), content);
        assertEquals(Arrays.asList(0), result);
    }

    @Test
    void testLargeFileWithRepeatedPattern() throws IOException {
        testFile = tempDir.resolve("large.txt");
        StringBuilder sb = new StringBuilder();
        String chunk = "test pattern ";
        for (int i = 0; i < 80000; i++) {
            sb.append(chunk);
        }
        Files.write(testFile, sb.toString().getBytes(StandardCharsets.UTF_8));

        List<Integer> result = SubstringSearcher.find(testFile.toString(), "pattern");
        assertTrue(result.size() > 1000, "Should find many pattern matches in large file");
        assertEquals(5, result.get(0));
        assertEquals(18, result.get(1));
    }
}
