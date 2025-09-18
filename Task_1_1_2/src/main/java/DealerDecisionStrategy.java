public class DealerDecisionStrategy implements DecisionStrategy {
	@Override
	public boolean shouldHit(Hand hand) {
		return hand.calculateScore() < 17;
	}
}