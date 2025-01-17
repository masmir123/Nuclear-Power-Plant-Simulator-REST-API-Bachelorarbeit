package de.uni_trier.restapi_vr.simulator.component;

import javax.swing.JComponent;

/**
 * A pump component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Pump extends Component {

	/** The state of the pump (0=crashed) */
	private int rpm;
	private int setRPM;
	private int blowcounter;
	private int upperRpmThreshold;
	private int maxRpm;
	
	private final int BLOW_COUNTER_INIT = 200;
	
	private JComponent parentViz;

	public Pump(int rpm, int maxRpm, int upperRpmThreshold, boolean blown) {
		this.rpm = rpm;
		this.setRPM = rpm;
		this.upperRpmThreshold = upperRpmThreshold;
		this.maxRpm = maxRpm;
		this.blown = blown;
		this.blowcounter = BLOW_COUNTER_INIT;
	}

	/** Blow up the pump */
	public void blow() {
		rpm = 0;
		setRPM = 0;
		blown = true;
	}
	
	public void update() {
		if ( ! isBlown() ) {
			if ( rpm > upperRpmThreshold ) blowcounter--;
			else if ( blowcounter < BLOW_COUNTER_INIT ) blowcounter++;
			if ( blowcounter < 0 ) blow();
			if ( rpm != setRPM )
				if ( rpm > setRPM ) rpm = setRPM + ((rpm - setRPM)/2);
				else rpm = setRPM - ((setRPM - rpm)/2);
		}
	}
	
	public int getRPM() {
		return rpm;
	}
	
	public void setRPM(int rpm) {
//		if ( !isBlown() ) this.rpm = rpm;
		if ( !isBlown() ) setRPM = rpm;
		else this.rpm = 0;
	}
	
	public int getSetRPMN() {
		return setRPM;
	}
	
	public int getUpperRPMThreshold() {
		return upperRpmThreshold;
	}
	
	public int getMaxRPM() {
		return maxRpm;
	}

	public int getBlowCounter() {
		return blowcounter;
	}
}