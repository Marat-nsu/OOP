package snake.model;

import java.util.List;

public class LevelManager {
    private final List<GameConfig> levels;
    private int currentLevelIndex;

    public LevelManager() {
        levels = new LevelConfigLoader().loadOrDefault();
        currentLevelIndex = 0;
    }

    public GameConfig getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public boolean nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            return true;
        }
        return false;
    }

    public void reset() {
        currentLevelIndex = 0;
    }
}
