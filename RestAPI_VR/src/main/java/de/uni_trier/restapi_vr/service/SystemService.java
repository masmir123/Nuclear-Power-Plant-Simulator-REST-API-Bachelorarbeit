package de.uni_trier.restapi_vr.service;

import de.uni_trier.restapi_vr.simulator.DTO.Component_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Status_DTO;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;

import java.util.List;

public class SystemService {

    private NPPSystemInterface nppSystemInterface;

    public SystemService(NPPSystemInterface instance) {
        this.nppSystemInterface = instance;
    }


    public Status_DTO getStatus() {
        try {
            boolean isRunning = nppSystemInterface.isSimulationRunning();
            String runningSince = isRunning ? nppSystemInterface.getSimulationStartTime() : null;

            return new Status_DTO(isRunning, runningSince);
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve status: " + e.getMessage());
        }
    }

    public List<Component_DTO> getComponents() {
        try {
            return nppSystemInterface.getComponents();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve components: " + e.getMessage());
        }
    }

    public void restartSimulation() {
        try {
            nppSystemInterface.restartSimulation();
        } catch (Exception e) {
            throw new RuntimeException("Failed to restart simulation: " + e.getMessage());
        }
    }

}
