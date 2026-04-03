package snake.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    private void cleanEngine(GameEngine engine) {
        engine.clearObstacles();
        engine.clearRobotSnakes();
        engine.clearFoods();
    }

    @Test
    public void testMoveCollisionWithWall() {
        GameConfig config = new GameConfig(5, 5, 0, 10, 500); 
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(10));
        cleanEngine(engine);
        engine.start();
        
        assertEquals(GameStatus.RUNNING, engine.getStatus(), "Should be READY/RUNNING after start");
        engine.step(); 
        assertEquals(GameStatus.RUNNING, engine.getStatus(), "Failed step to (2,3), status: " + engine.getStatus());
        
        engine.step();
        assertEquals(GameStatus.RUNNING, engine.getStatus(), "Failed step to (2,4), status: " + engine.getStatus());
        
        engine.step();
        assertEquals(GameStatus.LOST, engine.getStatus(), "Should be LOST at (2,5), status: " + engine.getStatus());
    }

    @Test
    public void testEatFoodAndWin() {
        GameConfig config = new GameConfig(10, 10, 1, 2, 500); 
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(2));
        cleanEngine(engine);
        engine.start();
        
        Cell head = engine.getSnake().getHead();
        Cell foodPosition = head.move(Direction.RIGHT);
        engine.addFood(new Food(foodPosition, FoodType.BASIC));
        
        engine.step();
        
        assertEquals(2, engine.getSnake().length(), "Length should be 2 after eating food");
        assertEquals(GameStatus.WON, engine.getStatus(), "Status should be WON, actual: " + engine.getStatus());
    }

    @Test
    public void testLevelTransitionResetsSnake() {
        GameConfig config = new GameConfig(10, 10, 1, 5, 100);
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(5));
        engine.clearObstacles();
        engine.clearRobotSnakes();
        engine.clearFoods();
        
        Snake snake = engine.getSnake();
        snake.move(new Cell(1, 1), 1);
        snake.move(new Cell(1, 2), 1);
        assertEquals(3, snake.length());
        
        GameConfig config2 = new GameConfig(10, 10, 1, 10, 50);
        engine.setConfig(config2);
        engine.reset();
        
        assertEquals(1, engine.getSnake().length());
        assertEquals(GameStatus.READY, engine.getStatus());
    }

    @Test
    public void testMoveCollisionWithObstacle() {
        GameConfig config = new GameConfig(10, 10, 0, 10, 500);
        GameEngine engine = new GameEngine(config, new LengthVictoryCondition(10));
        cleanEngine(engine);
        engine.start();
        
        Cell head = engine.getSnake().getHead();
        Cell obstaclePosition = head.move(Direction.RIGHT);
        engine.addObstacle(obstaclePosition);
        
        engine.step();
        assertEquals(GameStatus.LOST, engine.getStatus(), "Should hit obstacle at " + obstaclePosition);
    }
}
