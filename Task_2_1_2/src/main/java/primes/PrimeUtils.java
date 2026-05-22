package primes;

public class PrimeUtils {
    public static final int NO_DIVISOR = 1;

    public static boolean isPrime(int n) {
        return n > 1 && findNonTrivialDivisor(n) == NO_DIVISOR;
    }

    public static int findNonTrivialDivisor(int n) {
        if (n <= 1) {
            return NO_DIVISOR;
        }
        if (n % 2 == 0) {
            return n == 2 ? NO_DIVISOR : 2;
        }
        for (int i = 3; i <= n / i; i += 2) {
            if (n % i == 0) {
                return i;
            }
        }
        return NO_DIVISOR;
    }

    public static int sqrtFloor(int n) {
        return (int) Math.sqrt(n);
    }
}
