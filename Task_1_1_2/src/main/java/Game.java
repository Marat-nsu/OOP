/**
 * Класс Game, содержит в себе методы для управления игрой, также хранит в себе состояние игры.
 */
public class Game {
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private int playerScore;
    private int dealerScore;
    private int roundNumber;

    /**
     * Конструктор класса Game.
     *
     * @param decksCount Количество колод в игре.
     * @param player Игрок.
     * @param dealer Дилер.
     */
    public Game(int decksCount, Player player, Dealer dealer) {
        this.deck = new Deck(decksCount);
        this.player = player;
        this.dealer = dealer;
        this.playerScore = 0;
        this.dealerScore = 0;
        this.roundNumber = 1;
    }

    /**
     * Метод для начала нового раунда.
     * Очищает руки игрока и дилера, а затем раздает по две карты каждому.
     */
    public void startNewRound() {
        player.clearHand();
        dealer.clearHand();

        // Раздача начальных карт
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
    }

    /**
     * Определяет победителя в текущем раунде.
     * Сравнивает счета игрока и дилера в зависимости от результатов сравнения их рук.
     * Если один из игроков имеет блэкджек, то другой игрок автоматически выигрывает.
     * Если оба руки имеют одинаковое количество очков, то счет не меняется.
     * После определения победителя раунда, счет раунда увеличивается на 1.
     */
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
