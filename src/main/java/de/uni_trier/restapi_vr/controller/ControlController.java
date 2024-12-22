package de.uni_trier.restapi_vr.controller;

import de.uni_trier.restapi_vr.service.ControlService;
import de.uni_trier.restapi_vr.simulator.DTO.Pump_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Valve_DTO;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Package: com.example.restapi_vr.controller
 */
@ApplicationScoped
@Path("/control")
@Tag(name = "Control Endpoints")
public class ControlController {

    @Resource
    ExecutorService executorService;

    @Resource
    private final ControlService controlService = new ControlService(NPPSystemInterface.getInstance());



    @Operation(
            summary = "Update the status of a specified valve by ID",
            description = "Sets the state of a specified valve by ID using the activate query parameter."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valve status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or query parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PUT
    @Path("/valve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateValveStatus(
            @PathParam("id") int id,
            @QueryParam("activate") boolean activate,
            @Suspended final AsyncResponse response) {

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Valve_DTO valve = controlService.updateValveStatus(id, activate);
                response.resume(Response.ok(valve).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problem with Reactor or Server: " + e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }


    @Operation(
            summary = "Update the status of a specified pump by ID",
            description = "Sets the state of a specified valve by ID using the activate query parameter."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pump status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or query parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PUT
    @Path("/pump/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updatePumpStatus(
            @PathParam("id") int id,
            @QueryParam("setRpm") int setRpm,
            @Suspended final AsyncResponse response) {

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Pump_DTO pump = controlService.updatePumpStatus(id, setRpm);
                response.resume(Response.ok(pump).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problem with Reactor or Server: " + e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    @Operation(
            summary = "Control rod exposure",
            description = "Sets the exposure level of the control rods. Values range between 0 and 100."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rod exposure set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request. Rod exposure must be between 0 and 100"),
            @ApiResponse(responseCode = "500", description = "Problem with Server")
    })
    @PUT
    @Path("/rods")
    @Produces(MediaType.APPLICATION_JSON)
    public void setRodExposure(@QueryParam("setRod") int setRod, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                controlService.setRodExposure(setRod);
                response.resume(Response.ok().build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problem with Server: " + e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }


}