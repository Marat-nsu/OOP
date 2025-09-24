import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleDecisionStrategyTest {
    private ConsoleDecisionStrategy strategy;
    private Hand hand;

    @BeforeEach
    void setUp() {
        // Используем реальный класс Hand
        hand = new Hand();
    }

    @Test
    void shouldHit_returnsTrue_whenUserInputsOne() {
        // Подменяем System.in для эмуляции ввода "1"
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        strategy = new ConsoleDecisionStrategy(new Scanner(System.in));

        // Проверяем, что метод возвращает true
        assertTrue(strategy.shouldHit(hand), "Метод должен вернуть true при вводе '1'");
    }

    @Test
    void shouldHit_returnsFalse_whenUserInputsZero() {
        // Подменяем System.in для эмуляции ввода "0"
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        strategy = new ConsoleDecisionStrategy(new Scanner(System.in));

        // Проверяем, что метод возвращает false
        assertFalse(strategy.shouldHit(hand), "Метод должен вернуть false при вводе '0'");
    }

    @Test
    void shouldHit_throwsException_whenInvalidInput() {
        // Подменяем System.in для эмуляции некорректного ввода
        String input = "invalid\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        strategy = new ConsoleDecisionStrategy(new Scanner(System.in));

        // Проверяем, что метод выбрасывает InputMismatchException
        assertThrows(java.util.InputMismatchException.class, () -> strategy.shouldHit(hand),
                "Метод должен выбросить InputMismatchException при некорректном вводе");
    }

    @Test
    void shouldHit_handlesMultipleCallsCorrectly() {
        // Эмулируем последовательный ввод "1" и "0"
        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        strategy = new ConsoleDecisionStrategy(new Scanner(System.in));

        // Проверяем, что первый вызов возвращает true
        assertTrue(strategy.shouldHit(hand), "Первый вызов должен вернуть true при вводе '1'");
        // Проверяем, что второй вызов возвращает false
        assertFalse(strategy.shouldHit(hand), "Второй вызов должен вернуть false при вводе '0'");
    }

    @Test
    void shouldHit_worksWithNonEmptyHand() {
        // Подменяем System.in для эмуляции ввода "1"
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        strategy = new ConsoleDecisionStrategy(new Scanner(System.in));

        // Добавляем карту в руку (заглушка для Card)
        Card mockCard = new Card(Suit.HEARTS, Rank.TEN);
        hand.addCard(mockCard);

        // Проверяем, что метод возвращает true и рука не влияет на результат
        assertTrue(strategy.shouldHit(hand),
                "Метод должен вернуть true при вводе '1' для непустой руки");
        assertEquals(1, hand.getCards().size(), "Рука должна содержать одну карту");
    }
}