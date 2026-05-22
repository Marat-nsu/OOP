import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import primes.PrimeUtils;

class PrimeUtilsTest {

    @Test
    void testIsPrimeWithSmallPrimes() {

        Assertions.assertTrue(PrimeUtils.isPrime(2), "2 должно быть простым числом");
        Assertions.assertTrue(PrimeUtils.isPrime(3), "3 должно быть простым числом");
    }

    @Test
    void testIsPrimeWithSmallNonPrimes() {

        Assertions.assertFalse(PrimeUtils.isPrime(0), "0 не является простым числом");
        Assertions.assertFalse(PrimeUtils.isPrime(1), "1 не является простым числом");
        Assertions.assertFalse(PrimeUtils.isPrime(4), "4 не является простым числом");
    }

    @Test
    void testIsPrimeWithLargePrimes() {

        Assertions.assertTrue(PrimeUtils.isPrime(20319251), "20319251 должно быть простым числом");
        Assertions.assertTrue(PrimeUtils.isPrime(104729), "104729 должно быть простым числом");
    }

    @Test
    void testIsPrimeWithLargeNonPrimes() {

        Assertions.assertFalse(PrimeUtils.isPrime(20319250), "20319250 не является простым числом");
        Assertions.assertFalse(PrimeUtils.isPrime(100000000), "100000000 не является простым числом");
    }

    @Test
    void testIsPrimeEdgeCases() {

        Assertions.assertFalse(PrimeUtils.isPrime(-1), "-1 не является простым числом");
        Assertions.assertFalse(PrimeUtils.isPrime(-10), "-10 не является простым числом");
    }
}
