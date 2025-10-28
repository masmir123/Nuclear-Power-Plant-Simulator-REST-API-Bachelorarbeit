package de.uni_trier.restapi_vr.controller;

import de.uni_trier.restapi_vr.service.ControlService;
import de.uni_trier.restapi_vr.simulator.DTO.*;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
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
            @PathParam("id") String id,
            @QueryParam("activate") boolean activate,
            @Suspended final AsyncResponse response) {

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Valve_DTO valve = controlService.updateValveStatus(id.toUpperCase(), activate);
                response.resume(Response.ok(valve).build());
            } catch (Exception e) {
                response.resume(e);
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
            @PathParam("id") String id,
            @QueryParam("setRpm") float setRpm,
            @Suspended final AsyncResponse response) {

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Pump_DTO pump = controlService.updatePumpStatus(id.toUpperCase(), setRpm);
                response.resume(Response.ok(pump).build());
            } catch (Exception e) {
                response.resume(e);
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
    public void setRodExposure(@QueryParam("setRod") float setRod, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Reactor_DTO reactor = controlService.setRodExposure(setRod);
                response.resume(Response.ok(reactor).build());
            } catch (Exception e) {
                response.resume(e);
            }
        });
        executorService.shutdown();
    }

    @Operation(
            summary = "Timeout settings for simulation",
            description = "Sets the timeout for the simulation in milliseconds."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Timeout set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request. Timeout must be a positive integer"),
            @ApiResponse(responseCode = "500", description = "Problem with Server")
    })
    @PUT
    @Path("/timeout")
    @Produces(MediaType.APPLICATION_JSON)
    public void setTimeout(@QueryParam("setTimeout") int setTimeout, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Timeout_DTO timeout = controlService.setTimeout(setTimeout);
                response.resume(Response.ok(timeout).build());
            } catch (Exception e) {
                response.resume(e);
            }
        });
        executorService.shutdown();
    }

    @Operation(
            summary = "Slowdown factor for simulation",
            description = "Sets the slowdown factor for the simulation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slowdown factor set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request. Slowdown factor must be a positive integer"),
            @ApiResponse(responseCode = "500", description = "Problem with Server")
    })
    @PUT
    @Path("/slowdownFactor")
    @Produces(MediaType.APPLICATION_JSON)
    public void setSlowdownFactor(@QueryParam("setSlowdownFactor") int setSlowdownFactor, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                SlowdownFactor_DTO SlowdownFactor = controlService.setSlowdownFactor(setSlowdownFactor);
                response.resume(Response.ok(SlowdownFactor).build());
            } catch (Exception e) {
                response.resume(e);
            }
        });
        executorService.shutdown();
    }

}