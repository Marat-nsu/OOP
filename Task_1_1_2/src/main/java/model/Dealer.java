package model;

import strategy.DealerDecisionStrategy;

public class Dealer extends Player {
    public Dealer() {
        super("Дилер", new DealerDecisionStrategy());
    }
}
