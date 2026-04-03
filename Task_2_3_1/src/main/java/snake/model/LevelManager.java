package snake.model;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private final List<GameConfig> levels;
    private int currentLevelIndex;

    public LevelManager() {
        levels = new ArrayList<>();
        levels.add(new GameConfig(20, 28, 3, 5, 200));
        levels.add(new GameConfig(20, 28, 4, 10, 150));
        levels.add(new GameConfig(20, 28, 6, 15, 100));
        levels.add(new GameConfig(20, 28, 5, 20, 70));
        
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
