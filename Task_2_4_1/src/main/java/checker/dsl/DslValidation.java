package checker.dsl;

public final class DslValidation {
    private DslValidation() { }

    public static String requiredString(Object value, String field) {
        if (value == null || value.toString().isBlank()) {
            throw new DslConfigException("Required DSL field is missing: " + field);
        }
        return value.toString();
    }

    public static int requiredInt(Object value, String field) {
        if (value == null) {
            throw new DslConfigException("Required DSL field is missing: " + field);
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new DslConfigException("DSL field must be an integer: " + field);
        }
    }
}
