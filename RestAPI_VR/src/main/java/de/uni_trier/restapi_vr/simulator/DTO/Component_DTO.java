package de.uni_trier.restapi_vr.simulator.DTO;

public class Component_DTO {

    private String name;
    private boolean blown;
    private boolean interactable;

    public Component_DTO(String name, boolean blown) {
        this.name = name;
        this.blown = blown;
    }

    public Component_DTO(String name, boolean blown, boolean interactable) {
        this.name = name;
        this.blown = blown;
        this.interactable = interactable;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public boolean isBlown() {
        return blown;
    }

    public boolean isInteractable() {
        return interactable;
    }

}