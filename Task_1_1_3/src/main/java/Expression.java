import java.util.HashMap;
import java.util.Map;

public abstract class Expression {
    public abstract Expression derivative(String var);

    public abstract int eval(Map<String, Integer> vars) throws ExpressionException;

    // Точка входа публичная: парсит присвоения один раз и делегирует eval на основе Map.
    public final int eval(String assignments) throws ExpressionException {
        Map<String, Integer> vars = parseAssignments(assignments);
        return eval(vars);
    }

    /** Парсит строку вида "x=10;y=20" в Map. */
    public static Map<String, Integer> parseAssignments(String assignments) throws ExpressionException {
        Map<String, Integer> vars = new HashMap<>();
        if (assignments == null || assignments.isEmpty()) {
            return vars;
        }
        String[] pairs = assignments.split(";");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length != 2) {
                throw new ExpressionException("Invalid assignment: " + pair);
            }
            vars.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        return vars;
    }

    public abstract void print();

    public abstract String toString();
}
