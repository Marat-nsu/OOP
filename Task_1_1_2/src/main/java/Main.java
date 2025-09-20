public class Main {
	public static void main(String[] args) {
		GameUI gameUI = new GameUI();
		try {
			gameUI.startGame();
		} finally {
			gameUI.close();
		}
	}
}
