/**
 * Стратегия выбора действий для дилера, он просто берёт карты пока его счёт меньше чем 17.
 */
public class DealerDecisionStrategy implements DecisionStrategy {
    @Override
    public boolean shouldHit(Hand hand) {
        return hand.calculateScore() < 17;
    }
}