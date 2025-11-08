public class Main {
    public static void main(String[] args) {
        String input = "(3+(2*x))";
        ExpressionParser parser = new ExpressionParser(input);
        try {
            Expression e = parser.parse();
            e.print();
            System.out.println();
    
            Expression de = e.derivative("x");
            de.print();
            System.out.println();
    
            int result = e.eval("x = 10; y = 13");
            System.out.println(result);
        } catch (ExpressionException ex) {
            System.err.println("Error parsing expression: " + ex.getMessage());
            return;
        }
    }
}
