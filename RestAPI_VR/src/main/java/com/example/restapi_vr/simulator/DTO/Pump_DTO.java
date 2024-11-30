package com.example.restapi_vr.simulator.DTO;

public class PumpDTO {
    private int rpm;
    private boolean operational;

    public PumpDTO(int rpm, boolean operational) {
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