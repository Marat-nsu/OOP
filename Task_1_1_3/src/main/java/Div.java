import java.util.Map;

public class Div extends Expression {
    private Expression left;
    private Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression derivative(String var) {
        return new Div(
                new Sub(new Mul(left.derivative(var), right), new Mul(left, right.derivative(var))),
                new Mul(right, right));
    }

    public int eval(Map<String, Integer> vars) {
        int denominator = right.eval(vars);
        // Иерархия исключений
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.eval(vars) / denominator;
    }

    public void print() {
        final String s = toString();
        System.out.print(s);
    }

    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }
}
