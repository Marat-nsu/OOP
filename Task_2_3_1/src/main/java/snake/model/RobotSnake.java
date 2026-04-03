package snake.model;

public class RobotSnake extends Snake {
    private Direction pendingDirection;

    public RobotSnake(Cell startCell) {
        super(startCell);
        this.pendingDirection = Direction.RIGHT;
    }

    public Direction getPendingDirection() {
        return pendingDirection;
    }

    public void setPendingDirection(Direction pendingDirection) {
        this.pendingDirection = pendingDirection;
    }
}
