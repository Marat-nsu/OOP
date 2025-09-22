/**
 * Основной класс, запускает игру.
 */
public class Main {
    /**
     * Метод для запуска игры. Создает экземпляр класса GameUI,
     * вызывает метод startGame() для начала игры, а затем вызывает
     * метод close() для закрытия ресурсов, используемых игрой.
     */
    public static void main(String[] args) {
        GameUI gameUI = new GameUI();
        try {
            gameUI.startGame();
        } finally {
            gameUI.close();
        }
    }
}
