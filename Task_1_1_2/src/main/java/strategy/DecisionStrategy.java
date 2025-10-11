package strategy;

import model.Hand;

public interface DecisionStrategy {
    boolean shouldHit(Hand hand);
}