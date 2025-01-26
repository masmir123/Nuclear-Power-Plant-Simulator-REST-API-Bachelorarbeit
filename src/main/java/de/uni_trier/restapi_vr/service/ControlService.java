package de.uni_trier.restapi_vr.service;

import de.uni_trier.restapi_vr.simulator.DTO.Pump_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Reactor_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Valve_DTO;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;

import java.util.Optional;

public class ControlService {

    private NPPSystemInterface nppSystemInterface;

    public ControlService(NPPSystemInterface nppSystemInterface) {
        this.nppSystemInterface = nppSystemInterface;
    }


    public Valve_DTO updateValveStatus(String id, boolean activate) {

        nppSystemInterface.updateValveStatus(id, activate);

        return Optional.ofNullable(nppSystemInterface.getValves().get(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid valve ID " + id));
    }

    public Pump_DTO updatePumpStatus(String id, float setRpm) {
        if (setRpm < 0) {
            throw new IllegalArgumentException("Invalid RPM value. Must be greater than or equal to 0.");
        }

        nppSystemInterface.updatePumpRpm(id, (int) setRpm);

        return Optional.ofNullable(nppSystemInterface.getPumps().get(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid pump ID " + id));
    }

    public Reactor_DTO setRodExposure(float setRod) {
        if (setRod < 0 || setRod > 100) {
            throw new IllegalArgumentException("Rod exposure must be between 0 and 100.");
        }

        try {
            nppSystemInterface.setControlRodExposure((int) setRod);
            return new Reactor_DTO("reactor", nppSystemInterface.getReactorStatus(), nppSystemInterface.getPressureReactor(), nppSystemInterface.getWaterLevelReactor(), nppSystemInterface.getRodPosition(), nppSystemInterface.getRestheat() ,nppSystemInterface.getOverheatedStatus());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set rod exposure: " + e.getMessage());
        }
    }
}
