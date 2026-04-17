package snake.model;

public class Food {
    private final Cell position;
    private final FoodType type;

    public Food(Cell position, FoodType type) {
        this.position = position;
        this.type = type;
    }

    public Cell getPosition() {
        return position;
    }

    public FoodType getType() {
        return type;
    }
}
