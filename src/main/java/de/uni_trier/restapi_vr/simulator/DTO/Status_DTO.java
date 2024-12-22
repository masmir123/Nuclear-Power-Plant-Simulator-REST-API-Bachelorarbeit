package de.uni_trier.restapi_vr.simulator.DTO;

public class Status_DTO {

    private boolean isRunning;
    private String runningSince;

    public Status_DTO(boolean isRunning, String runningSince) {
        this.isRunning = isRunning;
        this.runningSince = runningSince;
    }

    // Getters and setters
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getRunningSince() {
        return runningSince;
    }

    public void setRunningSince(String runningSince) {
        this.runningSince = runningSince;
    }

}