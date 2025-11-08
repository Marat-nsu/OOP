import java.util.Map;

public class Variable extends Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }

    public int eval(Map<String, Integer> vars) {
        return vars.getOrDefault(name, 0);
    }

    public void print() {
        // Вообще toString просто возвращает имя и можно было его сразу и вывести,
        // но для единообразия с остальными классами сделал так
        // Вдруг что-то изменится и мы будем выводить переменные по-другому.
        final String s = toString();
        System.out.print(s);
    }

    public String toString() {
        return name;
    }
}
