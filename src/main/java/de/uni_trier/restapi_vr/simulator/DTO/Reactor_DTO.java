package de.uni_trier.restapi_vr.simulator.DTO;

public class Reactor_DTO {
    private String reactor_name;
    private float pressure;
    private float waterLevel;
    private boolean operational;
    private boolean intact;
    private int rodposition;
    private boolean overheated;

    public Reactor_DTO() {}

    public Reactor_DTO(String reactor_name, boolean intact, float pressure, float waterLevel, int rodposition, boolean overheated) {
        this.reactor_name = reactor_name;
        this.pressure = pressure;
        this.waterLevel = waterLevel;
        this.intact = intact;
        this.rodposition = rodposition;
        this.overheated = overheated;
    }

    public int getRodposition() {return rodposition;}

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