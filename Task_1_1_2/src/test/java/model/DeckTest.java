package model;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class DeckTest {

    @Test
    public void testShuffle() {
        Deck deck1 = new Deck(1);
        Deck deck2 = new Deck(1);

        // Не идеальный тест, но проверяет что колоды разные после перемешивания
        boolean allEqual = true;
        for (int i = 0; i < 10; i++) {
            Card card1 = deck1.drawCard();
            Card card2 = deck2.drawCard();
            if (!card1.toString().equals(card2.toString())) {
                allEqual = false;
                break;
            }
        }
        assertFalse(allEqual);
    }
}
