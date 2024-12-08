package de.uni_trier.restapi_vr.simulator.DTO;

public class Component_DTO {

    private String name;
    private boolean blown;
    private boolean interactable;

    public Component_DTO(String name, boolean blown, boolean interactable) {
        this.name = name;
        this.blown = blown;
        this.interactable = interactable;
    }

    public Component_DTO(String name, boolean blown) {
        this.name = name;
        this.blown = blown;
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

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

}