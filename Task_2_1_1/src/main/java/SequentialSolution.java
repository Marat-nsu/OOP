public class SequentialSolution {
    public boolean containsNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!PrimeUtils.isPrime(number)) {
                return true;
            }
        }
        return false;
    }
}
