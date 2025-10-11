public class GameUI implements AutoCloseable {
    private final Game game;
    private final InputHandler input;
    private final OutputHandler output;

    public GameUI() {
        this.input = new InputHandler();
        this.output = new OutputHandler();

        output.println("Enter number of decks (1-8): ");
        int decksCount;
        while (true) {
            try {
                decksCount = input.readInt(1, 8);
                break;
            } catch (IllegalArgumentException e) {
                output.println("Invalid number. Please enter 1-8.");
            }
        }

        Player player = new Player("Player", new ConsoleDecisionStrategy(input, output));
        Dealer dealer = new Dealer();
        game = new Game(decksCount, player, dealer);
    }

    public void startGame() {
        output.println("Welcome to Blackjack!\n");

        while (true) {
            output.println("Round " + game.getRoundNumber());
            playRound();

            output.println("Play another round? (1=Yes, 0=No): ");
            int choice;
            while (true) {
                try {
                    choice = input.readInt(0, 1);
                    break;
                } catch (IllegalArgumentException e) {
                    output.println("Invalid choice. Enter 0 or 1.");
                }
            }
            if (choice == 0) {
                break;
            }
        }

        displayFinalResults();
    }

    private void playRound() {
        game.startNewRound();

        output.println("Dealer deals cards");
        displayHands(false);

        if (game.getPlayer().getHand().isBlackjack()
                || game.getDealer().getHand().isBlackjack()) {
            output.println("Blackjack!");
            game.determineRoundWinner();
            displayRoundResult();
            return;
        }

        playerTurn();

        if (!game.getPlayer().getHand().isBust()) {
            dealerTurn();
        }

        game.determineRoundWinner();
        displayRoundResult();
    }

    private void playerTurn() {
        output.println("\nYour turn");
        output.println("---");

        while (true) {
            output.println("Hit or stand? (1=Hit, 0=Stand): ");
            int choice;
            while (true) {
                try {
                    choice = input.readInt(0, 1);
                    break;
                } catch (IllegalArgumentException e) {
                    output.println("Invalid input. Enter 0 or 1.");
                }
            }

            if (choice == 1) {
                Card drawnCard = game.getDeck().drawCard();
                game.getPlayer().takeCard(drawnCard);
                output.println("You drew: " + drawnCard);
                displayHands(false);

                if (game.getPlayer().getHand().isBust()) {
                    output.println("Bust!");
                    break;
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    private void dealerTurn() {
        output.println("\nDealer's turn");
        output.println("---");

        Card hiddenCard = game.getDealer().getHand().getCards().get(1);
        output.println("Dealer reveals hidden card: " + hiddenCard);
        displayHands(true);

        while (game.getDealer().wantsToHit()) {
            Card drawnCard = game.getDeck().drawCard();
            output.println("Dealer draws: " + drawnCard);
            game.getDealer().takeCard(drawnCard);
            displayHands(true);

            if (game.getDealer().getHand().isBust()) {
                output.println("Dealer busts!");
                break;
            }
        }
    }

    private void displayHands(boolean showDealerHand) {
        output.println("Player's hand: [" + game.getPlayer().getHand().getCards().get(0) + ", " +
                game.getPlayer().getHand().getCards().get(1) + "] (score: " +
                game.getPlayer().getHand().score() + ")");

        if (showDealerHand) {
            output.println("Dealer's hand: [" + game.getDealer().getHand() + "] (score: " +
                    game.getDealer().getHand().score() + ")");
        } else {
            output.println("Dealer's hand: [" +
                    game.getDealer().getHand().getCards().get(0)
                    + ", <Hidden card>]");
        }
    }

    private void displayRoundResult() {
        int playerHand = game.getPlayer().getHand().score();
        int dealerHand = game.getDealer().getHand().score();

        output.println("\nRound result:");
        output.println("Player hand: " + playerHand + ", Dealer hand: " + dealerHand);

        String winnerMessage;
        if (game.getPlayer().getHand().isBust()) {
            winnerMessage = "Dealer wins!";
        } else if (game.getDealer().getHand().isBust()) {
            winnerMessage = "Player wins!";
        } else {
            switch (Integer.compare(playerHand, dealerHand)) {
                case 1 -> winnerMessage = "Player wins!";
                case -1 -> winnerMessage = "Dealer wins!";
                default -> winnerMessage = "Draw!";
            }
        }
        output.println(winnerMessage);

        output.println("Current score: Player " + game.getPlayerScore() + " - Dealer " + game.getDealerScore());
    }

    private void displayFinalResults() {
        output.println("Game over!");
        output.println("Final score: Player " + game.getPlayerScore() + " - Dealer " + game.getDealerScore());
    }

    @Override
    public void close() {
        input.close();
    }
}