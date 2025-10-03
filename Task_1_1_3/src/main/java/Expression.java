abstract class Expression {
    abstract void print();

    abstract Expression derivative(String var);

    abstract int eval(String assignments);

    public abstract String toString();
}
