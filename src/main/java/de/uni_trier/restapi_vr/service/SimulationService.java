package de.uni_trier.restapi_vr.service;


import de.uni_trier.restapi_vr.simulator.DTO.*;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;

import java.util.List;
import java.util.Optional;

public class SimulationService {

    private NPPSystemInterface nppSystemInterface;

    public SimulationService(NPPSystemInterface instance) {
        this.nppSystemInterface = instance;
    }


    public Valve_DTO getValveStatus(String id) {
        return Optional.ofNullable(nppSystemInterface.getValves().get(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid valve ID " + id));
    }

    public Pump_DTO getPumpStatus(String id) {
        return Optional.ofNullable(nppSystemInterface.getPumps().get(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid pump ID " + id));
    }

    public Generator_DTO getGeneratorStatus() {
        return new Generator_DTO("generator", false, nppSystemInterface.getPowerOutlet());
    }

    public Condenser_DTO getCondenserStatus() {
        return new Condenser_DTO("condenser", !nppSystemInterface.getCondenserStatus(), nppSystemInterface.getWaterLevelCondenser(), nppSystemInterface.getPressureCondenser());
    }

    public Reactor_DTO getReactorStatus() {
        return new Reactor_DTO("reactor", nppSystemInterface.getReactorStatus(), nppSystemInterface.getPressureReactor(), nppSystemInterface.getWaterLevelReactor(), nppSystemInterface.getRodPosition(), nppSystemInterface.getRestheat(), nppSystemInterface.getOverheatedStatus());
    }

    public List<Components_DTO> getComponentsHealth() {
        try {
            return nppSystemInterface.getComponentsHealth();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve components: " + e.getMessage());
        }
    }
}
