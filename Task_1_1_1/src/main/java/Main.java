import java.util.Arrays;

public class Main {
    /**
     * Демонстрация работы пирамидальной сортировки.
     */
    public static void main(String[] args) {
        int[] input = { 5, 4, 3, 2, 1 };
        int[] sorted = HeapSort.heapsort(input);
        System.out.println("Result: " + Arrays.toString(sorted));
    }
}