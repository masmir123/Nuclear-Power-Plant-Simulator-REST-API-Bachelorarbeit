package com.example.restapi_vr.simulator.component;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A generic tank component for the nuclear power plant
 * 
 * @version 1.0f
 * @author Henrik Eriksson
 */
public class Tank extends Component {

	private float pressure;
	private float waterLevel;
	
	/** Blow up the tank */
	public void blow() {
		blown = true;
	}

	/** Array of bubble depths */
	private int bubble_depth[] = new int[100];

	/** Array of bubble X positions */
	private int bubble_x[] = new int[100];

	/**
	 * Paint animated bubbles in a region
	 * 
	 * @param g
	 *            The graphics context
	 * @param x
	 *            The X position for the bubble region
	 * @param y
	 *            The Y position for the bubble region
	 * @param size().width
	 *            The size().width of the bubble region
	 * @param size().height
	 *            The size().height of the bubble region
	 * @param noOfBubbles
	 *            The bubble frequency
	 */
	protected final void paintBubbles(Graphics g, int x, int y, int width,
			int depth, int noOfBubbles) {
		
		Color oldForeground = g.getColor();
		g.setColor(Color.white);
		if (noOfBubbles > bubble_depth.length)
			noOfBubbles = bubble_depth.length;
		for (int i = 0; i < noOfBubbles; i++) {
			if (bubble_depth[i] == 0) { // Create a new bubble
				bubble_depth[i] = rand(1, depth);
				bubble_x[i] = rand(x, width);
			}
			g.fillRect(bubble_x[i], y + bubble_depth[i]--, 1, 1);
			Thread.yield();
		}
		g.setColor(oldForeground);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
