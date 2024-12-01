package com.example.restapi_vr.simulator.DTO;

public class Condenser_DTO {
    private float pressure;
    private float waterLevel;
    private boolean operational;

    public Condenser_DTO(float pressure, float waterLevel, boolean operational) {
        this.pressure = pressure;
        this.waterLevel = waterLevel;
        this.operational = operational;
    }

    public float getPressure() {
        return pressure;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public boolean isOperational() {
        return operational;
    }
}