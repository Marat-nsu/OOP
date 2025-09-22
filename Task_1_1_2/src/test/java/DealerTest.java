import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования класса Dealer.
 */
public class DealerTest {

    @Test
    public void testDealerCreation() {
        Dealer dealer = new Dealer();
        assertEquals("Дилер", dealer.getName());
        assertNotNull(dealer.getHand());
    }

    @Test
    public void testDealerDecision() {
        Dealer dealer = new Dealer();

        // При пустой руке должен брать карты (score = 0 < 17)
        assertTrue(dealer.wantsToHit());

        // Добавляем карты чтобы сумма была меньше 17
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.SIX));
        assertTrue(dealer.wantsToHit()); // 16 < 17

        // Добавляем карту чтобы сумма стала 17
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.ACE));
        assertFalse(dealer.wantsToHit()); // 17 >= 17
    }
}
