package checker.engine;

public record TestCounts(int passed, int failed, int skipped) {
    public static final TestCounts ZERO = new TestCounts(0, 0, 0);

    public int total() {
        return passed + failed + skipped;
    }

    @Override
    public String toString() {
        return passed + "/" + failed + "/" + skipped;
    }
}
