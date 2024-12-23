package de.uni_trier.restapi_vr.simulator.DTO;


public class Generator_DTO {

    private String name;
    private boolean blown;
    private int power;

    public Generator_DTO() {}

    public Generator_DTO(String name, boolean blown, int power) {
        this.name = name;
        this.blown = blown;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public boolean isBlown() {
        return blown;
    }


}
