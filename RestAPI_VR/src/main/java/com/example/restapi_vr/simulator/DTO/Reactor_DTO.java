package com.example.restapi_vr.simulator.DTO;

public class Reactor_DTO {
    private float pressure;
    private float waterLevel;
    private boolean operational;
    private boolean intact;

    public Reactor_DTO(float pressure, float waterLevel, boolean operational, boolean intact) {
        this.pressure = pressure;
        this.waterLevel = waterLevel;
        this.operational = operational;
        this.intact = intact;
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

    public boolean isIntact() {
        return intact;
    }
}