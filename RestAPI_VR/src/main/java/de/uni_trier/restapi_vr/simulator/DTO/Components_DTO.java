package de.uni_trier.restapi_vr.simulator.DTO;

public class Components_DTO {

    private String name;
    private boolean blown;
    private boolean overheated;
    private boolean broken;


    public Components_DTO(String name, boolean broken) {
        this.name = name;
        this.broken = broken;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public boolean isBroken() {
        return broken;
    }
}
