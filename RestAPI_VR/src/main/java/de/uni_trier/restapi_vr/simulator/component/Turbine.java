package de.uni_trier.restapi_vr.simulator.component;



/**
 * Turbine component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Turbine extends Component {

	public Turbine(boolean blown) {
		this.blown = blown;
	}

	public void blow() {
		blown = true;
	}
	
	public boolean isBlown()  {
		return this.blown;
	}

	@Override
	public void update() {
		
	}
}
