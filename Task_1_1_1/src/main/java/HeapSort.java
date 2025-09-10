/**
 * Классическая реализация пирамидальной сортировки (heapsort).
 */
public class HeapSort {
    /**
     * Метод пирамидальной сортировки.
     * 
     * @param arr
     * @return отсортированный массив
     */
    public static int[] heapsort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
        return arr;
    }

    /**
     * Heapify - рекурсивная функция, которая перестраивает подмассив
     * arr[rootIndex..heapSize-1] в пирамидальную форму.
     * 
     * @param arr       массив для сортировки
     * @param heapSize  размер кучи
     * @param rootIndex индекс вершины кучи
     */
    private static void heapify(int[] arr, int heapSize, int rootIndex) {
        int largest = rootIndex;
        int left = 2 * rootIndex + 1;
        int right = 2 * rootIndex + 2;

        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != rootIndex) {
            int swap = arr[rootIndex];
            arr[rootIndex] = arr[largest];
            arr[largest] = swap;

            heapify(arr, heapSize, largest);
        }
    }
}
