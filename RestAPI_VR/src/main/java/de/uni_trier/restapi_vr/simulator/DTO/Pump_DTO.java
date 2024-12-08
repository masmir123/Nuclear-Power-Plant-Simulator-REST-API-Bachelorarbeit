package de.uni_trier.restapi_vr.simulator.DTO;

public class Pump_DTO {
    private String name;
    private boolean blown;
    private int rpm;
    private int setRpm;
    private int maxRpm;
    private boolean operational;

    public Pump_DTO(int rpm, boolean operational) {
        this.rpm = rpm;
        this.operational = operational;
    }

    public Pump_DTO(String name, boolean blown, int rpm, int setRpm, int maxRpm) {
        this.name = name;
        this.blown = blown;
        this.rpm = rpm;
        this.setRpm = setRpm;
        this.maxRpm = maxRpm;
    }

    public Pump_DTO(String name, int rpm) {
        this.name = name;
        this.rpm = rpm;
    }

    public String getName() {
        return name;
    }

    public int getMaxRpm() {
        return maxRpm;
    }

    public int getSetRpm() {
        return setRpm;
    }

    public boolean isBlown() {
        return blown;
    }

    public int getRpm() {
        return rpm;
    }

    public boolean isOperational() {
        return operational;
    }
}