import java.util.Map;

public class Mul extends Expression {
    private Expression left;
    private Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression derivative(String var) {
        return new Add(
                    new Mul(left.derivative(var), right),
                    new Mul(left, right.derivative(var)));
    }

    public int eval(Map<String, Integer> vars) throws ExpressionException {
        return left.eval(vars) * right.eval(vars);
    }

    public void print() {
        final String s = toString();
        System.out.print(s);
    }

    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }
}
