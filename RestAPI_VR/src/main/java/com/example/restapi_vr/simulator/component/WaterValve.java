package com.example.restapi_vr.simulator.component;



public class WaterValve extends Component {

	boolean status = false;
	
	public WaterValve(boolean blown, boolean status) {
		this.status = status;
		this.blown = blown;
	}
	
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(boolean s) {
		if (s != this.status) {
			firePropertyChange("status", this.status, s);
		}
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
