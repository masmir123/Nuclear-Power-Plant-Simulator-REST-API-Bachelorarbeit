package de.uni_trier.restapi_vr.service;


import de.uni_trier.restapi_vr.simulator.DTO.*;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;

import java.util.List;

public class SimulationService {

    private NPPSystemInterface nppSystemInterface;

    public SimulationService(NPPSystemInterface instance) {
        this.nppSystemInterface = instance;
    }


    public Valve_DTO getValveStatus(int id) {
        return switch (id) {
            case 1 -> new Valve_DTO("SV1", nppSystemInterface.getSV1_BlownStatus(), nppSystemInterface.getSV1Status());
            case 2 -> new Valve_DTO("SV2", nppSystemInterface.getSV2_BlownStatus(), nppSystemInterface.getSV2Status());
            default -> throw new IllegalArgumentException("Invalid valve ID");
        };
    }

    public Pump_DTO getPumpStatus(int id) {
        return switch (id) {
            case 1 -> new Pump_DTO("WP1", nppSystemInterface.getWP1_BlownStatus(), nppSystemInterface.getWP1RPM(), nppSystemInterface.getWP1RPMSet(), nppSystemInterface.getWP1RPMSet());
            case 2 -> new Pump_DTO("WP2", nppSystemInterface.getWP2_BlownStatus(), nppSystemInterface.getWP2RPM(), nppSystemInterface.getWP2RPMSet(), nppSystemInterface.getWP2RPMSet());
            default -> throw new IllegalArgumentException("Invalid pump ID");
        };
    }

    public Generator_DTO getGeneratorStatus() {
        return new Generator_DTO("generator", false, nppSystemInterface.getPowerOutlet());
    }

    public Condenser_DTO getCondenserStatus() {
        return new Condenser_DTO("condenser", !nppSystemInterface.getCondenserStatus(), nppSystemInterface.getWaterLevelCondenser(), nppSystemInterface.getWaterLevelCondenser());
    }

    public Reactor_DTO getReactorStatus() {
        return new Reactor_DTO("reactor", nppSystemInterface.getReactorStatus(), nppSystemInterface.getPressureReactor(), nppSystemInterface.getWaterLevelReactor(), nppSystemInterface.getRodPosition(), nppSystemInterface.getOverheatedStatus());
    }

    public List<Components_DTO> getComponentsHealth() {
        try {
            return nppSystemInterface.getComponentsHealth();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve components: " + e.getMessage());
        }
    }
}
