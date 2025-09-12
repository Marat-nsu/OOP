import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class HeapSortTest {

    @Test
    void testSortedArray() {
        int[] arr = { 1, 2, 3, 4, 5 };
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, HeapSort.heapsort(arr));
    }

    @Test
    void testReverseArray() {
        int[] arr = { 5, 4, 3, 2, 1 };
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, HeapSort.heapsort(arr));
    }

    @Test
    void testRandomArray() {
        int[] arr = { 3, 1, 4, 1, 5, 9 };
        int[] expected = arr.clone();
        Arrays.sort(expected);
        assertArrayEquals(expected, HeapSort.heapsort(arr));
    }

    @Test
    void testEmptyArray() {
        int[] arr = {};
        assertArrayEquals(new int[] {}, HeapSort.heapsort(arr));
    }

    @Test
    void testSingleElement() {
        int[] arr = { 42 };
        assertArrayEquals(new int[] { 42 }, HeapSort.heapsort(arr));
    }

    @Test
    void testNullArray() {
        assertThrows(NullPointerException.class, () -> HeapSort.heapsort(null));
    }
}
