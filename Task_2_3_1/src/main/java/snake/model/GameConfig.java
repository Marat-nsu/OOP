package snake.model;

public class GameConfig {
    private final int rows;
    private final int cols;
    private final int foodCount;
    private final int obstacleCount;
    private final int robotCount;
    private final int winLength;
    private final int tickMillis;

    public GameConfig(int rows, int cols, int foodCount, int winLength, int tickMillis) {
        this(rows, cols, foodCount, 18, 2, winLength, tickMillis);
    }

    public GameConfig(int rows, int cols, int foodCount, int obstacleCount, int robotCount, int winLength, int tickMillis) {
        this.rows = rows;
        this.cols = cols;
        this.foodCount = foodCount;
        this.obstacleCount = obstacleCount;
        this.robotCount = robotCount;
        this.winLength = winLength;
        this.tickMillis = tickMillis;
    }

    public static GameConfig defaultConfig() {
        return new GameConfig(20, 28, 3, 18, 2, 20, 130);
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

    public int getObstacleCount() {
        return obstacleCount;
    }

    public int getRobotCount() {
        return robotCount;
    }

    public int getWinLength() {
        return winLength;
    }

    public int getTickMillis() {
        return tickMillis;
    }
}
