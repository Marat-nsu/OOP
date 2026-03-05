package model;

public enum OrderStatus {
    QUEUED,
    BAKING,
    RESERVING_WAREHOUSE_SLOT,
    STORED,
    PICKED_UP_BY_COURIER,
    DELIVERED
}