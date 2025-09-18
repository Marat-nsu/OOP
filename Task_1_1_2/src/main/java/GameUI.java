import java.util.Scanner;

public class GameUI {
	private Game game;
	private Scanner scanner;

	public GameUI() {
		scanner = new Scanner(System.in);
		System.out.print("Введите количество колод (1-8): ");
		int decksCount = scanner.nextInt();

		Player player = new Player("Игрок", new ConsoleDecisionStrategy(scanner));
		Dealer dealer = new Dealer();
		game = new Game(decksCount, player, dealer);
	}

	public void startGame() {
		System.out.println("Добро пожаловать в Блэкджек!");

		while (true) {
			System.out.println("\nРаунд " + game.getRoundNumber());
			playRound();

			System.out.print("Хотите сыграть еще раунд? (1 - да, 0 - нет): ");
			int choice = scanner.nextInt();
			if (choice == 0) {
				break;
			}
		}

		displayFinalResults();
	}

	private void playRound() {
		game.startNewRound();

		System.out.println("Дилер раздал карты");
		displayHands(false);

		// Проверка на блэкджек
		if (game.getPlayer().getHand().isBlackjack() ||
				game.getDealer().getHand().isBlackjack()) {
			System.out.println("Обнаружен блэкджек!");
			game.determineRoundWinner();
			displayRoundResult();
			return;
		}

		// Ход игрока
		playerTurn();

		// Ход дилера, если игрок не проиграл
		if (!game.getPlayer().getHand().isBust()) {
			dealerTurn();
		}

		game.determineRoundWinner();
		displayRoundResult();
	}

	private void playerTurn() {
		System.out.println("\nВаш ход");
		System.out.println("-------");

		while (true) {
			System.out.print("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться: ");
			int choice = scanner.nextInt();

			if (choice == 1) {
				Card drawnCard = game.getDeck().drawCard();
				game.getPlayer().takeCard(drawnCard);
				System.out.println("Вы открыли карту " + drawnCard);
				displayHands(false);

				if (game.getPlayer().getHand().isBust()) {
					System.out.println("Перебор! Сумма ваших карт превысила 21.");
					break;
				}
			} else if (choice == 0) {
				break;
			}
		}
	}

	private void dealerTurn() {
		System.out.println("\nХод дилера");
		System.out.println("-------");

		// Дилер открывает закрытую карту
		Card hiddenCard = game.getDealer().getHand().getCards().get(1);
		System.out.println("Дилер открывает закрытую карту " + hiddenCard);
		displayHands(true);

		// Дилер берет карты по правилам
		while (game.getDealer().wantsToHit()) {
			Card drawnCard = game.getDeck().drawCard();
			System.out.println("Дилер открывает карту " + drawnCard);
			game.getDealer().takeCard(drawnCard);
			displayHands(true);

			if (game.getDealer().getHand().isBust()) {
				System.out.println("У дилера перебор!");
				break;
			}
		}
	}

	private void displayHands(boolean showDealerHand) {
		System.out.println("Ваши карты: [" + game.getPlayer().getHand() + "] > " +
				game.getPlayer().getHand().calculateScore());

		if (showDealerHand) {
			System.out.println("Карты дилера: [" + game.getDealer().getHand() + "] > " +
					game.getDealer().getHand().calculateScore());
		} else {
			// Показываем только первую карту дилера
			System.out.println("Карты дилера: [" + game.getDealer().getHand().getCards().get(0) +
					", <закрытая карта>]");
		}
	}

	private void displayRoundResult() {
		int playerScore = game.getPlayerScore();
		int dealerScore = game.getDealerScore();

		System.out.println("\nРезультат раунда:");
		System.out.println("Счет: Игрок " + playerScore + " : " + dealerScore + " Дилер");

		if (playerScore > dealerScore) {
			System.out.println("Вы выиграли раунд!");
		} else if (playerScore < dealerScore) {
			System.out.println("Дилер выиграл раунд.");
		} else {
			System.out.println("Ничья в раунде.");
		}
	}

	private void displayFinalResults() {
		System.out.println("\nИгра завершена!");
		System.out.println("Финальный счет: Игрок " + game.getPlayerScore() +
				" : " + game.getDealerScore() + " Дилер");
	}

	public void close() {
		scanner.close();
	}
}
