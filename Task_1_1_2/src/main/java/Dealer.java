/**
 * Класс, представляющий дилера, наследуется от игрока.
 */
public class Dealer extends Player {
    public Dealer() {
        super("Дилер", new DealerDecisionStrategy());
    }
}
