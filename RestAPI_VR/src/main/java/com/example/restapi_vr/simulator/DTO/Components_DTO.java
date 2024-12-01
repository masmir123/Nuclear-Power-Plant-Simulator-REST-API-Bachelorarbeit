package com.example.restapi_vr.simulator.DTO;

public class Components_DTO {
    private String componentName;
    private float pressure;
    private float waterLevel;
    private boolean operational;
    private boolean intact;
    private boolean interactable;

    public Components_DTO(String componentName, float pressure, float waterLevel, boolean operational, boolean intact, boolean interactable) {
        this.componentName = componentName;
        this.pressure = pressure;
        this.waterLevel = waterLevel;
        this.operational = operational;
        this.intact = intact;
        this.interactable = interactable;
    }

    public String getComponentName() {
        return componentName;
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

    public boolean isInteractable() {
        return interactable;
    }
}