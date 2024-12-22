package de.uni_trier.restapi_vr.controller;

import de.uni_trier.restapi_vr.simulator.DTO.*;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import de.uni_trier.restapi_vr.service.SimulationService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Package: com.example.restapi_vr.controller
 */
@Path("/simulation")
@Tag(name = "Simulation Endpoints")
public class SimulationController {

    @Resource
    ExecutorService executorService;

    private final SimulationService simulationService = new SimulationService(NPPSystemInterface.getInstance());


    // GET RESPONSE for Valve Info
    @Operation(
            summary = "Get the status of a specified valve by ID",
            description = "Retrieve the status of a specified valve by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valve status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of valve Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/valve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void ValveInfo(@PathParam("id") int id, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Valve_DTO valve = simulationService.getValveStatus(id);
                response.resume(Response.ok(valve).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    // GET RESPONSE for Pump Info
    @Operation(
            summary = "Get the status of a specified pump by ID",
            description = "Retrieve informative data about the status of a pump by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pump status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of pump Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/pump/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void PumpInfo(@PathParam("id") int id, @Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Pump_DTO pump = simulationService.getPumpStatus(id);
                response.resume(Response.ok(pump).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    // GET RESPONSE for Generator Info
    @Operation(
            summary = "Get the status of the generator",
            description = "Retrieve informative data about the current status of the generator"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generator status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of generator Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/generator")
    @Produces(MediaType.APPLICATION_JSON)
    public void GeneratorInfo(@Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Generator_DTO generator = simulationService.getGeneratorStatus();
                response.resume(Response.ok(generator).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    // GET RESPONSE for Condenser Info
    @Operation(
            summary = "Get the status of the condenser",
            description = "Retrieve informative data about the current status of the condenser"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Condenser status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of condenser Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/condenser")
    @Produces(MediaType.APPLICATION_JSON)
    public void CondenserInfo(@Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Condenser_DTO condenser = simulationService.getCondenserStatus();
                response.resume(Response.ok(condenser).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    // GET RESPONSE for Reactor Info
    @Operation(
            summary = "Get the status of the reactor",
            description = "Retrieve informative data about the current status of the reactor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reactor status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of reactor Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/reactor")
    @Produces(MediaType.APPLICATION_JSON)
    public void ReactorInfo(@Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Reactor_DTO reactor = simulationService.getReactorStatus();
                response.resume(Response.ok(reactor).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }

    //GET RESPONSE for Component Info
    @Operation(
            summary = "Get the status of all components",
            description = "Retrieve informative data about all components"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reactor status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Retrieving of reactor Status failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public void Health(@Suspended final AsyncResponse response) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                List<Components_DTO> components = simulationService.getComponentsHealth();
                response.resume(Response.ok(components).build());
            } catch (IllegalArgumentException e) {
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
            } catch (Exception e) {
                response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
        executorService.shutdown();
    }





}