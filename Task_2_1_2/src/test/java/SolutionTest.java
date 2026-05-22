import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SolutionTest {
    @Test
    void distributedBlockchainSolutionSolvesExamples() {
        Solution solution = new DistributedBlockchainSolution();

        assertTrue(solution.containsNonPrime(new int[] {6, 8, 7, 13, 5, 9, 4}));
        assertTrue(solution.containsNonPrime(new int[] {2, 3, 1}));

        int[] onlyPrimes = {
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        };
        assertFalse(solution.containsNonPrime(onlyPrimes));
    }
}
