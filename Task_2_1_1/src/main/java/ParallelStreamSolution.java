import java.util.Arrays;
import java.util.List;

public class ParallelStreamSolution {
    public boolean containsNonPrime(int[] numbers) {
        List<Integer> list = Arrays.stream(numbers).boxed().toList();
        return list.parallelStream().anyMatch(n -> !PrimeUtils.isPrime(n));
    }
}
