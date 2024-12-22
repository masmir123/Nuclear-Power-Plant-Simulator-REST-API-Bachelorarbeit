package de.uni_trier.restapi_vr.simulator.component;

import java.awt.Color;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * Condenser component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Condenser extends Component {

	private float waterLevel;
	private float pressure;

	private Image[] water = new Image[3];
	private Image coolingRod;
	private Image crashedCondenser;
	private Color waterColor = new Color(0, 128, 255);
	private int imageState = 0;
	private String label;

	private JComponent parentViz;
	
	// water level
	public static final int MIN_WATER_LEVEL = 300;
	public static final int MAX_WATER_LEVEL = 5000;
	public static final int LOWER_WATER_LEVEL_THRESHOLD = 2000;
	
	// pressure
	public static final int MIN_PRESSURE = 0;
	public static final int MAX_PRESSURE = 140;
	public static final int UPPER_PRESSURE_THRESHOLD = 120;
	
	public Condenser(int waterLevel, int pressure, boolean blown) {
		this.waterLevel = waterLevel;
		this.pressure = pressure;
		this.blown = blown;
	}
	
	public float getWaterLevel() {
		return waterLevel;
	}
	
	public void setWaterLevel(float level) {
		this.waterLevel = level;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public boolean isBlown() {
		return blown;
	}

	@Override
	public void blow() {
		blown = true;
	}

	@Override
	public void update() {
		
	}
}
