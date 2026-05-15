package checker.logging;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckerLogger {
    private static final DateTimeFormatter FORMATTER 
        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final CheckerLogger STDERR = new CheckerLogger(System.err);

    private final PrintStream out;

    public CheckerLogger(PrintStream out) {
        this.out = out;
    }

    public static CheckerLogger stderr() {
        return STDERR;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        out.printf("[%s] %-5s %s%n", LocalDateTime.now().format(FORMATTER), level, message);
    }
}
