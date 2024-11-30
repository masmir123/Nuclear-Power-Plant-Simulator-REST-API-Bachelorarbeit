package com.example.restapi_vr.simulator.component;
import javax.swing.JComponent;


/**
 * Class for components of the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public abstract class Component extends JComponent{
	
	protected JComponent parentViz;
	
	/** True iff component broken */
	protected boolean blown = false;

	public abstract void blow();
	
	public abstract void update();
	
	public boolean isBlown() {
		return blown;
	}
	
	public static int rand(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}
}