package ui;

import core.Game;
import model.Card;
import model.Dealer;
import model.Player;
import strategy.ConsoleDecisionStrategy;

public class GameUI implements AutoCloseable {
    private final Game game;
    private final InputHandler input;
    private final OutputHandler output;
    private final LocalizationLoader localization;

    public GameUI(String language) {
        this.input = new InputHandler();
        this.output = new OutputHandler();
        this.localization = new LocalizationLoader(language != null ? language : "en");

        output.println(localization.get("enterDecks"));
        int decksCount;
        while (true) {
            try {
                decksCount = input.readInt(1, 8);
                break;
            } catch (IllegalArgumentException e) {
                output.println(localization.get("invalidDecks"));
            }
        }

        Player player = new Player("Player", new ConsoleDecisionStrategy(input));
        Dealer dealer = new Dealer();
        game = new Game(decksCount, player, dealer);
    }

    public void startGame() {
        output.println(localization.get("welcomeMessage"));

        while (true) {
            output.println(localization.get("roundPrefix", game.getRoundNumber()));
            playRound();

            output.println(localization.get("playAnotherRound"));
            int choice;
            while (true) {
                try {
                    choice = input.readInt(0, 1);
                    break;
                } catch (IllegalArgumentException e) {
                    output.println(localization.get("invalidChoice"));
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

        output.println(localization.get("dealerDealsCards"));
        displayHands(false);

        if (game.getPlayer().getHand().isBlackjack()
                || game.getDealer().getHand().isBlackjack()) {
            output.println(localization.get("blackjack"));
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
        output.println(localization.get("yourTurn"));
        output.println(localization.get("separator"));

        while (true) {
            output.println(localization.get("hitOrStand"));
            int choice;
            while (true) {
                try {
                    choice = input.readInt(0, 1);
                    break;
                } catch (IllegalArgumentException e) {
                    output.println(localization.get("invalidInput"));
                }
            }

            if (choice == 1) {
                Card drawnCard = game.getDeck().drawCard();
                game.getPlayer().takeCard(drawnCard);
                output.println(localization.get("youDrew") + drawnCard);
                displayHands(false);

                if (game.getPlayer().getHand().isBust()) {
                    output.println(localization.get("bust"));
                    break;
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    private void dealerTurn() {
        output.println(localization.get("dealersTurn"));
        output.println(localization.get("separator"));

        Card hiddenCard = game.getDealer().getHand().getCards().get(1);
        output.println(localization.get("dealerRevealsHidden") + hiddenCard);
        displayHands(true);

        while (game.getDealer().wantsToHit()) {
            Card drawnCard = game.getDeck().drawCard();
            output.println(localization.get("dealerDraws") + drawnCard);
            game.getDealer().takeCard(drawnCard);
            displayHands(true);

            if (game.getDealer().getHand().isBust()) {
                output.println(localization.get("dealerBusts"));
                break;
            }
        }
    }

    private void displayHands(boolean showDealerHand) {
        output.println(game.getPlayer().getHand().formatForDisplay(true, localization));
        output.println(game.getDealer().getHand().formatForDisplay(showDealerHand, localization));
    }

    private void displayRoundResult() {
        int playerHand = game.getPlayer().getHand().score();
        int dealerHand = game.getDealer().getHand().score();

        output.println(localization.get("roundResultPrefix"));
        output.println(localization.get("playerHandPrefix") + playerHand
                + localization.get("dealerHandSuffix") + dealerHand);

        String winnerMessage;
        if (game.getPlayer().getHand().isBust()) {
            winnerMessage = localization.get("dealerWins");
        } else if (game.getDealer().getHand().isBust()) {
            winnerMessage = localization.get("playerWins");
        } else {
            switch (Integer.compare(playerHand, dealerHand)) {
                case 1 -> winnerMessage = localization.get("playerWins");
                case -1 -> winnerMessage = localization.get("dealerWins");
                default -> winnerMessage = localization.get("draw");
            }
        }
        output.println(winnerMessage);

        output.println(localization.get("currentScorePrefix") + game.getPlayerScore()
                + localization.get("scoreSeparator") + game.getDealerScore());
    }

    private void displayFinalResults() {
        output.println(localization.get("gameOver"));
        output.println(localization.get("finalScorePrefix") + game.getPlayerScore()
                + localization.get("scoreSeparator") + game.getDealerScore());
    }

    @Override
    public void close() {
        input.close();
    }
}