package de.uni_trier.restapi_vr.simulator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import de.uni_trier.restapi_vr.simulator.DTO.Component_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Components_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Pump_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Valve_DTO;
import de.uni_trier.restapi_vr.simulator.component.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


public class NPPSystemInterface implements Runnable {

    //Logging
    private static Logger logger = LoggerFactory.getLogger(NPPSystemInterface.class);
    private static Marker reactorMarker = MarkerFactory.getMarker("REACTOR");

    private Instant startDate;

    private Thread nppSimulatorThread = null;

    //private NPPAutomation automation;

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

    private static final NPPSystemInterface INSTANCE = new NPPSystemInterface();


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

    private NPPSystemInterface() {
        init();
    }

    private void init() {

        components = new Vector<Component>();

        initSimulation();

        STATE = normalState;
        tempTime = System.currentTimeMillis();

        // Save Date since simulation starts
        startDate = Instant.now();


        start();
        //automation = new NPPAutomation(this);
        //automation.start();

    }

    public void start() {
        if (nppSimulatorThread == null) {
            logger.info(reactorMarker, "Starting NPPSystemInterfaceThread");
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
        //logSystemState("Stopp");
        logger.debug(reactorMarker, "Stop Simulation");
        wait = true;
    }

    public boolean isSimulationRunning() {
        return !wait;
    }

    /*
     * Interface implementiation for controlling from outside
     */

    // Update Function for WV by ID
    public void updateValveStatus(String id, boolean activate) {
        switch (id) {
            case "SV1" -> {
                SV1.setStatus(activate);
            }
            case "SV2" -> {
                SV2.setStatus(activate);
            }
            case "WV1" -> {
                WV1.setStatus(activate);
            }
            case "WV2" -> {
                WV2.setStatus(activate);
            }
        }
    }


    // UpdateRpm Function for Pump by ID
    public void updatePumpRpm(String id, int setRpm) {
        switch (id) {
            case "WP1" -> {
                WP1.setRPM(setRpm);
            }
            case "WP2" -> {
                WP2.setRPM(setRpm);
            }
            case "CP" -> {
                CP.setRPM(setRpm);
            }
        }
    }

    // Function to return startDate of the simulation
    public String getSimulationStartTime() {
        if (startDate != null) {
            return startDate.toString();
        }
        throw new IllegalStateException("Simulation is not running");
    }

    // Function to update control rods
    public void setControlRodExposure(int exposurePercentage) {
        if (exposurePercentage < 0 || exposurePercentage > 100) {
            throw new IllegalArgumentException("Exposure percentage must be between 0 and 100.");
        }

        reactor.setModeratorPosition(exposurePercentage);
    }

    // Function to add all Components for RESTService
    public List<Component_DTO> getComponents() {
        List<Component_DTO> components = new ArrayList<>();

        components.add(new Component_DTO("WV1", WV1.isBlown(), true));
        components.add(new Component_DTO("WV2", WV2.isBlown(), true));
        components.add(new Component_DTO("WP1", WP1.isBlown(), true));
        components.add(new Component_DTO("WP2", WP2.isBlown(), true));
        components.add(new Component_DTO("Reactor", reactor.isBlown(), true));
        components.add(new Component_DTO("Turbine", turbine.isBlown(), false));
        components.add(new Component_DTO("Cooling Pump", CP.isBlown(), true));
        components.add(new Component_DTO("SV1", SV1.isBlown(), true));
        components.add(new Component_DTO("SV2", SV2.isBlown(), true));
        components.add(new Component_DTO("Condenser", condenser.isBlown(), false));
        components.add(new Component_DTO("Generator", generator.isBlown(), false));

        return components;
    }

    public Map<String, Pump_DTO> getPumps(){
        Pump_DTO[] pumps = new Pump_DTO[]{
            new Pump_DTO("WP1", WP1.isBlown(), WP1.getRPM(), WP1.getSetRPMN(), WP1.getMaxRPM()),
            new Pump_DTO("WP2", WP2.isBlown(), WP2.getRPM(), WP2.getSetRPMN(), WP2.getMaxRPM()),
            new Pump_DTO("CP", CP.isBlown(), CP.getRPM(), CP.getSetRPMN(), CP.getMaxRPM())
        };

        return Arrays.stream(pumps).collect(Collectors.toMap(Pump_DTO::getName, c -> c));
    }

    public Map<String, Valve_DTO> getValves(){
        Valve_DTO[] valves = new Valve_DTO[]{
                new Valve_DTO("SV1", SV1.isBlown(), SV1.getStatus()),
                new Valve_DTO("SV2", SV2.isBlown(), SV2.getStatus()),
                new Valve_DTO("WV1", WV1.isBlown(), WV1.getStatus()),
                new Valve_DTO("WV2", WV2.isBlown(), WV2.getStatus())
        };

        return Arrays.stream(valves).collect(Collectors.toMap(Valve_DTO::getName, c -> c));
    }



    // Functions to restart simulation
    public void restartSimulation() {
        try {
            this.shutdownSimulation();
            this.initializeSimulation();
        } catch (Exception e) {
            throw new RuntimeException("Error restarting simulation: " + e.getMessage());
        }
    }

    private void shutdownSimulation() {
        System.out.println("Shutting down the simulation...");
        this.wait = true;
    }

    private void initializeSimulation() {
        System.out.println("Initializing the simulation...");
        components.clear();
        wait = false;
        init();
    }

    public List<Components_DTO> getComponentsHealth() {
        List<Components_DTO> components = new ArrayList<>();

        /*
        RKS = ReaktorStatus
        RKT = ReaktorTank
        KNT = CondenserStatus
        TBN = Turbine
        WP1 = WP1
        WP2 = WP2
        CP = CoolingPump
        AU = AtomicStatus
         */

        components.add(new Components_DTO("WV1", WV1.isBlown()));
        components.add(new Components_DTO("WV2", WV2.isBlown()));
        components.add(new Components_DTO("WP1", WP1.isBlown()));
        components.add(new Components_DTO("WP2", WP2.isBlown()));
        components.add(new Components_DTO("RKS", reactor.isBlown()));
        components.add(new Components_DTO("RKT", !getReactorTankStatus()));
        components.add(new Components_DTO("TBN", turbine.isBlown()));
        components.add(new Components_DTO("CP",  CP.isBlown()));
        components.add(new Components_DTO("SV1", SV1.isBlown()));
        components.add(new Components_DTO("SV2", SV2.isBlown()));
        components.add(new Components_DTO("KNT", condenser.isBlown()));
        components.add(new Components_DTO("AU",  !getAtomicStatus()));
        components.add(new Components_DTO("Generator", generator.isBlown()));

        return components;
    }

    public static NPPSystemInterface getInstance() {
        return INSTANCE;
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


    //Reaktorgift.
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



    // Eigene Getter

    public Boolean getSV1_BlownStatus() {
        return SV1.isBlown();
    }

    public Boolean getSV2_BlownStatus() {
        return SV2.isBlown();
    }

    public boolean getWP1_BlownStatus() {
        return WP1.isBlown();
    }

    public boolean getWP2_BlownStatus() {
        return WP2.isBlown();
    }

    public boolean getGenerator_BlownStatus() {
        return generator.isBlown();
    }

    public boolean getOverheatedStatus() {
        return reactor.isOverheated();
    }



    public void logSystemState(){
        int generatorPower = generator.getPower();
        int moderatorPosition = reactor.getModeratorPosition();
        String reactorWaterLevel = String.valueOf(reactor.getWaterLevel()).replace(".", ",");
        String reactorPressure = String.valueOf(reactor.getPressure()).replace(".", ",") ;
        String reactorBlown = String.valueOf(reactor.isBlown());
        String condenserWaterLevel = String.valueOf(condenser.getWaterLevel()).replace(".", ",");
        String condenserPressure =  String.valueOf(condenser.getPressure()).replace(".", ",");
        String condenserBlown = String.valueOf(condenser.isBlown());

        logger.debug(reactorMarker,
                "\nSYSTEM: WaterLvl: Con: {}, Rea: {}; Pres: Con: {}, Rea: {}; ModP: {}; GenPow: {}; RPM: WP1: {}, WP2: {}, CP: {}; Valves: SV1: {}, SV2: {}, WV1: {}, WV2: {}" +
                "\nBLOWN: Rea: {}, Con: {}, Tur: {}, WP1: {}, WP2: {}, CP: {}, SV1: {}, SV2: {}, Gen: {}" +
                "\nHEALTH: Pumps(Blowcounter): WP1: {}, WP2: {}, CP: {}; Reactor(Meltstage, Overheated): {}, {}",
                condenserWaterLevel, reactorWaterLevel, condenserPressure, reactorPressure, moderatorPosition, generatorPower, WP1.getRPM(), WP2.getRPM(), CP.getRPM(), SV1.getStatus(), SV2.getStatus(), WV1.getStatus(), WV2.getStatus(),
                reactorBlown, condenserBlown, turbine.isBlown(), WP1.isBlown(), WP2.isBlown(), CP.isBlown(), SV1.isBlown(), SV2.isBlown(), generator.isBlown(),
                WP1.getBlowCounter(), WP2.getBlowCounter(), CP.getBlowCounter(), reactor.getMeltStage(), reactor.isOverheated()
        );
    }

}
