package strategy;

import model.Hand;
import ui.InputHandler;

/**
 * Стратегия выбора действий для игрока через консоль.
 */
public class ConsoleDecisionStrategy implements DecisionStrategy {
    private final InputHandler inputHandler;

    public ConsoleDecisionStrategy(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public boolean shouldHit(Hand hand) {
        int choice;
        while (true) {
            try {
                choice = inputHandler.readInt(0, 1);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Enter 0 or 1.");
            }
        }
        return choice == 1;
    }
}