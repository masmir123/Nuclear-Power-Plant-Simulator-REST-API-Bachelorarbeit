package com.example.restapi_vr.simulator.DTO;

public class Pump_DTO {
    private int rpm;
    private boolean operational;

    public Pump_DTO(int rpm, boolean operational) {
        this.rpm = rpm;
        this.operational = operational;
    }

    public int getRpm() {
        return rpm;
    }

    public boolean isOperational() {
        return operational;
    }
}