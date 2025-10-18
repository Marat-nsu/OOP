class Number extends Expression {
    private int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    void print() {
        System.out.print(value);
    }

    @Override
    Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    int eval(String assignments) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
