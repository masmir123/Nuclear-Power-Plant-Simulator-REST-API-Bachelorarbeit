package de.uni_trier.restapi_vr.simulator.component;

import javax.swing.JComponent;

/**
 * Generator component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Generator extends Component {

	/** Output generator power (in MW) */
	private int power;
	
	private JComponent parentViz;
	
	public Generator(int power) {
		this.power = power;
	}
	
	public Generator(JComponent parentViz) {
		this.parentViz = parentViz;
	}

	public int getPower() {
		return power;
	}
	
	public void setPower(int p) {
		if ( !blown ) this.power = p;
		else this.power = 0;
	}
	
	public void update() {
		
	}

	@Override
	public void blow() {
		blown = true;
	}

}
