package de.uni_trier.restapi_vr.service;

import de.uni_trier.restapi_vr.simulator.DTO.Pump_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Valve_DTO;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;

public class ControlService {

    private NPPSystemInterface nppSystemInterface;

    public ControlService(NPPSystemInterface nppSystemInterface) {
        this.nppSystemInterface = nppSystemInterface;
    }


    public Valve_DTO updateValveStatus(int id, boolean activate) {

        boolean status = nppSystemInterface.updateWaterValveStatus(id, activate);

        return switch (id) {
            case 1 -> new Valve_DTO("WV1", status);
            case 2 -> new Valve_DTO("WV2", status);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };

    }

    public Pump_DTO updatePumpStatus(int id, int setRpm) {
        if (setRpm < 0) {
            throw new IllegalArgumentException("Invalid RPM value. Must be greater than or equal to 0.");
        }


        int updatedRpm = nppSystemInterface.updatePumpRpm(id, setRpm);

        return switch (id) {
            case 1 -> new Pump_DTO("WV1", updatedRpm);
            case 2 -> new Pump_DTO("WV2", updatedRpm);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public void setRodExposure(int setRod) {
        if (setRod < 0 || setRod > 100) {
            throw new IllegalArgumentException("Rod exposure must be between 0 and 100.");
        }

        try {

            nppSystemInterface.setControlRodExposure(setRod);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set rod exposure: " + e.getMessage());
        }
    }
}
