package de.uni_trier.restapi_vr.simulator.DTO;

public class SlowdownFactor_DTO {
    private int slowdownFactor;

    public SlowdownFactor_DTO() {
    }

    public SlowdownFactor_DTO(int slowdownFactor) {
        this.slowdownFactor = slowdownFactor;
    }

    public float getSlowdownFactor() {
        return slowdownFactor;
    }
}
