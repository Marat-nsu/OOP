package snake.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import snake.model.Cell;
import snake.model.Direction;
import snake.model.Food;
import snake.model.GameEngine;
import snake.model.RobotSnake;


public class RobotController {

    public void update(GameEngine engine) {
        for (RobotSnake robot : engine.getRobotSnakes()) {
            Direction dir = decideDirection(robot, engine);
            robot.setPendingDirection(dir);
        }
    }

    private Direction decideDirection(RobotSnake robot, GameEngine engine) {
        Cell head = robot.getHead();
        List<Direction> safeDirections = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Cell next = head.move(dir);
            if (isSafe(next, engine)) {
                safeDirections.add(dir);
            }
        }

        if (safeDirections.isEmpty()) {
            return Direction.UP;
        }

        Food nearestFood = null;
        double minDist = Double.MAX_VALUE;
        for (Food food : engine.getFoods()) {
            double dist = Math.abs(food.getPosition().getRow() - head.getRow()) 
                        + Math.abs(food.getPosition().getCol() - head.getCol());
            if (dist < minDist) {
                minDist = dist;
                nearestFood = food;
            }
        }

        if (nearestFood != null) {
            Direction bestDir = safeDirections.get(0);
            double bestDist = Double.MAX_VALUE;
            for (Direction dir : safeDirections) {
                Cell next = head.move(dir);
                double d = Math.abs(nearestFood.getPosition().getRow() - next.getRow())
                         + Math.abs(nearestFood.getPosition().getCol() - next.getCol());
                if (d < bestDist) {
                    bestDist = d;
                    bestDir = dir;
                }
            }
            return bestDir;
        }

        return safeDirections.get(new Random().nextInt(safeDirections.size()));
    }

    private boolean isSafe(Cell cell, GameEngine engine) {
        if (cell.getRow() < 0 || cell.getRow() >= engine.getConfig().getRows() ||
            cell.getCol() < 0 || cell.getCol() >= engine.getConfig().getCols()) {
            return false;
        }
        if (engine.getObstacles().contains(cell)) {
            return false;
        }
        if (engine.getSnake().contains(cell)) {
            return false;
        }
        for (RobotSnake other : engine.getRobotSnakes()) {
            if (other.contains(cell)) {
                return false;
            }
        }
        return true;
    }
}
