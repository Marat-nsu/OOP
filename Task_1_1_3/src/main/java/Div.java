class Div extends Expression {
    private Expression left, right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    void print() {
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }

    @Override
    Expression derivative(String var) {
        return new Div(
                new Sub(new Mul(left.derivative(var), right), new Mul(left, right.derivative(var))),
                new Mul(right, right));
    }

    @Override
    int eval(String assignments) {
        int denominator = right.eval(assignments);
        if (denominator == 0) throw new ArithmeticException("Division by zero");
        return left.eval(assignments) / denominator;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }
}
