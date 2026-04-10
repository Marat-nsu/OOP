package snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GameEngine {
    private static final int OBSTACLE_SAFETY_RADIUS = 1;

    private GameConfig config;
    private VictoryCondition victoryCondition;
    private final SpawnService spawnService;

    private Snake snake;
    private final List<RobotSnake> robotSnakes;
    private Direction currentDirection;
    private Direction pendingDirection;
    private GameStatus status;
    private final Set<Cell> obstacles;
    private final List<Food> foods;

    public GameEngine(GameConfig config, VictoryCondition victoryCondition) {
        this.config = config;
        this.victoryCondition = victoryCondition;
        this.spawnService = new SpawnService(new java.util.Random());
        this.obstacles = new HashSet<>();
        this.foods = new ArrayList<>();
        this.robotSnakes = new ArrayList<>();
        reset();
    }

    public void reset() {
        Cell start = new Cell(config.getRows() / 2, config.getCols() / 2);
        snake = new Snake(start);
        currentDirection = Direction.RIGHT;
        pendingDirection = Direction.RIGHT;
        status = GameStatus.READY;
        obstacles.clear();
        obstacles.addAll(spawnService.generateObstacles(config, config.getObstacleCount(), new HashSet<>(Collections.singletonList(start)), OBSTACLE_SAFETY_RADIUS));
        foods.clear();
        spawnFoodToTarget();
        robotSnakes.clear();
        spawnRobots();
    }

    public void nextLevelReset() {
        status = GameStatus.READY;
        obstacles.clear();
        
        Set<Cell> reserved = new HashSet<>(snake.getSegments());
        for (RobotSnake robot : robotSnakes) {
            reserved.addAll(robot.getSegments());
        }
        
        obstacles.addAll(spawnService.generateObstacles(config, config.getObstacleCount(), reserved, OBSTACLE_SAFETY_RADIUS));
        foods.clear();
        spawnFoodToTarget();
        
        robotSnakes.clear();
        spawnRobots();
    }

    public void start() {
        if (status == GameStatus.READY) {
            status = GameStatus.RUNNING;
        }
    }

    public void changeDirection(Direction newDirection) {
        if (status == GameStatus.LOST || status == GameStatus.WON) {
            return;
        }

        if (snake.length() > 1) {
            Direction source = currentDirection;
            if (newDirection.isOpposite(source)) {
                return;
            }
        }
        pendingDirection = newDirection;
    }

    public void step() {
        if (status != GameStatus.RUNNING) {
            return;
        }

        currentDirection = pendingDirection;
        Cell nextHead = snake.getHead().move(currentDirection);
        Food eaten = getFoodAt(nextHead);
        int growth = eaten == null ? 0 : eaten.getType().getGrowth();

        if (CollisionService.playerLoses(nextHead, growth, snake, obstacles, robotSnakes, config)) {
            status = GameStatus.LOST;
            return;
        }

        if (eaten != null) {
            foods.remove(eaten);
        }

        snake.move(nextHead, growth);
        
        Iterator<RobotSnake> it = robotSnakes.iterator();
        while (it.hasNext()) {
            RobotSnake robot = it.next();
            Direction robotDir = robot.getPendingDirection();
            Cell robotNext = robot.getHead().move(robotDir);
            
            if (CollisionService.robotDies(robotNext, robot, snake, obstacles, robotSnakes, config)) {
                it.remove();
                continue;
            }
            
            Food robotEaten = getFoodAt(robotNext);
            int robotGrowth = robotEaten == null ? 0 : robotEaten.getType().getGrowth();
            if (robotEaten != null) {
                foods.remove(robotEaten);
            }
            
            robot.move(robotNext, robotGrowth);
        }

        spawnFoodToTarget();

        if (victoryCondition.isWon(snake)) {
            status = GameStatus.WON;
        }
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    public void setVictoryCondition(VictoryCondition victoryCondition) {
        this.victoryCondition = victoryCondition;
    }


    public GameConfig getConfig() {
        return config;
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Food> getFoods() {
        return new ArrayList<>(foods);
    }

    public Set<Cell> getObstacles() {
        return new HashSet<>(obstacles);
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<RobotSnake> getRobotSnakes() {
        return new ArrayList<>(robotSnakes);
    }

    public void clearObstacles() {
        obstacles.clear();
    }

    public void addObstacle(Cell cell) {
        obstacles.add(cell);
    }

    public void clearFoods() {
        foods.clear();
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void clearRobotSnakes() {
        robotSnakes.clear();
    }

    private void spawnRobots() {
        Set<Cell> occupied = BoardOccupancy.occupiedCells(snake, robotSnakes, obstacles, foods);
        robotSnakes.addAll(spawnService.spawnRobots(config, config.getRobotCount(), occupied));
    }

    private void spawnFoodToTarget() {
        Set<Cell> occupied = BoardOccupancy.occupiedCells(snake, robotSnakes, obstacles, foods);
        spawnService.fillFoodsToTarget(config, foods, occupied);
    }

    private Food getFoodAt(Cell cell) {
        for (Food food : foods) {
            if (food.getPosition().equals(cell)) {
                return food;
            }
        }
        return null;
    }

}
