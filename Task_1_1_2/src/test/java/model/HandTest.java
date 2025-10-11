package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HandTest {

    @Test
    public void testEmptyHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.score());
        assertFalse(hand.isBlackjack());
        assertFalse(hand.isBust());
    }

    @Test
    public void testHandWithCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));

        assertEquals(17, hand.score());
        assertFalse(hand.isBlackjack());
        assertFalse(hand.isBust());
    }

    @Test
    public void testBlackjack() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));

        assertEquals(21, hand.score());
        assertTrue(hand.isBlackjack());
        assertFalse(hand.isBust());
    }

    @Test
    public void testBust() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertEquals(22, hand.score());
        assertFalse(hand.isBlackjack());
        assertTrue(hand.isBust());
    }

    @Test
    public void testAceAdjustment() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        // Должно быть 1 + 1 + 1 = 3, а не 11 + 11 + 11 = 33
        assertEquals(13, hand.score());
    }

    @Test
    public void testClearCard() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));

        // Должно быть 1 + 1 + 1 = 3, а не 11 + 11 + 11 = 33
        assertEquals(13, hand.score());
        hand.clear();
        assertEquals(0, hand.score());
    }
}
