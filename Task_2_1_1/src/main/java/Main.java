public class Main {
    public static void main(String[] args) throws InterruptedException {
        int[] threadCounts = {1, 2, 4, 8, 16, 32};
        int arraySize = 300_000;

        BenchmarkVisualizer visualizer = new BenchmarkVisualizer(threadCounts, arraySize);
        visualizer.run();
    }
}

