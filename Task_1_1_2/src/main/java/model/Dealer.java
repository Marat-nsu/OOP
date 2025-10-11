package model;

import strategy.DealerDecisionStrategy;
import strategy.DecisionStrategy;

public class Dealer extends Player {
    public Dealer() {
        super("Дилер", new DealerDecisionStrategy());
    }
}
