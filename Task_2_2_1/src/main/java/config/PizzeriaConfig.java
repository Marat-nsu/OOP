package config;

public record PizzeriaConfig(
        int bakersCount,
        int[] bakingSpeeds,
        int couriersCount,
        int[] trunkCapacities,
        int warehouseCapacity) {

    public PizzeriaConfig {
        bakingSpeeds = bakingSpeeds.clone();
        trunkCapacities = trunkCapacities.clone();
    }

    @Override
    public int[] bakingSpeeds() {
        return bakingSpeeds.clone();
    }

    @Override
    public int[] trunkCapacities() {
        return trunkCapacities.clone();
    }
}