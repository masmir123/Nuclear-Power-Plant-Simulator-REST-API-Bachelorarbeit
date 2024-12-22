package de.uni_trier.restapi_vr.simulator.component;

import java.awt.Image;

import javax.swing.JComponent;

/**
 * A valve component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class SteamValve extends Component {

	/** True iff the valve is open */
	boolean status = false;
	private String label;
	private int x;
	private int y;

	/** Valve image */
	protected static Image ventil_o_bm, ventil_s_bm;
	
	private JComponent parentViz;
	
	/**
	 * Construct a valve and initialize it.
	 * 
	 * @param l
	 *            The valve label
	 * @param xPos
	 *            The X coordinate
	 * @param yPos
	 *            The Y coordinate
	 */
	public SteamValve(boolean blown, boolean status) {
		this.status = status;
		this.blown = blown;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(boolean s) {
		if ( !blown ) this.status = s;
	}
	
	public boolean isBlown() {
		return this.blown;
	}

	@Override
	public void blow() {
		blown = true;
	}

	@Override
	public void update() {
		
	}

}