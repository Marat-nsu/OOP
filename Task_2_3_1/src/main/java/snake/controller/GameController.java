package snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import snake.model.Direction;
import snake.model.GameConfig;
import snake.model.GameEngine;
import snake.model.GameStatus;
import snake.model.LengthVictoryCondition;
import snake.model.LevelManager;
import snake.view.GameView;

public class GameController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    private Label scoreLabel;
    
    @FXML
    private Label levelLabel;

    private GameEngine engine;
    private GameView view;
    private LevelManager levelManager;
    private RobotController robotController;
    private Timeline timeline;
    private boolean running;
    private boolean tickInProgress;


    public void bindScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    public void onWindowShown() {
        gameCanvas.requestFocus();
    }

    @FXML
    private void initialize() {
        levelManager = new LevelManager();
        robotController = new RobotController();
        GameConfig config = levelManager.getCurrentLevel();
        engine = new GameEngine(config, new LengthVictoryCondition(config.getWinLength()));
        view = new GameView(gameCanvas);
        
        setupTimeline(config.getTickMillis());
        updateStatus();
        view.fullRedraw(engine);
    }

    private void setupTimeline(int millis) {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(millis), event -> onTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        if (running) {
            timeline.play();
        }
    }

    @FXML
    private void onStartClicked() {
        if (engine.getStatus() == GameStatus.READY) {
            engine.start();
            running = true;
            timeline.play();
            updateStatus();
        }
    }

    @FXML
    private void onRestartClicked() {
        running = false;
        timeline.stop();
        levelManager.reset();
        GameConfig config = levelManager.getCurrentLevel();
        engine.setConfig(config);
        engine.setVictoryCondition(new LengthVictoryCondition(config.getWinLength()));
        engine.reset();
        setupTimeline(config.getTickMillis());
        updateStatus();
        view.fullRedraw(engine);
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
                running = true;
                timeline.play();
            }
            engine.changeDirection(direction);
            event.consume();
        }
    }

    private void processTick() {
        robotController.update(engine);
        engine.step();
        view.update(engine);
        updateStatus();

        if (engine.getStatus() == GameStatus.WON) {
            if (levelManager.nextLevel()) {
                GameConfig nextConfig = levelManager.getCurrentLevel();
                engine.setConfig(nextConfig);
                engine.setVictoryCondition(new LengthVictoryCondition(nextConfig.getWinLength()));
                engine.reset();
                setupTimeline(nextConfig.getTickMillis());
                running = true;
                view.fullRedraw(engine);
                updateStatus();
            } else {
                running = false;
                timeline.stop();
            }
        } else if (engine.getStatus() == GameStatus.LOST) {
            running = false;
            timeline.stop();
        }
    }

    private void onTick() {
        if (!running || tickInProgress) {
            return;
        }

        tickInProgress = true;
        timeline.stop();
        try {
            processTick();
        } finally {
            tickInProgress = false;
            if (running) {
                timeline.play();
            }
        }
    }

    private void updateStatus() {
        int currentLength = engine.getSnake().length();
        int targetLength = engine.getConfig().getWinLength();
        scoreLabel.setText("Length: " + currentLength + " / " + targetLength);
        levelLabel.setText("Level: " + levelManager.getCurrentLevelNumber());

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

}
