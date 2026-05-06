package checker.logging;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class CheckerLoggerTest {
    @Test
    void writesLevelAndMessageToConfiguredStream() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        CheckerLogger logger
             = new CheckerLogger(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        logger.warn("Something happened");

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("WARN"));
        assertTrue(output.contains("Something happened"));
    }
}
