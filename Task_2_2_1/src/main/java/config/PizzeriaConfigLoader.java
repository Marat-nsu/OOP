package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class PizzeriaConfigLoader implements PizzeriaConfigSource {
    private final String configFilePath;

    public PizzeriaConfigLoader(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    @Override
    public PizzeriaConfig load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode config = objectMapper.readTree(new File(configFilePath));
        validateConfig(config);

        return new PizzeriaConfig(
                config.get("bakersCount").asInt(),
                readIntArray(config.get("bakingSpeeds")),
                config.get("couriersCount").asInt(),
                readIntArray(config.get("trunkCapacities")),
                config.get("warehouseCapacity").asInt());
    }

    private int[] readIntArray(JsonNode node) {
        int[] values = new int[node.size()];
        for (int i = 0; i < node.size(); i++) {
            values[i] = node.get(i).asInt();
        }
        return values;
    }

    private void validateConfig(JsonNode config) {
        if (config == null) {
            throw new IllegalArgumentException("Config file is empty or unreadable");
        }
        requireField(config, "bakersCount");
        requireField(config, "bakingSpeeds");
        requireField(config, "couriersCount");
        requireField(config, "trunkCapacities");
        requireField(config, "warehouseCapacity");

        int bakersCount = config.get("bakersCount").asInt();
        int couriersCount = config.get("couriersCount").asInt();
        int warehouseCapacity = config.get("warehouseCapacity").asInt();
        int[] bakingSpeeds = readIntArray(config.get("bakingSpeeds"));
        int[] trunkCapacities = readIntArray(config.get("trunkCapacities"));

        if (bakingSpeeds.length != bakersCount) {
            throw new IllegalArgumentException("bakingSpeeds length must match bakersCount");
        }
        if (trunkCapacities.length != couriersCount) {
            throw new IllegalArgumentException("trunkCapacities length must match couriersCount");
        }

        validatePositive("bakersCount", bakersCount);
        validatePositive("couriersCount", couriersCount);
        validatePositive("warehouseCapacity", warehouseCapacity);

        validatePositiveArray("bakingSpeeds", bakingSpeeds);
        validatePositiveArray("trunkCapacities", trunkCapacities);
    }

    private void requireField(JsonNode config, String fieldName) {
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