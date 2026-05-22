package blockchain;

import lombok.Value;
import primes.PrimeUtils;

@Value
public class PrimalityProof {
    public static final String LESS_THAN_TWO_PROOF = "LESS_THAN_TWO";

    CheckResult result;
    String proof;

    public static PrimalityProof forNumber(int number) {
        if (number <= 1) {
            return new PrimalityProof(CheckResult.COMPOSITE, LESS_THAN_TWO_PROOF);
        }
        int divisor = PrimeUtils.findNonTrivialDivisor(number);
        if (divisor == PrimeUtils.NO_DIVISOR) {
            return new PrimalityProof(CheckResult.PRIME, String.valueOf(PrimeUtils.sqrtFloor(number)));
        }
        return new PrimalityProof(CheckResult.COMPOSITE, String.valueOf(divisor));
    }
}
