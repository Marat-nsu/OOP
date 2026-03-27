package snake.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameEngine {
    private final GameConfig config;
    private final VictoryCondition victoryCondition;
    private final Random random;

    private Snake snake;
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
        reset();
    }

    public void reset() {
        Cell start = new Cell(config.getRows() / 2, config.getCols() / 2);
        snake = new Snake(start);
        currentDirection = Direction.RIGHT;
        pendingDirection = Direction.RIGHT;
        status = GameStatus.READY;
        obstacles.clear();
        generateObstacles(18, start);
        foods.clear();
        spawnFoodToTarget();
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

        Direction source = snake.length() > 1 ? currentDirection : pendingDirection;
        if (newDirection.isOpposite(source)) {
            return;
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

        if (isOutOfBounds(nextHead) || obstacles.contains(nextHead) || hitsSelf) {
            status = GameStatus.LOST;
            return;
        }

        if (eaten != null) {
            foods.remove(eaten);
        }

        snake.move(nextHead, growth);
        spawnFoodToTarget();

        if (victoryCondition.isWon(snake)) {
            status = GameStatus.WON;
        }
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

    private void generateObstacles(int count, Cell reservedCell) {
        int attempts = 0;
        int maxAttempts = config.getRows() * config.getCols() * 3;
        while (obstacles.size() < count && attempts < maxAttempts) {
            attempts++;
            Cell candidate = new Cell(random.nextInt(config.getRows()), random.nextInt(config.getCols()));
            if (candidate.equals(reservedCell)) {
                continue;
            }
            if (Math.abs(candidate.getRow() - reservedCell.getRow()) <= 1
                && Math.abs(candidate.getCol() - reservedCell.getCol()) <= 1) {
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
}
