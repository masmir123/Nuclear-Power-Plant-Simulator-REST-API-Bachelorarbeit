package de.uni_trier.restapi_vr.simulator.DTO;

public class Valve_DTO {
    private String name;
    private boolean blown;
    private boolean status;

    public Valve_DTO(String name, boolean blown, boolean status) {
        this.name = name;
        this.blown = blown;
        this.status = status;
    }

    public Valve_DTO(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBlown() {
        return blown;
    }

    public void setBlown(boolean blown) {
        this.blown = blown;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}