import java.util.Scanner;

/**
 * Стратегия выбора действий для игрока через консоль.
 */
public class ConsoleDecisionStrategy implements DecisionStrategy {
    private final Scanner scanner;

    public ConsoleDecisionStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean shouldHit(Hand hand) {
        System.out.print("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться: ");
        int choice = scanner.nextInt();
        return choice == 1;
    }
}