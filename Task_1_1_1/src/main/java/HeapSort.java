/**
 * Классическая реализация пирамидальной сортировки (heapsort).
 */
public class HeapSort {

    // Приватный конструктор предотвращает создание экземпляра класса
    private HeapSort() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    /**
     * Метод пирамидальной сортировки.
     *
     * @param arr исходный массив (не может быть null)
     * @return новый отсортированный массив
     */
    public static int[] heapsort(int[] arr) {
        if (arr == null) {
            throw new NullPointerException("Input array must not be null");
        }
        // создаем копию, чтобы не менять оригинал
        int[] copy = arr.clone();
        int n = copy.length;

        // Построение max-heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(copy, n, i);
        }

        // Извлечение элементов из кучи
        for (int i = n - 1; i > 0; i--) {
            int temp = copy[0];
            copy[0] = copy[i];
            copy[i] = temp;

            heapify(copy, i, 0);
        }
        return copy;
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
