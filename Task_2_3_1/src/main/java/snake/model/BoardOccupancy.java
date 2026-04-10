package snake.model;

import java.util.HashSet;
import java.util.Set;

public final class BoardOccupancy {
    private BoardOccupancy() {
    }

    public static Set<Cell> occupiedCells(Snake snake, Iterable<RobotSnake> robots, Iterable<Cell> obstacles, Iterable<Food> foods) {
        Set<Cell> occupied = new HashSet<>();
        occupied.addAll(snake.getSegments());

        for (RobotSnake robot : robots) {
            occupied.addAll(robot.getSegments());
        }

        for (Cell obstacle : obstacles) {
            occupied.add(obstacle);
        }

        for (Food food : foods) {
            occupied.add(food.getPosition());
        }

        return occupied;
    }
}
