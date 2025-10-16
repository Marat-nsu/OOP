package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import model.Card;
import model.Hand;
import model.Rank;
import model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.InputHandler;

class ConsoleDecisionStrategyTest {
    private ConsoleDecisionStrategy strategy;
    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void shouldHit_returnsTrue_whenUserInputsOne() {

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        InputHandler inputHandler = new InputHandler();

        strategy = new ConsoleDecisionStrategy(inputHandler);

        assertTrue(strategy.shouldHit(hand), "Метод должен вернуть true при вводе '1'");
    }

    @Test
    void shouldHit_returnsFalse_whenUserInputsZero() {

        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        InputHandler inputHandler = new InputHandler();

        strategy = new ConsoleDecisionStrategy(inputHandler);

        assertFalse(strategy.shouldHit(hand), "Метод должен вернуть false при вводе '0'");
    }

    @Test
    void shouldHit_handlesInvalidInput_withRetry() {

        String input = "a\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        InputHandler inputHandler = new InputHandler();

        strategy = new ConsoleDecisionStrategy(inputHandler);

        assertTrue(strategy.shouldHit(hand),
                "Метод должен обработать invalid ввод и вернуть true на основе валидного '1'");
    }

    @Test
    void shouldHit_handlesMultipleCallsCorrectly() {

        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        InputHandler inputHandler = new InputHandler();

        strategy = new ConsoleDecisionStrategy(inputHandler);

        assertTrue(strategy.shouldHit(hand), "Первый вызов должен вернуть true при вводе '1'");

        assertFalse(strategy.shouldHit(hand), "Второй вызов должен вернуть false при вводе '0'");
    }

    @Test
    void shouldHit_worksWithNonEmptyHand() {

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        InputHandler inputHandler = new InputHandler();

        Card card = new Card(Suit.HEARTS, Rank.TEN);
        hand.addCard(card);

        strategy = new ConsoleDecisionStrategy(inputHandler);

        assertTrue(strategy.shouldHit(hand),
                "Метод должен вернуть true при вводе '1' для непустой руки");
        assertEquals(1, hand.getCards().size(), "Рука должна содержать одну карту");
    }
}