/**
 * Класс игрока, содержит руку и стратегию решения.
 */
public class Player {
    protected Hand hand;
    protected String name;
    protected DecisionStrategy decisionStrategy;

    /**
     * Конструктор класса игрока.
     *
     * @param name Имя игрока.
     * @param decisionStrategy Стратегия решения.
     */
    public Player(String name, DecisionStrategy decisionStrategy) {
        this.name = name;
        this.hand = new Hand();
        this.decisionStrategy = decisionStrategy;
    }

    public boolean wantsToHit() {
        return decisionStrategy.shouldHit(hand);
    }

    // Остальные методы без изменений
    public void takeCard(Card card) {
        hand.addCard(card);
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void clearHand() {
        hand.clear();
    }
}
