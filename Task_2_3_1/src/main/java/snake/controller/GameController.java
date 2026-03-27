package snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import snake.model.Cell;
import snake.model.Direction;
import snake.model.Food;
import snake.model.GameConfig;
import snake.model.GameEngine;
import snake.model.GameStatus;
import snake.model.LengthVictoryCondition;

public class GameController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    private Label scoreLabel;

    private GameEngine engine;
    private Timeline timeline;

    @FXML
    private void initialize() {
        GameConfig config = GameConfig.defaultConfig();
        engine = new GameEngine(config, new LengthVictoryCondition(config.getWinLength()));
        timeline = new Timeline(new KeyFrame(Duration.millis(config.getTickMillis()), event -> onTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        updateStatus();
        render();
    }

    public void bindScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    public void onWindowShown() {
        gameCanvas.requestFocus();
    }

    @FXML
    private void onStartClicked() {
        if (engine.getStatus() == GameStatus.READY) {
            engine.start();
            timeline.play();
            updateStatus();
        }
    }

    @FXML
    private void onRestartClicked() {
        timeline.stop();
        engine.reset();
        updateStatus();
        render();
    }

    private void handleKeyPressed(KeyEvent event) {
        Direction direction = null;
        KeyCode code = event.getCode();
        if (code == KeyCode.UP || code == KeyCode.W) {
            direction = Direction.UP;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            direction = Direction.RIGHT;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            direction = Direction.DOWN;
        } else if (code == KeyCode.LEFT || code == KeyCode.A) {
            direction = Direction.LEFT;
        }

        if (direction != null) {
            if (engine.getStatus() == GameStatus.READY) {
                engine.start();
                timeline.play();
            }
            engine.changeDirection(direction);
            event.consume();
        }
    }

    private void onTick() {
        engine.step();
        render();
        updateStatus();

        if (engine.getStatus() == GameStatus.WON || engine.getStatus() == GameStatus.LOST) {
            timeline.stop();
        }
    }

    private void updateStatus() {
        int currentLength = engine.getSnake().length();
        int targetLength = engine.getConfig().getWinLength();
        scoreLabel.setText("Length: " + currentLength + " / " + targetLength);

        GameStatus status = engine.getStatus();
        if (status == GameStatus.READY) {
            statusLabel.setText("Press Start or Arrow keys to play");
        } else if (status == GameStatus.RUNNING) {
            statusLabel.setText("Running");
        } else if (status == GameStatus.WON) {
            statusLabel.setText("You won!");
        } else {
            statusLabel.setText("Game over");
        }
    }

    private void render() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        double width = gameCanvas.getWidth();
        double height = gameCanvas.getHeight();
        int rows = engine.getConfig().getRows();
        int cols = engine.getConfig().getCols();
        double cellSize = Math.min(width / cols, height / rows);

        gc.setFill(Color.web("#0D1117"));
        gc.fillRect(0, 0, width, height);

        gc.setStroke(Color.web("#1E293B"));
        gc.setLineWidth(1);
        for (int r = 0; r <= rows; r++) {
            double y = r * cellSize;
            gc.strokeLine(0, y, cols * cellSize, y);
        }
        for (int c = 0; c <= cols; c++) {
            double x = c * cellSize;
            gc.strokeLine(x, 0, x, rows * cellSize);
        }

        for (Food food : engine.getFoods()) {
            drawCell(gc, food.getPosition(), cellSize, Color.web("#F59E0B"));
        }

        for (Cell obstacle : engine.getObstacles()) {
            drawCell(gc, obstacle, cellSize, Color.web("#94A3B8"));
        }

        boolean first = true;
        for (Cell segment : engine.getSnake().getSegments()) {
            Color color = first ? Color.web("#22C55E") : Color.web("#16A34A");
            drawCell(gc, segment, cellSize, color);
            first = false;
        }
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
