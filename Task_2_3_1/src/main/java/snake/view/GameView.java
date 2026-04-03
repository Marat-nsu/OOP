package snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import snake.model.*;

import java.util.HashMap;
import java.util.Map;

public class GameView {
    private final Canvas canvas;
    private final Map<Cell, Color> lastState = new HashMap<>();
    
    private final Color bgColor = Color.web("#0D1117");
    private final Color gridColor = Color.web("#1E293B");
    private final Color snakeHeadColor = Color.web("#22C55E");
    private final Color snakeBodyColor = Color.web("#16A34A");
    private final Color robotHeadColor = Color.web("#EF4444");
    private final Color robotBodyColor = Color.web("#B91C1C");
    private final Color foodColor = Color.web("#F59E0B");
    private final Color obstacleColor = Color.web("#94A3B8");

    public GameView(Canvas canvas) {
        this.canvas = canvas;
    }

    public void fullRedraw(GameEngine engine) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        int rows = engine.getConfig().getRows();
        int cols = engine.getConfig().getCols();
        double cellSize = calculateCellSize(engine.getConfig());

        
        gc.setFill(bgColor);
        gc.fillRect(0, 0, width, height);

        
        gc.setStroke(gridColor);
        gc.setLineWidth(1);
        for (int r = 0; r <= rows; r++) {
            double y = r * cellSize;
            gc.strokeLine(0, y, cols * cellSize, y);
        }
        for (int c = 0; c <= cols; c++) {
            double x = c * cellSize;
            gc.strokeLine(x, 0, x, rows * cellSize);
        }

        lastState.clear();
        update(engine);
    }

    public void update(GameEngine engine) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cellSize = calculateCellSize(engine.getConfig());
        
        Map<Cell, Color> currentState = new HashMap<>();

        
        for (Food food : engine.getFoods()) {
            currentState.put(food.getPosition(), foodColor);
        }
        for (Cell obstacle : engine.getObstacles()) {
            currentState.put(obstacle, obstacleColor);
        }
        
        
        boolean first = true;
        for (Cell segment : engine.getSnake().getSegments()) {
            currentState.put(segment, first ? snakeHeadColor : snakeBodyColor);
            first = false;
        }

        
        for (RobotSnake robot : engine.getRobotSnakes()) {
            first = true;
            for (Cell segment : robot.getSegments()) {
                currentState.put(segment, first ? robotHeadColor : robotBodyColor);
                first = false;
            }
        }

        for (Cell cell : lastState.keySet()) {
            if (!currentState.containsKey(cell)) {
                clearCell(gc, cell, cellSize);
            }
        }

        for (Map.Entry<Cell, Color> entry : currentState.entrySet()) {
            Cell cell = entry.getKey();
            Color color = entry.getValue();
            if (!color.equals(lastState.get(cell))) {
                drawCell(gc, cell, cellSize, color);
            }
        }

        lastState.clear();
        lastState.putAll(currentState);
    }

    private double calculateCellSize(GameConfig config) {
        return Math.min(canvas.getWidth() / config.getCols(), canvas.getHeight() / config.getRows());
    }

    private void clearCell(GraphicsContext gc, Cell cell, double cellSize) {
        double x = cell.getCol() * cellSize;
        double y = cell.getRow() * cellSize;
        gc.setFill(bgColor);
        gc.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2); 
    }

    private void drawCell(GraphicsContext gc, Cell cell, double cellSize, Color color) {
        double x = cell.getCol() * cellSize;
        double y = cell.getRow() * cellSize;
        double padding = Math.max(1.5, cellSize * 0.08);

        gc.setFill(color);
        gc.fillRoundRect(
            x + padding, y + padding, cellSize - 2 * padding,
            cellSize - 2 * padding, 6, 6);
    }
}
