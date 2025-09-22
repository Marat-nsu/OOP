public class Game {
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private int playerScore;
    private int dealerScore;
    private int roundNumber;

    public Game(int decksCount, Player player, Dealer dealer) {
        this.deck = new Deck(decksCount);
        this.player = player;
        this.dealer = dealer;
        this.playerScore = 0;
        this.dealerScore = 0;
        this.roundNumber = 1;
    }

    public void startNewRound() {
        player.clearHand();
        dealer.clearHand();

        // Раздача начальных карт
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
    }

    public void determineRoundWinner() {
        int playerHandValue = player.getHand().calculateScore();
        int dealerHandValue = dealer.getHand().calculateScore();

        if (player.getHand().isBlackjack() && !dealer.getHand().isBlackjack()) {
            playerScore++;
        } else if (!player.getHand().isBlackjack() && dealer.getHand().isBlackjack()) {
            dealerScore++;
        } else if (player.getHand().isBust()) {
            dealerScore++;
        } else if (dealer.getHand().isBust()) {
            playerScore++;
        } else if (playerHandValue > dealerHandValue) {
            playerScore++;
        } else if (playerHandValue < dealerHandValue) {
            dealerScore++;
        }
        // Ничья - счет не меняется

        roundNumber++;
    }

    // Геттеры
    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }
}
