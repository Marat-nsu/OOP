package snake.model;

public class GameConfig {
    private final int rows;
    private final int cols;
    private final int foodCount;
    private final int winLength;
    private final int tickMillis;

    public GameConfig(int rows, int cols, int foodCount, int winLength, int tickMillis) {
        this.rows = rows;
        this.cols = cols;
        this.foodCount = foodCount;
        this.winLength = winLength;
        this.tickMillis = tickMillis;
    }

    public static GameConfig defaultConfig() {
        return new GameConfig(20, 28, 3, 20, 130);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getWinLength() {
        return winLength;
    }

    public int getTickMillis() {
        return tickMillis;
    }
}
