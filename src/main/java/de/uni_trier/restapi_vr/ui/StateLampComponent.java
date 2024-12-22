package de.uni_trier.restapi_vr.ui;

import java.awt.*;
import javax.swing.*;

public class StateLampComponent extends JPanel implements Runnable {

	private static final long serialVersionUID	= -2219441906018439819L;
//	private ImageIcon lampImage					= new ImageIcon(System.class.getResource("/machine/chocolaterie/birne.png"));
	private Color lampColor						= Color.red;
	private Double minThreshold					= new Double(0.0);
	private Double maxThreshold					= new Double(1.0);
	private Color c1 = Color.red;
	private Color c2 = Color.green;
	private Color c3 = Color.yellow;
	
	private boolean blinkFlag = true;
	private boolean isBlinking = false;
	private boolean blinkC1 = false;
	private boolean blinkC2 = false;
	private boolean blinkC3 = false;
	
	public boolean isBlinkC1() {
		return blinkC1;
	}

	public void setBlinkC1(boolean blinkC1) {
		this.blinkC1 = blinkC1;
	}

	public boolean isBlinkC2() {
		return blinkC2;
	}

	public void setBlinkC2(boolean blinkC2) {
		this.blinkC2 = blinkC2;
	}

	public boolean isBlinkC3() {
		return blinkC3;
	}

	public void setBlinkC3(boolean blinkC3) {
		this.blinkC3 = blinkC3;
	}

	public StateLampComponent() {
		
		super();
		setStateValueDouble(0.0);
		
		Thread t = new Thread(this);
		t.start();
	}
	
	public StateLampComponent(Double value) {
		
		super();
		setStateValueDouble(value);
	}
	
	public void paintComponent(Graphics g){
		
//		g.setColor(Color.white);
//		g.fillRect(0, 0, getSize().width, getSize().height);
//		g.drawImage(lampImage.getImage(), 0,0, null);
		if (isBlinking) {
			if (blinkFlag) g.setColor(this.lampColor);
			else g.setColor(Color.gray);
		} else {
			g.setColor(this.lampColor);
		}
//		g.fillOval(0, 0, this.getWidth(), this.getHeight());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setColor(Color.darkGray);
		
		int distance = 6;
	    int horiz = this.getHeight() / distance;
	    int vert = this.getWidth() / distance;
	    
	    for ( int i = 0; i <= horiz; i++) 
	    	g.drawLine(0, i*distance, this.getWidth(), i*distance);
	    
	    for ( int i = 0; i <= vert; i++)
	    	g.drawLine(i*distance, 0, i*distance, this.getHeight());
	    
	    g.setColor(Color.black);
	    
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		g.drawRect(1, 1, this.getWidth()-3, this.getHeight()-3);
	}
	
	public void setStateValue(Object o) {
		if ( o instanceof Double ) setStateValueDouble((Double) o );
		if ( o instanceof Integer ) setStateValueDouble(new Double((Integer)o));
		if ( o instanceof Boolean ) setStateValueBoolean((Boolean) o );
		repaint();
	}
	
	private void setStateValueDouble(Double value) {
			
		if (value.doubleValue() < this.minThreshold) {
			this.lampColor = c3;
			if (blinkC1) isBlinking = true;
			else isBlinking = false;
		}
		else if (value.doubleValue() > this.maxThreshold) {
			this.lampColor = c1;
			if (blinkC3) isBlinking = true;
			else isBlinking = false;
		}
		else {
			this.lampColor = c2;
			if (blinkC2) isBlinking = true;
			else isBlinking = false;
		}
		
	}

	private void setStateValueBoolean(Boolean value) {
		Color c = this.lampColor;
		if (value.booleanValue()) {
			this.lampColor = c1;
			if (blinkC1) isBlinking = true;
			else isBlinking = false;
//			firePropertyChange("lampColor", true, false);
		} else {
			this.lampColor = c2;
			if (blinkC2) isBlinking = true;
			else isBlinking = false;
//			firePropertyChange("lampColor", true, false);
		}
		if(c != this.lampColor){ 
			firePropertyChange("lampColor", true, false);
		}
		
	}
	
	public Boolean getStateValueBoolean() {
		if(this.lampColor == c1){
			return true;
		}
		else if(this.lampColor == c2){
			return false;
		}
		else {
			return null;
		}
	}
	
	public Color getColor(){
		return lampColor;
	}
	
	public void setMinThreshold(Double value) {
		
		this.minThreshold = value;
	}
	
	public void setMaxThreshold(Double value) {
		
		this.maxThreshold = value;
	}
	
	public Double getMinThreshold() {
		
		return this.minThreshold;
	}
	
	public Double getMaxThreshold() {
		
		return this.maxThreshold;
	}
	
	public void setColor1(Color c1) {
		this.c1 = c1;
	}
	
	public void setColor2(Color c2) {
		this.c2 = c2;
	}
	
	public void setColor3(Color c3) {
		this.c3 = c3;
	}

	@Override
	public void run() {
		while(true) {
			blinkFlag = !blinkFlag;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
		
	}
}

