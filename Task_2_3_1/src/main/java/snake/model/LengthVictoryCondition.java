package snake.model;

public class LengthVictoryCondition implements VictoryCondition {
    private final int targetLength;

    public LengthVictoryCondition(int targetLength) {
        this.targetLength = targetLength;
    }

    @Override
    public boolean isWon(Snake snake) {
        return snake.length() >= targetLength;
    }
}
