import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SolutionTest {
    static Stream<Solution> solutionProvider() {
        return Stream.of(
            new SequentialSolution(),
            new ParallelThreadSolution(4),
            new ParallelStreamSolution()
        );
    }

    @ParameterizedTest(name = "Testing with {0}")
    @MethodSource("solutionProvider")
    void testAllImplementations(Solution solver) throws InterruptedException {
        int[] withNonPrime = {6, 8, 7, 13, 5, 9, 4};
        assertTrue(solver.containsNonPrime(withNonPrime), 
            solver.getClass().getSimpleName() + " failed on non-prime data");

        int[] onlyPrimes = {
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        };
        assertFalse(solver.containsNonPrime(onlyPrimes), 
            solver.getClass().getSimpleName() + " failed on all-prime data");
    }
}
