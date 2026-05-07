package checker.dsl;

public class DslConfigException extends RuntimeException {
    public DslConfigException(String message) {
        super(message);
    }

    public DslConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
