/**
 * Стратегия выбора действий для игрока через консоль.
 */
public class ConsoleDecisionStrategy implements DecisionStrategy {
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public ConsoleDecisionStrategy(InputHandler inputHandler, OutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public boolean shouldHit(Hand hand) {

        outputHandler.println("Your hand score: " + hand.score() + "\n");

        outputHandler.println("Decide muffaka");

        int choice;
        while (true) {
            try {
                choice = inputHandler.readInt(0, 1);
                break;
            } catch (IllegalArgumentException e) {
                outputHandler.println("Dumbo");
            }
        }
        return choice == 1;
    }
}