import java.util.Map;

public class Add extends Expression {
    private Expression left;
    private Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) + right.eval(vars);
    }

    public void print() {
        final String s = toString();
        System.out.print(s);
    }

    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }
}
