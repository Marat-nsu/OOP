import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AceTest {

    @Test
    public void testMultipleAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        // Должно быть 1 + 1 + 1 = 3, а не 11 + 11 + 11 = 33
        assertEquals(13, hand.calculateScore());
    }

    @Test
    public void testAceAdjustment() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        // Должно быть 1 + 10 + 5 = 16, а не 11 + 10 + 5 = 26
        assertEquals(16, hand.calculateScore());
    }
}
