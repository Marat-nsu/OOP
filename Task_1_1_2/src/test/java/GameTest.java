import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования класса Game.
 */
public class GameTest {

    @Test
    public void testGameInitialization() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        Game game = new Game(1, player, dealer);

        assertEquals(1, game.getRoundNumber());
        assertEquals(0, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
    }

    @Test
    public void testStartNewRound() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        Game game = new Game(1, player, dealer);

        game.startNewRound();

        assertEquals(2, player.getHand().getCards().size());
        assertEquals(2, dealer.getHand().getCards().size());
    }

    @Test
    public void testDetermineRoundWinnerPlayerBlackjack() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Создаем блэкджек для игрока и обычную руку для дилера
        player.takeCard(new Card(Suit.HEARTS, Rank.ACE));
        player.takeCard(new Card(Suit.SPADES, Rank.KING));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.SIX));

        game.determineRoundWinner();

        assertEquals(1, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
        assertEquals(2, game.getRoundNumber()); // Номер раунда должен увеличиться
    }

    @Test
    public void testDetermineRoundWinnerDealerBlackjack() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Создаем обычную руку для игрока и блэкджек для дилера
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.SEVEN));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.ACE));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.KING));

        game.determineRoundWinner();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }

    @Test
    public void testDetermineRoundWinnerPlayerBust() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Создаем перебор для игрока
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.takeCard(new Card(Suit.DIAMONDS, Rank.THREE));

        dealer.takeCard(new Card(Suit.CLUBS, Rank.FIVE));
        dealer.takeCard(new Card(Suit.HEARTS, Rank.SIX));

        game.determineRoundWinner();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }

    @Test
    public void testDetermineRoundWinnerDealerBust() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Создаем перебор для дилера
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.KING));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.QUEEN));
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TWO));

        game.determineRoundWinner();

        assertEquals(1, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }

    @Test
    public void testDetermineRoundWinnerPush() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Создаем ничью (одинаковое количество очков)
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.KING));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.QUEEN));

        game.determineRoundWinner();

        assertEquals(0, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }

    @Test
    public void testDetermineRoundWinnerPlayerWins() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Игрок выигрывает по очкам
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.KING));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.NINE));

        game.determineRoundWinner();

        assertEquals(1, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }

    @Test
    public void testDetermineRoundWinnerDealerWins() {
        Player player = new Player("Test", hand -> false);
        Dealer dealer = new Dealer();
        final Game game = new Game(1, player, dealer);

        // Дилер выигрывает по очкам
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.NINE));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.KING));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.QUEEN));

        game.determineRoundWinner();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
        assertEquals(2, game.getRoundNumber());
    }
}
