package snake.controller;

import org.junit.jupiter.api.Test;
import snake.model.*;

import static org.junit.jupiter.api.Assertions.*;

public class RobotControllerTest {

    @Test
    public void testAISelectionSafeDirection() {
        GameConfig config = new GameConfig(5, 5, 0, 10, 100);
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(10));
        engine.clearObstacles();
        engine.clearFoods();
        engine.clearRobotSnakes();
        
        RobotSnake robot = new RobotSnake(new Cell(2, 2));
        engine.getRobotSnakes().add(robot);

        RobotController controller = new RobotController();
        controller.update(engine);
        
        Direction dir = robot.getPendingDirection();
        assertNotNull(dir);
        
        Cell next = robot.getHead().move(dir);
        assertTrue(next.getRow() >= 0 && next.getRow() < 5);
        assertTrue(next.getCol() >= 0 && next.getCol() < 5);
    }
}
