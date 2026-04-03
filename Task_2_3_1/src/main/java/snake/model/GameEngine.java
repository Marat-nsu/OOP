package snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameEngine {
    private GameConfig config;
    private VictoryCondition victoryCondition;
    private final Random random;

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
        this.random = new Random();
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
        generateObstacles(18, new HashSet<>(Collections.singletonList(start)));
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
        
        generateObstacles(18, reserved);
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

        boolean hitsSelf = snake.contains(nextHead);
        if (hitsSelf && growth <= 0 && nextHead.equals(snake.getTail())) {
            hitsSelf = false;
        }

        if (isOutOfBounds(nextHead) || obstacles.contains(nextHead) || hitsSelf || hitsOtherSnakes(nextHead, snake)) {
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
            
            if (isOutOfBounds(robotNext) || obstacles.contains(robotNext) || robot.contains(robotNext) 
                || snake.contains(robotNext) || hitsOtherSnakes(robotNext, robot)) {
                it.remove();
                continue;
            }
            
            Food robotEaten = getFoodAt(robotNext);
            int robotGrowth = robotEaten == null ? 0 : robotEaten.getType().getGrowth();
            if (robotEaten != null) foods.remove(robotEaten);
            
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
        for (int i = 0; i < 2; i++) {
            Cell pos = findFreeCell();
            if (pos != null) {
                robotSnakes.add(new RobotSnake(pos));
            }
        }
    }

    private Cell findFreeCell() {
        for (int i = 0; i < 100; i++) {
            Cell c = new Cell(random.nextInt(config.getRows()), random.nextInt(config.getCols()));
            if (!isOccupied(c)) return c;
        }
        return null;
    }

    private boolean hitsOtherSnakes(Cell head, Snake self) {
        if (self != snake && snake.contains(head)) return true;
        for (RobotSnake other : robotSnakes) {
            if (other != self && other.contains(head)) return true;
        }
        return false;
    }

    private void spawnFoodToTarget() {
        int maxAttempts = config.getRows() * config.getCols() * 2;
        int attempts = 0;
        while (foods.size() < config.getFoodCount() && attempts < maxAttempts) {
            attempts++;
            Cell candidate = new Cell(random.nextInt(config.getRows()), random.nextInt(config.getCols()));
            if (isOccupied(candidate)) {
                continue;
            }
            foods.add(new Food(candidate, FoodType.BASIC));
        }
    }

    private void generateObstacles(int count, Set<Cell> reservedCells) {
        int attempts = 0;
        int maxAttempts = config.getRows() * config.getCols() * 3;
        while (obstacles.size() < count && attempts < maxAttempts) {
            attempts++;
            Cell candidate = new Cell(random.nextInt(config.getRows()), random.nextInt(config.getCols()));
            
            boolean tooClose = false;
            for (Cell reserved : reservedCells) {
                if (Math.abs(candidate.getRow() - reserved.getRow()) <= 1
                    && Math.abs(candidate.getCol() - reserved.getCol()) <= 1) {
                    tooClose = true;
                    break;
                }
            }
            if (tooClose) {
                continue;
            }
            obstacles.add(candidate);
        }
    }

    private Food getFoodAt(Cell cell) {
        for (Food food : foods) {
            if (food.getPosition().equals(cell)) {
                return food;
            }
        }
        return null;
    }

    private boolean isOutOfBounds(Cell cell) {
        return cell.getRow() < 0 || cell.getRow() >= config.getRows() || cell.getCol() < 0 || cell.getCol() >= config.getCols();
    }

    private boolean isOccupied(Cell cell) {
        if (snake.contains(cell) || obstacles.contains(cell)) {
            return true;
        }

        for (Food food : foods) {
            if (food.getPosition().equals(cell)) {
                return true;
            }
        }
        return false;
    }
}
