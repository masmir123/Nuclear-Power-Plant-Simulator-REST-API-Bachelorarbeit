package de.uni_trier.restapi_vr.simulator.DTO;

public class Condenser_DTO {
    private String condenser_name;
    private boolean condenser_blown;
    private float pressure;
    private float waterLevel;
    private boolean operational;

    public Condenser_DTO() {
    }

    public Condenser_DTO(float pressure, float waterLevel, boolean operational) {
        this.pressure = pressure;
        this.waterLevel = waterLevel;
        this.operational = operational;
    }

    public Condenser_DTO(String condenser_name, boolean condenser_blown, float waterLevel, float pressure) {
        this.condenser_name = condenser_name;
        this.condenser_blown = condenser_blown;
        this.pressure = pressure;
        this.waterLevel = waterLevel;
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