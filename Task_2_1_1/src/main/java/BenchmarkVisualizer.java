import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

public class BenchmarkVisualizer {
    private static final int PRIME_VALUE = 20319251;

    private final int[] threadCounts;
    private final int arraySize;

    public BenchmarkVisualizer(int[] threadCounts, int arraySize) {
        this.threadCounts = threadCounts;
        this.arraySize = arraySize;
    }

    public void run() throws InterruptedException {
        int[] data = new int[arraySize];
        Arrays.fill(data, PRIME_VALUE);

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        // Последовательное.
        double seqMs = measureMillis(() -> new SequentialSolution().containsNonPrime(data));
        labels.add("Seq (No1)");
        values.add(seqMs);
        System.out.printf("%-18s %.3f%n", "Seq (No1)", seqMs);

        // Параллельные потоки с разным количеством.
        for (int t : threadCounts) {
            ParallelThreadSolution threadSolver = new ParallelThreadSolution(t);
            double ms = measureMillis(() -> {
                try {
                    return threadSolver.containsNonPrime(data);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
            String label = "Threads=" + t + " (No2)";
            labels.add(label);
            values.add(ms);
            System.out.printf("%-18s %.3f%n", label, ms);
        }

        // Parallel Stream.
        double streamMs = measureMillis(() -> new ParallelStreamSolution().containsNonPrime(data));
        labels.add("ParallelStream (No3)");
        values.add(streamMs);
        System.out.printf("%-18s %.3f%n", "ParallelStream (No3)", streamMs);

        System.out.println();
        printChart(labels, values);
    }

    private double measureMillis(BooleanSupplier op) {
        long start = System.nanoTime();
        op.getAsBoolean();
        long end = System.nanoTime();
        return (end - start) / 1_000_000.0;
    }

    private void printChart(List<String> labels, List<Double> values) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Benchmark Results")
                .xAxisTitle("Threads/Methods")
                .yAxisTitle("Time (ms)")
                .build();

        chart.addSeries("Execution Time", labels, values);

        new SwingWrapper<>(chart).displayChart();
    }
}