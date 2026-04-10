package snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpawnService {
    private final Random random;

    public SpawnService(Random random) {
        this.random = random;
    }

    public List<RobotSnake> spawnRobots(GameConfig config, int targetCount, Set<Cell> occupiedCells) {
        List<Cell> freeCells = collectFreeCells(config, occupiedCells, Collections.emptySet(), 0);
        Collections.shuffle(freeCells, random);

        int spawnCount = Math.min(targetCount, freeCells.size());
        List<RobotSnake> robots = new ArrayList<>(spawnCount);
        for (int i = 0; i < spawnCount; i++) {
            robots.add(new RobotSnake(freeCells.get(i)));
        }
        return robots;
    }

    public Set<Cell> generateObstacles(GameConfig config, int targetCount, Set<Cell> reservedCells, int safetyRadius) {
        List<Cell> candidates = collectFreeCells(config, Collections.emptySet(), reservedCells, safetyRadius);
        Collections.shuffle(candidates, random);

        int obstacleCount = Math.min(targetCount, candidates.size());
        return Set.copyOf(candidates.subList(0, obstacleCount));
    }

    public void fillFoodsToTarget(GameConfig config, List<Food> foods, Set<Cell> occupiedCells) {
        int missing = config.getFoodCount() - foods.size();
        if (missing <= 0) {
            return;
        }

        List<Cell> freeCells = collectFreeCells(config, occupiedCells, Collections.emptySet(), 0);
        Collections.shuffle(freeCells, random);

        int foodToAdd = Math.min(missing, freeCells.size());
        for (int i = 0; i < foodToAdd; i++) {
            foods.add(new Food(freeCells.get(i), FoodType.BASIC));
        }
    }

    private List<Cell> collectFreeCells(GameConfig config, Set<Cell> occupiedCells, Set<Cell> reservedCells, int safetyRadius) {
        List<Cell> freeCells = new ArrayList<>();
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getCols(); col++) {
                Cell candidate = new Cell(row, col);
                if (occupiedCells.contains(candidate)) {
                    continue;
                }
                if (isTooCloseToReserved(candidate, reservedCells, safetyRadius)) {
                    continue;
                }
                freeCells.add(candidate);
            }
        }
        return freeCells;
    }

    private boolean isTooCloseToReserved(Cell candidate, Set<Cell> reservedCells, int safetyRadius) {
        if (safetyRadius <= 0 || reservedCells.isEmpty()) {
            return false;
        }

        for (Cell reserved : reservedCells) {
            if (Math.abs(candidate.getRow() - reserved.getRow()) <= safetyRadius
                && Math.abs(candidate.getCol() - reserved.getCol()) <= safetyRadius) {
                return true;
            }
        }
        return false;
    }
}
