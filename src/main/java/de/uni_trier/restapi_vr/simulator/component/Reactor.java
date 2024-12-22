package de.uni_trier.restapi_vr.simulator.component;

import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JComponent;

/**
 * A reactor tank component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Reactor extends Component {

	private float waterLevel;
	private float pressure;
	private boolean overheated;
	private int moderatorPercent;
	private int meltStage;
	private int coreTemperature;
	private int thermicOutput;
	private ArrayBlockingQueue<Integer> poiseningFactor;
	
	private String label;
	
	private JComponent parentViz;
	
	// critical values
	
	// water level
	public static final int MIN_WATER_LEVEL = 700;
	public static final int MAX_WATER_LEVEL = 2400;
	public static final int LOWER_WATER_LEVEL_THRESHOLD = 1500;
	public static final int CRITICAL_WATER_LEVEL_THRESHOLD = 1000;
	
	// pressure
	public static final int MIN_PRESSURE = 1;
	public static final int MAX_PRESSURE = 500;
	public static final int UPPER_PRESSURE_THRESHOLD = 450;
	
	// temperature
	public static final int MIN_TEMPERATURE = 20;
	public static final int LOWER_THRESHOLD_TEMPERATURE = 200;
	public static final int UPPER_THRESHOLD_TEMPERATURE = 300;
	public static final int MAX_TEMPERATURE = 350;
	
	// view values
	private int x = 10;
	private int y = 425;
	
	public Reactor(int moderatorPosition, boolean overheated, int meltStage, float pressure, float waterLevel, boolean blow) {
		this.moderatorPercent = moderatorPosition;
		this.overheated = overheated;
		this.meltStage = meltStage;
		this.pressure = pressure;
		this.waterLevel = waterLevel;
		this.blown = blow;
		this.poiseningFactor = new ArrayBlockingQueue<Integer>(100);
		for ( int i = 0 ; i < 100; i++) poiseningFactor.add(new Integer(moderatorPercent));
//		System.out.println(poiseningFactor);
	}

	public void meltdown() {
		overheated = true;
		meltStage = 5;
	}

//	public void paintMeltdown(Graphics g) {
//
//		int x0 = -60, y0 = 110;
//		if (meltStage == 5)
//			g.drawImage(radiationSign, 170, 150, parentViz);
//		if (meltStage < 1500) {
//			int d = meltStage;
//			int x = Math.max(x0 - (d - 100), 0)
//					+ rand(0, Math.min(d, parentViz.getSize().width));
//			int y = Math.max(y0 - (d - 100), 0)
//					+ rand(0, Math.min(d, parentViz.getSize().height));
//			g.copyArea(x, y, rand(10, 100), rand(10, 100), ((d < 250) ? rand(-3, 3) : rand(-2, 2)), ((d < 250) ? rand(-3, 3) : rand(-2,2)));
//			meltStage++;
//		}
//	}
//
//	
	
	public int getModeratorPosition() {
		return this.moderatorPercent;
	}
	
	public void setModeratorPosition(int modpos) {
		this.moderatorPercent = modpos;
		try {
			for ( int i = 0 ; i < 10; i++) {
				poiseningFactor.poll();
				poiseningFactor.put(new Integer(modpos));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isOverheated() {
		return overheated;
	}
	
	public void setOverheated(boolean overh) {
		this.overheated = overh;
	}
	
	public int getMeltStage() {
		return meltStage;
	}
	
	public void setMeltStage(int meltSt) {
		this.meltStage = meltSt;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public float getWaterLevel() {
		return waterLevel;
	}
	
	public void setWaterLevel(float level) {
		this.waterLevel = level;
	}
	
	public boolean isBlown() {
		return blown;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void blow() {
		blown = true;
	}
	
	public Integer getPoisiningFactor() {
		int res =  (poiseningFactor.peek() - moderatorPercent);
		return res;
	}

}
