import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SolutionTest {
    @Test
    public void testSequential() {
        SequentialSolution solver = new SequentialSolution();
        assertTrue(solver.containsNonPrime(new int[]{6, 8, 7, 13, 5, 9, 4}));
        assertFalse(solver.containsNonPrime(new int[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        }));
    }

    @Test
    public void testParallelThread() throws InterruptedException {
        ParallelThreadSolution solver = new ParallelThreadSolution();
        assertTrue(solver.containsNonPrime(new int[]{6, 8, 7, 13, 5, 9, 4}, 2));
        assertFalse(solver.containsNonPrime(new int[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        }, 4));
    }

    @Test
    public void testParallelStream() {
        ParallelStreamSolution solver = new ParallelStreamSolution();
        assertTrue(solver.containsNonPrime(new int[]{6, 8, 7, 13, 5, 9, 4}));
        assertFalse(solver.containsNonPrime(new int[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        }));
    }
}
