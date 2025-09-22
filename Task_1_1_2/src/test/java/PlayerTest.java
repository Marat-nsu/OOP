import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testPlayerCreation() {
        DecisionStrategy strategy = hand -> false;
        Player player = new Player("Test", strategy);

        assertEquals("Test", player.getName());
        assertNotNull(player.getHand());
    }

    @Test
    public void testTakeCard() {
        DecisionStrategy strategy = hand -> false;
        Player player = new Player("Test", strategy);
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        player.takeCard(card);
        assertEquals(1, player.getHand().getCards().size());
        assertEquals(card, player.getHand().getCards().get(0));
    }

    @Test
    public void testClearHand() {
        DecisionStrategy strategy = hand -> false;
        Player player = new Player("Test", strategy);
        player.takeCard(new Card(Suit.HEARTS, Rank.ACE));

        player.clearHand();
        assertEquals(0, player.getHand().getCards().size());
    }
}
