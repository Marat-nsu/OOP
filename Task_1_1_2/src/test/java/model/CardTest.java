package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ui.LocalizationLoader;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(11, card.getValue());
    }

    @Test
    public void testCardToString() {
        Card card = new Card(Suit.SPADES, Rank.QUEEN);
        LocalizationLoader loc = new LocalizationLoader("en");
        assertEquals("Queen Spades (10)", card.toString(loc));
    }
}
