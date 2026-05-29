package blockchain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProofValidatorTest {
    private final ProofValidator validator = new ProofValidator();

    @Test
    void acceptsValidCompositeProof() {
        assertTrue(validator.isValid(8, CheckResult.COMPOSITE, "2"));
    }

    @Test
    void acceptsLessThanTwoCompositeProof() {
        assertTrue(validator.isValid(1, CheckResult.COMPOSITE, PrimalityProof.LESS_THAN_TWO_PROOF));
        assertTrue(validator.isValid(0, CheckResult.COMPOSITE, PrimalityProof.LESS_THAN_TWO_PROOF));
        assertTrue(validator.isValid(-3, CheckResult.COMPOSITE, PrimalityProof.LESS_THAN_TWO_PROOF));
    }

    @Test
    void rejectsFakeCompositeProof() {
        assertFalse(validator.isValid(7, CheckResult.COMPOSITE, "2"));
        assertFalse(validator.isValid(7, CheckResult.COMPOSITE, "1"));
        assertFalse(validator.isValid(7, CheckResult.COMPOSITE, "7"));
    }

    @Test
    void acceptsValidPrimeProof() {
        assertTrue(validator.isValid(13, CheckResult.PRIME, "3"));
    }

    @Test
    void rejectsFakePrimeProof() {
        assertFalse(validator.isValid(9, CheckResult.PRIME, "3"));
        assertFalse(validator.isValid(1, CheckResult.PRIME, "1"));
    }
}
