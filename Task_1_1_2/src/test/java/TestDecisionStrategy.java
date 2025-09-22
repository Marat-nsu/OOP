/**
 * Вспомогательный класс для тестирования DecisionStrategy.
 */
public class TestDecisionStrategy implements DecisionStrategy {
    private final boolean[] decisions;
    private int decisionIndex = 0;

    public TestDecisionStrategy(boolean... decisions) {
        this.decisions = decisions;
    }

    @Override
    public boolean shouldHit(Hand hand) {
        if (decisionIndex < decisions.length) {
            return decisions[decisionIndex++];
        }
        return false;
    }
}
