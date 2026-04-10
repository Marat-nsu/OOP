package snake.model;

import java.util.List;
import java.util.Set;

public final class CollisionService {
    private CollisionService() {
    }

    public static boolean playerLoses(
        Cell nextHead,
        int growth,
        Snake snake,
        Set<Cell> obstacles,
        List<RobotSnake> robots,
        GameConfig config
    ) {
        if (isOutOfBounds(nextHead, config) || obstacles.contains(nextHead)) {
            return true;
        }

        boolean hitsSelf = snake.contains(nextHead);
        if (hitsSelf && growth <= 0 && nextHead.equals(snake.getTail())) {
            hitsSelf = false;
        }

        return hitsSelf || hitsOtherSnakes(nextHead, snake, snake, robots);
    }

    public static boolean robotDies(
        Cell robotNext,
        RobotSnake robot,
        Snake snake,
        Set<Cell> obstacles,
        List<RobotSnake> robots,
        GameConfig config
    ) {
        return isOutOfBounds(robotNext, config)
            || obstacles.contains(robotNext)
            || robot.contains(robotNext)
            || snake.contains(robotNext)
            || hitsOtherSnakes(robotNext, robot, snake, robots);
    }

    private static boolean hitsOtherSnakes(Cell head, Snake self, Snake snake, List<RobotSnake> robots) {
        if (self != snake && snake.contains(head)) {
            return true;
        }

        for (RobotSnake other : robots) {
            if (other != self && other.contains(head)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOutOfBounds(Cell cell, GameConfig config) {
        return cell.getRow() < 0
            || cell.getRow() >= config.getRows()
            || cell.getCol() < 0
            || cell.getCol() >= config.getCols();
    }
}
