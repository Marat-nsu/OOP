package snake.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LevelConfigLoader {
    private static final String RESOURCE_NAME = "levels.properties";

    public List<GameConfig> loadOrDefault() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(RESOURCE_NAME)) {
            if (in == null) {
                return defaultLevels();
            }

            Properties props = new Properties();
            props.load(in);
            return parseLevels(props);
        } catch (IOException | IllegalArgumentException e) {
            return defaultLevels();
        }
    }

    private List<GameConfig> parseLevels(Properties props) {
        int count = parsePositiveInt(props, "level.count");
        List<GameConfig> levels = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String prefix = "level." + i + ".";
            int rows = parsePositiveInt(props, prefix + "rows");
            int cols = parsePositiveInt(props, prefix + "cols");
            int foodCount = parseNonNegativeInt(props, prefix + "foodCount");
            int obstacleCount = parseNonNegativeInt(props, prefix + "obstacleCount");
            int robotCount = parseNonNegativeInt(props, prefix + "robotCount");
            int winLength = parsePositiveInt(props, prefix + "winLength");
            int tickMillis = parsePositiveInt(props, prefix + "tickMillis");

            levels.add(new GameConfig(rows, cols, foodCount, obstacleCount, robotCount, winLength, tickMillis));
        }

        if (levels.isEmpty()) {
            throw new IllegalArgumentException("No levels loaded");
        }
        return levels;
    }

    private int parsePositiveInt(Properties props, String key) {
        int value = Integer.parseInt(required(props, key));
        if (value <= 0) {
            throw new IllegalArgumentException("Expected positive value for " + key);
        }
        return value;
    }

    private int parseNonNegativeInt(Properties props, String key) {
        int value = Integer.parseInt(required(props, key));
        if (value < 0) {
            throw new IllegalArgumentException("Expected non-negative value for " + key);
        }
        return value;
    }

    private String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing key " + key);
        }
        return value.trim();
    }

    private List<GameConfig> defaultLevels() {
        List<GameConfig> defaults = new ArrayList<>();
        defaults.add(new GameConfig(20, 28, 3, 18, 2, 5, 200));
        defaults.add(new GameConfig(20, 28, 4, 18, 2, 10, 150));
        defaults.add(new GameConfig(20, 28, 6, 18, 2, 15, 100));
        defaults.add(new GameConfig(20, 28, 5, 18, 2, 20, 70));
        return defaults;
    }
}
