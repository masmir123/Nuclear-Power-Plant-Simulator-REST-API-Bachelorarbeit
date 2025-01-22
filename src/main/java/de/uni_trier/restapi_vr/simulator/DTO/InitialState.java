package de.uni_trier.restapi_vr.simulator.DTO;

import de.uni_trier.restapi_vr.simulator.component.*;

public class InitialState {

    int reactorControlRodsPosition;
    boolean reactorOverheat;
    int reactorMeltStage;
    float reactorPressure;
    float reactorWaterLevel;
    boolean reactorBlown;

    boolean sv1Blown;
    boolean sv1Open;
    boolean sv2Blown;
    boolean sv2Open;

    boolean wv1Blown;
    boolean wv2Blown;
    boolean wv1Open;
    boolean wv2Open;

    int wp1Rpm;
    boolean wp1Blown;
    int wp2Rpm;
    boolean wp2Blown;
    int cpRpm;
    boolean cpBlown;

    boolean turbineBlown;

    float condenserWaterLevel;
    float condenserPressure;
    boolean condenserBlown;

    int generatorPower;


    public InitialState(Reactor reactor, SteamValve SV1, SteamValve SV2, WaterValve WV1, WaterValve WV2, Pump WP1, Pump WP2, Pump CP, Turbine turbine, Condenser condenser, Generator generator) {

        this.reactorControlRodsPosition = reactor.getModeratorPosition();
        this.reactorOverheat = reactor.isOverheated();
        this.reactorMeltStage = reactor.getMeltStage();
        this.reactorPressure = reactor.getPressure();
        this.reactorWaterLevel = reactor.getWaterLevel();
        this.reactorBlown = reactor.isBlown();

        this.sv1Blown = SV1.isBlown();
        this.sv1Open = SV1.getStatus();
        this.sv2Blown = SV2.isBlown();
        this.sv2Open = SV2.getStatus();

        this.wv1Blown = WV1.isBlown();
        this.wv1Open = WV1.getStatus();
        this.wv2Blown = WV2.isBlown();
        this.wv2Open = WV2.getStatus();

        this.wp1Rpm = WP1.getRPM();
        this.wp1Blown = WP1.isBlown();
        this.wp2Rpm = WP2.getRPM();
        this.wp2Blown = WP2.isBlown();
        this.cpRpm = CP.getRPM();
        this.cpBlown = CP.isBlown();

        this.turbineBlown = turbine.isBlown();

        this.condenserWaterLevel = condenser.getWaterLevel();
        this.condenserPressure = condenser.getPressure();
        this.condenserBlown = condenser.isBlown();

        this.generatorPower = generator.getPower();
    }

    public boolean isStateChanged(Reactor reactor, SteamValve SV1, SteamValve SV2, WaterValve WV1, WaterValve WV2, Pump WP1, Pump WP2, Pump CP, Turbine turbine, Condenser condenser, Generator generator) {
        return reactor.getModeratorPosition() != reactorControlRodsPosition ||
                reactor.isOverheated() != reactorOverheat ||
                reactor.getMeltStage() != reactorMeltStage ||
                reactor.getPressure() != reactorPressure ||
                reactor.getWaterLevel() != reactorWaterLevel ||
                reactor.isBlown() != reactorBlown ||

                SV1.isBlown() != sv1Blown ||
                SV1.getStatus() != sv1Open ||
                SV2.isBlown() != sv2Blown ||
                SV2.getStatus() != sv2Open ||

                WV1.isBlown() != wv1Blown ||
                WV1.getStatus() != wv1Open ||
                WV2.isBlown() != wv2Blown ||
                WV2.getStatus() != wv2Open ||
                WP1.getRPM() != wp1Rpm ||
                WP1.isBlown() != wp1Blown ||
                WP2.getRPM() != wp2Rpm ||
                WP2.isBlown() != wp2Blown ||
                CP.getRPM() != cpRpm ||
                CP.isBlown() != cpBlown ||

                turbine.isBlown() != turbineBlown ||

                condenser.getWaterLevel() != condenserWaterLevel ||
                condenser.getPressure() != condenserPressure ||
                condenser.isBlown() != condenserBlown ||

                generator.getPower() != generatorPower;
    }
}