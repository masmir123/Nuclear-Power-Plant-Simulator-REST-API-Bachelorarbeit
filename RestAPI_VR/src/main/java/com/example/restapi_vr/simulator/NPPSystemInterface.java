package com.example.restapi_vr.simulator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.simulator.component.Component;
import org.simulator.component.Condenser;
import org.simulator.component.Generator;
import org.simulator.component.Pump;
import org.simulator.component.Reactor;
import org.simulator.component.SteamValve;
import org.simulator.component.Turbine;
import org.simulator.component.WaterValve;

@Path("/npp")
public class NPPSystemInterface implements Runnable {

    private Thread nppSimulatorThread = null;

    private NPPAutomation automation;

    private Reactor reactor;
    private SteamValve SV1, SV2;
    private WaterValve WV1, WV2;
    private Pump WP1, WP2, CP;
    private Turbine turbine;
    private Condenser condenser;
    private Generator generator;

    private Vector<Component> components;

    private boolean open = true;

    private int restheat = 0;

    private BufferedWriter logstream;

    private long tempTime;

    private long countdownPump = 210000;
    private long countdownTurbine = 270000;
    private long countdownSAA, countdownSAB, countdownSAC;
    private boolean flagA, flagB, flagC;

    private int pumpDamage = 0;
    private int turbineDamage = 1;
    private int saTest = 2;
    private int normalState = 3;

    private int STATE = normalState;
    private boolean wait = false;

    private boolean log = true;

    // Simulation parameter

    public static final int PRESSURE_MAX_THRESHOLD_REACTOR = 400;
    public static final int PRESSURE_MAX_THRESHOLD_TRUBINE = 400;
    public static final int PRESSURE_MAX_THRESHOLD_CONDENSER = 200;
    public static final int WATERLEVEL_MAX_THRESHOLD_REACTOR = 4000;
    public static final int WATERLEVEL_MIN_THRESHOLD_REACTOR = 1500;
    public static final int WATERLEVEL_MAX_THRESHOLD_CONDENSER = 4000;

    public static final int RESTHEAT = 200;
    public static final double RESTHEAT_REDUCING_FACTOR = 0.0001;

    public void close() {
        nppSimulatorThread = null;
        open = false;
        try {
            if ( logstream != null ) {
                logstream.flush();
                logstream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public void init() {
        components = new Vector<Component>();

        initSimulation();

        STATE = normalState;
        tempTime = System.currentTimeMillis();
        start();
        automation = new NPPAutomation(this);
        automation.start();

    }

    public void start() {
        if (nppSimulatorThread == null) {
            nppSimulatorThread = new Thread(this);
            nppSimulatorThread.setName("NPPSystemInterfaceThread");
            nppSimulatorThread.start();
        }
    }

    protected void initSimulation() {
        reactor = new Reactor(100, false, 1, 0, 2000, false);
        components.add(reactor);
        SV1 = new SteamValve(false, false);
        components.add(SV1);
        SV2 = new SteamValve(false, false);
        components.add(SV2);
        WV1 = new WaterValve(false, false);
        components.add(WV1);
        WV2 = new WaterValve(false, false);
        components.add(WV2);
        WP1 = new Pump(0, 2000, 1800, false);
        components.add(WP1);
        WP2 = new Pump(0, 2000, 1800, false);
        components.add(WP2);
        CP = new Pump(0, 2000, 1800, false);
        components.add(CP);
        turbine = new Turbine(false);
        components.add(turbine);
        condenser = new Condenser(4000, 0, false);
        components.add(condenser);
        generator = new Generator(0);
        components.add(generator);
    }



    protected void timeStep(int n) {
        float v1, v2, v3, v4;

        if ( !wait ) {

            for (int i = 0; i < n; i++) {
                if (!reactor.isOverheated()) {
                    // Compute the flow through valve_1...
                    if (SV1.getStatus())
                        v1 = (reactor.getPressure() - condenser.getPressure()) / 10;
                    else
                        v1 = 0;
                    // Compute the flow through valve_2...
                    if (SV2.getStatus())
                        v2 = (reactor.getPressure() - condenser.getPressure()) / 2.5f;
                    else
                        v2 = 0;

                    // Compute the flow through valve_3 and pump_1...
                    if (WV1.getStatus()) {
                        if (WP1.getRPM() > 0) {
                            if (condenser.getWaterLevel() > 0)
                                v3 = WP1.getRPM() * 0.07f;
                            else
                                v3 = 0;
                        } else {
                            if (condenser.getWaterLevel() > 0 &&
                                    (condenser.getWaterLevel() - reactor.getWaterLevel()) > 470
                                    && (SV1.getStatus() || SV2.getStatus()) ) v3 = 2f;
                            else if (condenser.getWaterLevel() > 0 &&
                                    (condenser.getWaterLevel() - reactor.getWaterLevel()) < 470
                                    && (SV1.getStatus() || SV2.getStatus()) ) v3 = -2f;
                            else v3 = 0;
                        }
                        if ( reactor.getWaterLevel() >= (reactor.MAX_WATER_LEVEL + 500) && !SV1.getStatus() ) v3 = 0;
                    } else v3 = 0;

                    // Compute the flow through valve_4 and pump_2...
                    if (WV2.getStatus()) {
                        if (WP2.getRPM() > 0) {
                            if (condenser.getWaterLevel() > 0)
                                v4 = WP2.getRPM() * 0.07f;
                            else
                                v4 = 0;
                        } else {
                            if (condenser.getWaterLevel() > 0 &&
                                    (condenser.getWaterLevel() - reactor.getWaterLevel()) > 470
                                    && (SV1.getStatus() || SV2.getStatus()) ) v4 = 2f;
                            else if (condenser.getWaterLevel() > 0 &&
                                    (condenser.getWaterLevel() - reactor.getWaterLevel()) < 470
                                    && (SV1.getStatus() || SV2.getStatus()) ) v4 = -2f;
                            else v4 = 0;
                        }
                        if ( reactor.getWaterLevel() >= (reactor.MAX_WATER_LEVEL + 500) && !SV1.getStatus()  ) v4 = 0;
                    } else v4 = 0;

                    // Scale the flow levels to allow frequent time steps
                    // (smother animation)
                    float factor = 0.5f;
                    v1 *= factor;
                    v2 *= factor;
                    v3 *= factor;
                    v4 *= factor;

                    // Compute new values for pressure and water levels...
                    float poison = reactor.getPoisiningFactor()/5;
                    float boiledRW = ((100 - reactor.getModeratorPosition()) * 2 * (900 - reactor.getPressure()) / 620);
                    if ( reactor.getModeratorPosition() == 100 ) {
                        restheat = (int)(restheat / (1 + RESTHEAT_REDUCING_FACTOR));
                        if ( poison > 0 ) boiledRW = (boiledRW + restheat) * factor * poison;
                        else boiledRW = (boiledRW + restheat) * factor;
                    } else {
                        restheat = RESTHEAT - (2*reactor.getModeratorPosition());
                        if ( poison > 0 ) boiledRW = boiledRW * factor * poison;
                        else boiledRW = boiledRW * factor;

                    }

                    float cooledKP = (float) (CP.getRPM() * Math.sqrt(condenser.getPressure()) * 0.003f);
                    cooledKP *= factor;

                    float newRP = reactor.getPressure() - v1 - v2 + boiledRW / 4;

                    // The steam flow to the condenser stops if the
                    // turbine is blown...
                    if (turbine.isBlown()) v1 = 0;

                    // Compute new values for pressure and water levels...
                    float newKP = condenser.getPressure() + v1 + v2 - cooledKP;
                    float newRW = reactor.getWaterLevel() + v3 + v4 - boiledRW;
                    float newKW = condenser.getWaterLevel() - v3 - v4 + 4 * cooledKP;

                    // Make adjustments for blown tanks...
                    if (reactor.isBlown()) newRP = 0.15f * newRP;
                    if (condenser.isBlown()) newKP = 0.2f * newKP;

                    // Check the computed values for illegal values...
                    if (newKW < 0) newKW = 0;
                    if (newKW > 9600) newKW = 9600;
                    if (newRW > 4700) newRW = 4700;
                    if (newKP < 0) newKP = 0;
                    if (newKP > 300) newKP = 300;
                    if (newRP > 800) newRP = 800;

                    // Adjust the generator power...
                    float newEffect;
                    if (SV1.getStatus() && !turbine.isBlown())
                        newEffect = (newRP - newKP) * 2.5f;
                    else
                        newEffect = 0;

                    // Assign the computed values...
                    generator.setPower((int) newEffect);
                    condenser.setPressure(newKP);
                    condenser.setWaterLevel(newKW);
                    reactor.setPressure(newRP);
                    reactor.setWaterLevel(newRW);

                    // RULES
                    if (WP1.isBlown()) WP1.setRPM(0);
                    if (WP2.isBlown()) WP2.setRPM(0);
                    if (CP.isBlown()) CP.setRPM(0);

                    if (reactor.getWaterLevel() < Reactor.CRITICAL_WATER_LEVEL_THRESHOLD) reactor.meltdown();
                    if (reactor.getPressure() >= Reactor.MAX_PRESSURE) reactor.blow();
                    if (reactor.getWaterLevel() > (Reactor.MAX_WATER_LEVEL + 500) && SV1.getStatus() ) turbine.blow();

                    if (condenser.getWaterLevel() <= Condenser.MIN_WATER_LEVEL && WP1.getRPM() > 0) WP1.blow();
                    if (condenser.getWaterLevel() <= Condenser.MIN_WATER_LEVEL && WP2.getRPM() > 0) WP2.blow();
                    if (condenser.getPressure() >= Condenser.MAX_PRESSURE) condenser.blow();
                    if (condenser.getWaterLevel() > Condenser.MAX_WATER_LEVEL + 300 ) turbine.blow();

                }

                for ( Component c : components ) c.update();

                if ( log ) logSystemState(null);

                if ( STATE == pumpDamage && !WP1.isBlown() ) {
                    long tt = System.currentTimeMillis();
                    long diff  = (tt - tempTime);
                    countdownPump = countdownPump - diff;
                    if ( countdownPump < 0 ) WP1.blow();
                    tempTime = tt;
                }
                if ( STATE == turbineDamage && !turbine.isBlown() ) {
                    long tt = System.currentTimeMillis();
                    long diff  = (tt - tempTime);
                    if ( countdownTurbine < 0 ) turbine.blow();
                    countdownTurbine = countdownTurbine - diff;
                    tempTime = tt;
                }
                if ( STATE == saTest ) {
                    long tt = System.currentTimeMillis();
                    long diff  = (tt - tempTime);
                    if ( countdownSAA > 0 ) {
                        countdownSAA = countdownSAA - diff;
                    } else if ( countdownSAA <= 0 && flagA ) {
                        flagA = false;
                        stopp();
                        return;
                    }

                    if ( countdownSAB > 0 && !flagA ) {
                        countdownSAB = countdownSAB - diff;
                    } else if ( countdownSAB <= 0 && flagB ) {
                        flagB = false;
                        stopp();
                        return;
                    }

                    if ( countdownSAC > 0 && !flagA && !flagB ) {
                        countdownSAC = countdownSAC - diff;
                    } else if ( countdownSAC <= 0 && flagC ) {
                        flagC = false;
                        stopp();
                        return;
                    }
                    tempTime = tt;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {}

            }
        }

    }


    @Override
    public void run() {
        while (open) {
            this.timeStep(1);
        }

    }

    public void stopp() {
        logSystemState("Stopp");
        wait = true;
    }

    public boolean isSimulationRunning() {
        return !wait;
    }

    /*
     * Interface implementiation for controlling from outside
     */


    @GET
    @Path("/valve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the status of a steam valve",
            description = "Retrieves the status of a specified steam valve by ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Valve status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of Valve status failed"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Invalid valve ID")
    })
    public Response getValveStatus(@PathParam("id") int id) {
        boolean status;
        switch (id) {
            case 1 -> status = SV1.getStatus();
            case 2 -> status = SV2.getStatus();
            default -> {
                return Response.status(Response.Status.NOT_FOUND).entity("Invalid valve ID").build();
            }
        }
        return Response.ok(status).build();
    }

    @GET
    @Path("/pump/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the status of a pump",
            description = "Retrieves the status of a specified pump by ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pump status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Pump_DTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of Pump status failed"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Invalid pump ID")
    })
    public Response getPumpStatus(@PathParam("id") int id) {
        Pump pump;
        switch (id) {
            case 1 -> pump = WP1;
            case 2 -> pump = WP2;
            default -> {
                return Response.status(Response.Status.NOT_FOUND).entity("Invalid pump ID").build();
            }
        }
        return Response.ok(new PumpDTO(pump.getRPM(), !pump.isBlown())).build();
    }

    @GET
    @Path("/generator")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the status of the generator",
            description = "Retrieves the current power output of the generator.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "Generator status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of Generator status failed")
    })
    public Response getGeneratorStatus() {
        int power = generator.getPower();
        return Response.ok(power).build();
    }

    @GET
    @Path("/condenser")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the status of the condenser",
            description = "Retrieves the current status of the condenser.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "Condenser status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Condenser_DTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of Condenser status failed")
    })
    public Response getCondenserStatus() {
        return Response.ok(new CondenserDTO(condenser.getPressure(), condenser.getWaterLevel(), !condenser.isBlown())).build();
    }

    @GET
    @Path("/reactor")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the status of the reactor",
            description = "Retrieves the current status of the reactor.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reactor status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Reactor_DTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of Reactor status failed")
    })
    public Response getReactorStatus() {
        return Response.ok(new ReactorDTO(reactor.getPressure(), reactor.getWaterLevel(), !reactor.isOverheated(), !reactor.isBlown())).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the overall health of the system",
            description = "Checks the overall health status of the NPP system.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "System health retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SystemHealth_DTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving of System health failed")
    })
    public Response getSystemHealth() {
        boolean overallStatus = !reactor.isBlown() && !condenser.isBlown() && !WP1.isBlown() && !WP2.isBlown();
        return Response.ok(new SystemHealthDTO(overallStatus)).build();
    }

    @GET
    @Path("/components")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get an array of all components and their attributes",
            description = "Checks all Components individually and adds them to an array.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "Data of all Components retrieved successfully",
                    content = @Content(schema = @Schema(implementaion = Components_DTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving Data of all Components failed")
    })
    public Response getComponents(){
        try {
            /* Building Array for Data of all Components */
            List<ComponentsDTO> components = new ArrayList<>();

            // Data of Reactor
            components.add(new ComponentsDTO(
                    "Reactor",
                    reactor.getPressure(),
                    reactor.getWaterLevel(),
                    !reactor.isOverheated(),
                    !reactor.isBlown(),
                    false
            ));

            // Data of Steam Valves
            components.add(new ComponentsDTO(
                    "Steam Valve 1",
                    -1,
                    -1,
                    SV1.getStatus(),
                    true,
                    true
            ));
            components.add(new ComponentsDTO(
                    "Steam Valve 2",
                    -1,
                    -1,
                    SV2.getStatus(),
                    true,
                    true
            ));

            // Data of Pumps
            components.add(new ComponentsDTO(
                    "Pump 1",
                    -1,
                    -1,
                    !WP1.isBlown(),
                    true,
                    true
            ));
            components.add(new ComponentsDTO(
                    "Pump 2",
                    -1,
                    -1,
                    !WP2.isBlown(),
                    true,
                    true
            ));
            components.add(new ComponentsDTO(
                    "Cooling Pump",
                    -1,
                    -1,
                    !CP.isBlown(),
                    true,
                    true
            ));

            // Data of Condenser
            components.add(new ComponentsDTO(
                    "Condenser",
                    condenser.getPressure(),
                    condenser.getWaterLevel(),
                    !condenser.isBlown(),
                    true,
                    false
            ));

            // Data of Generator
            components.add(new ComponentsDTO(
                    "Generator",
                    generator.getPower(),
                    -1,
                    true,
                    true,
                    false
            ));

            // Retrieving an answer successfully
            return Response.ok(components).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Retrieving Data of all Components failed: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get information about the Server and the simulation",
            description = "Checks the current status of the server and simulation. Additionally if its running, it puts out the date since when its running.")
    @ApiResponse({
            @ApiResponse(
                    responseCode = "200",
                    description = "Data of Server and simulation retrieved successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Retrieving Data of Server and simulation failed")
    })
    public Response getStatus() {
        return Response.ok(this.isSimulationRunning(),/* Missing Implementation since when the simulation is running */).build();
    }


    ///// SETTER

    public void setReactorModeratorPosition(Integer pos) {
        if ( pos > 100 ) pos = 100;
        if ( pos < 0 ) pos = 0;
        this.reactor.setModeratorPosition(100-pos);
    }

    public void setWP1RPM(Integer rpm) {
        this.WP1.setRPM(rpm);
    }

    public void setWP2RPM(Integer rpm) {
        this.WP2.setRPM(rpm);
    }

    public void setCPRPM(Integer rpm) {
        this.CP.setRPM(rpm);
    }

    public void setSV1Status(Boolean st) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        this.SV1.setStatus(st);
    }

    public void setSV2Status(Boolean st) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        this.SV2.setStatus(st);
    }

    public void setWV1Status(Boolean st) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        this.WV1.setStatus(st);
    }

    public void setWV2Status(Boolean st) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        this.WV2.setStatus(st);
    }

    /// GETTER

    public Boolean getWP1Status() {
        return !WP1.isBlown();
    }

    public Integer getWP1RPM() {
        return WP1.getRPM();
    }

    public Boolean getWP2Status() {
        return !WP2.isBlown();
    }

    public Integer getWP2RPM() {
        return WP2.getRPM();
    }

    public Boolean getCPStatus() {
        return !CP.isBlown();
    }

    public Integer getCPRPM() {
        return CP.getRPM();
    }

    public Boolean getWV1Status() {
        return WV1.getStatus();
    }

    public Boolean getWV2Status() {
        return WV2.getStatus();
    }

    public Boolean getSV1Status() {
        return SV1.getStatus();
    }

    public Boolean getSV2Status() {
        return SV2.getStatus();
    }

    public Integer getWaterLevelReactor() {
        return (int)reactor.getWaterLevel();
    }

    public Integer getWaterLevelCondenser() {
        return (int)condenser.getWaterLevel();
    }

    public Integer getPressureReactor() {
        return (int)reactor.getPressure();
    }

    public Integer getPressureCondenser() {
        return (int)condenser.getPressure();
    }

    public Integer getStandardValue() {
        return new Integer(12);
    }

    public Integer getPowerOutlet() {
        return Integer.valueOf((int)generator.getPower());
    }

    public Boolean getReactorTankStatus() {
        return Boolean.valueOf(!reactor.isBlown());
    }

    public Boolean getReactorStatus() {
        return Boolean.valueOf(!reactor.isOverheated());
    }

    public Boolean getCondenserStatus() {
        return Boolean.valueOf(!condenser.isBlown());
    }

    public Boolean getTurbineStatus() {
        return Boolean.valueOf(!turbine.isBlown());
    }

    public Boolean getKPStatus() {
        return Boolean.valueOf(!CP.isBlown());
    }

    public Boolean getAtomicStatus() {
        if ( reactor.isOverheated() && (
                reactor.isBlown()||condenser.isBlown()||turbine.isBlown()) ) return Boolean.valueOf(!true);
        else return Boolean.valueOf(!false);
    }

    public Integer getRodPosition() {
        return Integer.valueOf(reactor.getModeratorPosition());
    }

    public Integer getWP1RPMSet() {
        return WP1.getSetRPMN();
    }

    public Integer getWP2RPMSet() {
        return WP2.getSetRPMN();
    }

    public Integer getCPRPMSet() {
        return CP.getSetRPMN();
    }



    private void logSystemState(String info){
//		if ( logstream == null ) {
//			try {
//				String t = new Date(System.currentTimeMillis()).toString().replace(" ", "_").replace(":", "_");
//				logstream = new BufferedWriter(new FileWriter(logpath.getFile().concat("\\" + LOG_FILE_NAME + "_" + test + "_" + t + LOG_FILE_SUFFIX).replace("%20", " ")));
//				logstream.write("Time;CondWL;CondPres;ReacWL;ReacPressure;RodPos;Leistung;WP1RPM;WP2RPM;CPRPM;SV1;SV2;WV1;WV2;ReacBLOWN;ReacOverheated;CondBLOWN;WP1BLOWN;WP2BLOWN;CPRMBLOWN\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if ( logcounter == 0) {
//			int generatorPower = generator.getPower();
//			int moderatorPosition = reactor.getModeratorPosition();
//			String reactorWaterLevel = String.valueOf(reactor.getWaterLevel()).replace(".", ",");
//			String reactorPressure = String.valueOf(reactor.getPressure()).replace(".", ",") ;
//			String reactorBlown = convertBoolean(reactor.isBlown());
//			String condenserWaterLevel = String.valueOf(condenser.getWaterLevel()).replace(".", ",");
//			String condenserPressure =  String.valueOf(condenser.getPressure()).replace(".", ",");
//			String condenserBlown = convertBoolean(condenser.isBlown());
//
//			String log =
//				condenserWaterLevel+s+condenserPressure+s+
//				reactorWaterLevel+s+reactorPressure+s+
//				moderatorPosition+s+
//				generatorPower+s+
//				WP1.getRPM()+s+
//				WP2.getRPM()+s+
//				CP.getRPM()+s+
//				convertBoolean(SV1.getStatus())+s+
//				convertBoolean(SV2.getStatus())+s+
//				convertBoolean(WV1.getStatus())+s+
//				convertBoolean(WV2.getStatus())+s+
//				reactorBlown+s+convertBoolean(reactor.isOverheated())+s+condenserBlown+s+convertBoolean(WP1.isBlown())+s+convertBoolean(WP2.isBlown())+s+convertBoolean(CP.isBlown());
//
//
//			try {
//				if ( info != null ) logstream.write(info + s + log + "\n");
//				else logstream.write((System.currentTimeMillis()-startTime) + s + log + "\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else if ( logcounter == logtimer ) logcounter = 0;
//		else logcounter++;
    }

}
