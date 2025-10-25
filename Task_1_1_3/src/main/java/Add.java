class Add extends Expression {
    private Expression left;
    private Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    void print() {
        // использовать toString
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }


    Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }


    int eval(String assignments) {
        return left.eval(assignments) + right.eval(assignments);
    }

    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }
}
