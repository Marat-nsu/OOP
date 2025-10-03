import java.util.HashMap;
import java.util.Map;

class Main {
    static Map<String, Integer> parseAssignments(String assignments) {
        Map<String, Integer> vars = new HashMap<>();
        if (assignments == null || assignments.isEmpty()) {
            return vars;
        }
        String[] pairs = assignments.split(";");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment: " + pair);
            }
            vars.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        return vars;
    }

    public static void main(String[] args) {
        String input = "(3+(2*x))";
        ExpressionParser parser = new ExpressionParser(input);
        Expression e = parser.parse();
        e.print();
        System.out.println();

        Expression de = e.derivative("x");
        de.print();
        System.out.println();

        int result = e.eval("x = 10; y = 13");
        System.out.println(result);
    }
}
