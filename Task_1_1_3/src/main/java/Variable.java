import java.util.Map;

class Variable extends Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    void print() {
        System.out.print(name);
    }

    @Override
    Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }

    @Override
    int eval(String assignments) {
        Map<String, Integer> vars = Main.parseAssignments(assignments);
        return vars.getOrDefault(name, 0);
    }

    @Override
    public String toString() {
        return name;
    }
}
