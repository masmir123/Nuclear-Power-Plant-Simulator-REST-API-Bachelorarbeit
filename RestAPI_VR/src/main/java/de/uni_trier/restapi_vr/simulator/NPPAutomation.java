package de.uni_trier.restapi_vr.simulator;

public class NPPAutomation implements Runnable {

    private NPPSystemInterface simulator;

    private Thread automationThread;

    // Visible variables

    private boolean scram;

    // Automation parameters

    private int deltaWL;

    private int waterPumpStepSize = 100;

    public NPPAutomation(NPPSystemInterface simulator) {
        this.simulator = simulator;

        this.scram = false;
    }

    public void start() {
        if (automationThread == null) {
            automationThread = new Thread(this);
            automationThread.setName("NPPAutomationThread");
            automationThread.start();
        }
    }

    @Override
    public void run() {

        // local variables
        int wl1 = simulator.getWaterLevelReactor();

        while (simulator.isSimulationRunning()) {

            if (simulator.getWaterLevelReactor() > 2500 || simulator.getWaterLevelReactor() < 1900) {

                deltaWL = simulator.getWaterLevelReactor() - wl1;

                System.out.println(deltaWL);

                if (deltaWL < 0) {
                    int newRPM = this.simulator.getWP1RPM() + (Math.abs(deltaWL) + Math.abs(this.simulator.getWaterLevelReactor() - 2200));
                    if (this.simulator.getWaterLevelReactor() < 2200)
                        if (newRPM >= 0)
                            this.simulator.setWP1RPM(newRPM);
                } else if (deltaWL > 0) {
                    int newRPM = this.simulator.getWP1RPM() - (Math.abs(deltaWL) + Math.abs(this.simulator.getWaterLevelReactor() - 2200));
                    if (this.simulator.getWaterLevelReactor() > 2200)
                        if (newRPM >= 0)
                            this.simulator.setWP1RPM(newRPM);
                } else {
                    if ( this.simulator.getWaterLevelReactor() > 2200 ) this.simulator.setWP1RPM(0);
                    if ( this.simulator.getWaterLevelReactor() < 1800 ) this.simulator.setWP1RPM(1800);
                }
            }

            if (simulator.getWaterLevelReactor() > 2800 || simulator.getWaterLevelReactor() < 1500) this.simulator.setReactorModeratorPosition(0);

            // update local variables
            wl1 = simulator.getWaterLevelReactor();

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {}

        }




    }

    private void scram() {
        System.out.println("SCRAM1");

        this.simulator.setReactorModeratorPosition(0);
        // this.simulator.setSV2Status(true);
        // this.simulator.setSV1Status(false);
        // this.simulator.setWV1Status(true);
        // this.simulator.setWV2Status(false);

        // Control WL / Pressure / Waterflow down to a steddy state
        // while ( this.simulator.getPressureReactor() > 10 ) {
        // // this.simulator.setWP1RPM(2000 *
        // ((this.simulator.getWaterLevelReactor() / 2200)-1));
        // }

        // System.out.println("SCRAM2");
        //
//		 int wl1 = simulator.getWaterLevelReactor();
//
//		 while ( this.simulator.getWaterLevelReactor() > 2300 ||
//		 this.simulator.getWaterLevelReactor() < 2100 ) {
//		 deltaWL = simulator.getWaterLevelReactor() - wl1;
//
//		 System.out.println(deltaWL);
//
//		 if ( deltaWL < 0 ) {
//		 if ( this.simulator.getWaterLevelReactor() < 2200 )
//		 this.simulator.setWP1RPM(this.simulator.getWP1RPM() +
//		 Math.abs(deltaWL));
//		 } else if ( deltaWL > 0 ) {
//		 if ( this.simulator.getWaterLevelReactor() > 2200 )
//		 this.simulator.setWP1RPM(this.simulator.getWP1RPM() -
//		 Math.abs(deltaWL));
//		 } else {
//		 if ( this.simulator.getWaterLevelReactor() < 2200 )
//		 this.simulator.setWP1RPM(this.simulator.getWP1RPM() +
//		 waterPumpStepSize);
//		 if ( this.simulator.getWaterLevelReactor() > 2200 )
//		 this.simulator.setWP1RPM(this.simulator.getWP1RPM() -
//		 waterPumpStepSize);
//		 }
//		 wl1 = simulator.getWaterLevelReactor();
//		 try {
//		 Thread.sleep(500);
//		 } catch (InterruptedException e) {}
//
//		 }

    }

}
