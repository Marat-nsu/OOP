package snake.view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import snake.model.*;

public class GameViewTest {

    @BeforeAll
    public static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        } catch (UnsupportedOperationException e) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false, "Skipping GUI test on headless environment.");
        }
    }

    @Test
    public void testFullRedrawAndUpdate() {
        Canvas canvas = new Canvas(100, 100);
        GameView view = new GameView(canvas);

        GameConfig config = new GameConfig(10, 10, 0, 10, 100);
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(10));
        engine.clearObstacles();
        engine.clearFoods();
        engine.addObstacle(new Cell(1, 1));
        engine.addFood(new Food(new Cell(2, 2), FoodType.BASIC));
        RobotSnake robot = new RobotSnake(new Cell(3, 3));
        engine.getRobotSnakes().add(robot);

        view.fullRedraw(engine);
        
        engine.start();
        engine.step();
        
        view.update(engine);
    }
}
