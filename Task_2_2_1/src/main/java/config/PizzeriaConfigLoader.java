package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class PizzeriaConfigLoader {
    private final JsonNode config;

    public PizzeriaConfigLoader(String configFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        config = objectMapper.readTree(new File(configFilePath));
        validateConfig();
    }

    public int getBakersCount() {
        return config.get("bakersCount").asInt();
    }

    public int[] getBakingSpeeds() {
        JsonNode speedsNode = config.get("bakingSpeeds");
        int[] speeds = new int[speedsNode.size()];
        for (int i = 0; i < speedsNode.size(); i++) {
            speeds[i] = speedsNode.get(i).asInt();
        }
        return speeds;
    }

    public int getCourierCount() {
        return config.get("couriersCount").asInt();
    }

    public int[] getTrunkCapacities() {
        JsonNode capacitiesNode = config.get("trunkCapacities");
        int[] capacities = new int[capacitiesNode.size()];
        for (int i = 0; i < capacitiesNode.size(); i++) {
            capacities[i] = capacitiesNode.get(i).asInt();
        }
        return capacities;
    }

    public int getWarehouseCapacity() {
        return config.get("warehouseCapacity").asInt();
    }

    private void validateConfig() {
        if (config == null) {
            throw new IllegalArgumentException("Config file is empty or unreadable");
        }
        requireField("bakersCount");
        requireField("bakingSpeeds");
        requireField("couriersCount");
        requireField("trunkCapacities");
        requireField("warehouseCapacity");

        if (getBakingSpeeds().length != getBakersCount()) {
            throw new IllegalArgumentException("bakingSpeeds length must match bakersCount");
        }
        if (getTrunkCapacities().length != getCourierCount()) {
            throw new IllegalArgumentException("trunkCapacities length must match couriersCount");
        }

        validatePositive("bakersCount", getBakersCount());
        validatePositive("couriersCount", getCourierCount());
        validatePositive("warehouseCapacity", getWarehouseCapacity());

        validatePositiveArray("bakingSpeeds", getBakingSpeeds());
        validatePositiveArray("trunkCapacities", getTrunkCapacities());
    }

    private void requireField(String fieldName) {
        if (!config.hasNonNull(fieldName)) {
            throw new IllegalArgumentException("Missing field: " + fieldName);
        }
    }

    private void validatePositive(String fieldName, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
    }

    private void validatePositiveArray(String fieldName, int[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }

        for (int value : values) {
            if (value <= 0) {
                throw new IllegalArgumentException(fieldName + " values must be greater than 0");
            }
        }
    }
}