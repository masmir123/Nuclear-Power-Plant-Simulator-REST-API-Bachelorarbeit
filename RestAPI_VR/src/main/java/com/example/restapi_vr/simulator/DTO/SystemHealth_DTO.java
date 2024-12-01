package com.example.restapi_vr.simulator.DTO;

public class SystemHealth_DTO {
    private boolean healthy;

    public SystemHealth_DTO(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }
}