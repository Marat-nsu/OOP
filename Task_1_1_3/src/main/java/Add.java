class Add extends Expression {
    private Expression left, right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    void print() {
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }

    @Override
    Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    int eval(String assignments) {
        return left.eval(assignments) + right.eval(assignments);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }
}
