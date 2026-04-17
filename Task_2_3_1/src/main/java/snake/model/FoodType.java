package snake.model;

public enum FoodType {
    BASIC(1);

    private final int growth;

    FoodType(int growth) {
        this.growth = growth;
    }

    public int getGrowth() {
        return growth;
    }
}
