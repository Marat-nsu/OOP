package blockchain;

public class ProofValidator {
    public boolean isValid(int number, CheckResult result, String proof) {
        if (result == CheckResult.COMPOSITE && number <= 1) {
            return PrimalityProof.LESS_THAN_TWO_PROOF.equals(proof);
        }
        if (result == CheckResult.PRIME && number <= 1) {
            return false;
        }

        try {
            int proofValue = Integer.parseInt(proof);
            if (result == CheckResult.COMPOSITE) {
                return proofValue > 1 && proofValue < number && number % proofValue == 0;
            }
            if ((long) (proofValue + 1) * (proofValue + 1) <= number) {
                return false;
            }
            for (int divisor = 2; divisor <= proofValue && divisor <= number / divisor; divisor++) {
                if (number % divisor == 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
