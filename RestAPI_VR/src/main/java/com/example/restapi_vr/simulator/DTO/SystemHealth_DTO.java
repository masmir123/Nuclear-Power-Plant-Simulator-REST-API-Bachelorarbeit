package com.example.restapi_vr.simulator.DTO;

public class SystemHealthDTO {
    private boolean healthy;

    public SystemHealthDTO(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }
}