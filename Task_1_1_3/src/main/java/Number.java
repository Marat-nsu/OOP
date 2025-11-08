import java.util.Map;

public class Number extends Expression {
    private int value;

    public Number(int value) {
        this.value = value;
    }

    public Expression derivative(String var) {
        return new Number(0);
    }

    public int eval(Map<String, Integer> vars) {
        return value;
    }

    public void print() {
        final String s = toString();
        System.out.print(s);
    }

    public String toString() {
        return String.valueOf(value);
    }
}
