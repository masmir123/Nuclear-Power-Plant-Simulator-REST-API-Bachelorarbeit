package de.uni_trier.restapi_vr.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class VerticalLevelComponent extends JComponent {
	
	private Integer value;
	private Integer setValue;
	
	private int max;
	private int min;
	
	private int sep;
	
	private int minTh1;
	private int minTh2;
	private int maxTh1;
	private int maxTh2;
	
	private Color normalColor;
	private Color minTh1Color;
	private Color minTh2Color;
	private Color maxTh1Color;
	private Color maxTh2Color;
	private Color selectColor;
	
	private int width, height;
	private int textwidth, frame;
	private int[] xPolygon, yPolygon;
	
	private boolean isValueSetable = false;
	private boolean showSetValue = false;
	private boolean reverseScale = false;
	
	public VerticalLevelComponent() {
		this(false);
	}

	
	//public VerticalLevelComponent(int minTH1, int minTh2, int maxTh1, int maxTh2, int max, int separation) {
	public VerticalLevelComponent(int max, int separation) {
	    this(false);
		
		this.max = max;
		this.sep = separation;
		
//		this.minTh1 = minTH1;
//		this.minTh2 = minTh2;
//		this.maxTh1 = maxTh1;
//		this.maxTh2 = maxTh2;
	}
	
	
	
	public VerticalLevelComponent(boolean isComplex) {
		min = 0;
		max = 4000;
		sep = 100;
		value = 0;
		setValue = 0;
		
		minTh1 = 1800;
		minTh2 = 1000;
		maxTh1 = 3000;
		maxTh2 = 3500;
		
		normalColor = Color.green;
		minTh1Color = Color.orange;
		minTh2Color = Color.red;
		maxTh1Color = Color.orange;
		maxTh2Color = Color.red;
		selectColor = Color.blue;
		
		textwidth = 50;
		frame = 10;
		width = height = 100;
		
		this.isValueSetable = isComplex;
		
		initInteractiveBehaviour();
	}
	
	public void setValue(Integer i) {
		int valueRef = value;
		if ( i < min ) value = min;
		else if ( i > max ) value = max;
		else value = i;
		this.repaint();
		if(valueRef != value){
			firePropertyChange("value", true, false);
		}
	}
	
	public void setValueSetable(boolean isSetable) {
		this.isValueSetable = isSetable;
	}
	
	public void setShowSetValue(boolean showSetValue) {
		this.showSetValue = showSetValue;
	}
	
	public void setMinValue(int min) {
		this.min = min;
	}
	
	public void setMaxValue(int max) {
		this.max = max;
	}
	
	public void setSepartion(int sep) {
		this.sep = sep;
	}
	
	public void setMinThreshold1(int minTh1) {
		this.minTh1 = minTh1;
	}
	
	public void setMinThreshold2(int minTh2) {
		this.minTh2 = minTh2;
	}
	
	public void setMaxThreshold1(int maxTh1) {
		this.maxTh1 = maxTh1;
	}
	
	public void setMaxThreshold2(int maxTh2) {
		this.maxTh2 = maxTh2;
	}
	
	public void setThresholds(int min1, int min2, int max1, int max2) {
		this.minTh1 = min1;
		this.minTh2 = min2;
		this.maxTh1 = max1;
		this.maxTh2 = max2;
	}
	
	
	public Color getNormalColor() {
		return normalColor;
	}

	public void setNormalColor(Color normalColor) {
		this.normalColor = normalColor;
	}

	public Color getMinTh1Color() {
		return minTh1Color;
	}

	public void setMinTh1Color(Color minTh1Color) {
		this.minTh1Color = minTh1Color;
	}

	public Color getMinTh2Color() {
		return minTh2Color;
	}

	public void setMinTh2Color(Color minTh2Color) {
		this.minTh2Color = minTh2Color;
	}

	public Color getMaxTh1Color() {
		return maxTh1Color;
	}

	public void setMaxTh1Color(Color maxTh1Color) {
		this.maxTh1Color = maxTh1Color;
	}

	public Color getMaxTh2Color() {
		return maxTh2Color;
	}

	public void setMaxTh2Color(Color maxTh2Color) {
		this.maxTh2Color = maxTh2Color;
	}

	public Color getSelectColor() {
		return selectColor;
	}

	public void setSelectColor(Color selectColor) {
		this.selectColor = selectColor;
	}

	public Integer getValue() {
		return value;
	}
	
	public boolean getValueSetable() {
		return this.isValueSetable;
	}
	
	public boolean getShowSetValue() {
		return this.showSetValue;
	}
	
	public int getMinValue() {
		return this.min;
	}
	
	public int getMaxValue() {
		return this.max;
	}
	
	public int getSepartion() {
		return this.sep;
	}
	
	public int getMinThreshold1() {
		return this.minTh1;
	}
	
	public int getMinThreshold2() {
		return this.minTh2;
	}
	
	public int getMaxThreshold1() {
		return this.maxTh1;
	}
	
	public int getMaxThreshold2() {
		return this.maxTh2;
	}	
	
	
	private void setSetValue(Point p) {
		setValue = getValueFromPoint(p, setValue);
	}
	private void setValue(Point p) {
		value = getValueFromPoint(p, value);
	}
	
	private int getValueFromPoint(Point p, int defaultValue) {
		int returnValue = defaultValue;
		
		int y = p.y-frame;
		int intervall = max-min;
		int newValue = (int)Math.rint(intervall - ((double)intervall * ((double)(y) / (double)(height))));

		if ((y > frame) || (y < (height + frame))) {
			if ( newValue > max ) returnValue = max;
			else if ( newValue < min ) returnValue = min;
			else returnValue = newValue;
		}
		
		return returnValue;
	}
		
	public void paintComponent(Graphics gd) {
		
		width = this.getWidth()-1;
		height = this.getHeight()-1-(2*frame);
		Graphics g = gd.create();
		
		int minLabel = min;
		int maxLabel = max;	
		
		int minDraw = min;
		int maxDraw = max;
		
		if ( max <= min && reverseScale ) {
			minLabel = min;
			maxLabel = max;
			reverseScale = false;
		} else if ( reverseScale ) {
			minLabel = max;
			maxLabel = min;
		}
		
		if ( min > max && !reverseScale ) {
			minLabel = max;
			maxLabel = min;
			reverseScale = true;
		}
		
		// Rahmenbeschriftungen
		g.setColor(Color.black);
		g.drawLine(3 * textwidth / 4, frame, textwidth, frame);
		g.drawLine(3 * textwidth / 4, height+frame, textwidth, height+frame);
		g.drawString(String.valueOf(minLabel), 0, height+6+frame);
		g.drawString(String.valueOf(maxLabel), 0, 16);
		
		// Darstellung des gesetzten Werts
		if ( showSetValue && isValueSetable ) {
			g.setColor(selectColor);
			int actValueY2 = (int)Math.rint(((double)(max-setValue)/(double)(max-min))*(double)height)+frame;
			
			g.drawLine(3 * textwidth / 4, actValueY2, width, actValueY2);
			
			xPolygon = new int[]{0, (3 * textwidth / 4)-10, 3 * textwidth / 4, (3 * textwidth / 4)-10, 0,0};
			yPolygon = new int[]{actValueY2-10, actValueY2-10, actValueY2, actValueY2+10, actValueY2+10, actValueY2-10};
			g.fillPolygon(xPolygon, yPolygon, 6);
			
			if ( actValueY2 < 10 ) actValueY2 = 10;
			g.setColor(Color.white);
			g.drawString(String.valueOf(setValue), 0, actValueY2+4);
		}
		
		// Darstellung des aktuellen Werts
		int actValueY = (int)Math.rint(((double)(max-value)/(double)(max-min))*(double)height)+frame;
		if (value <= minTh1 && value > minTh2) g.setColor(minTh1Color);
		else if (value <= minTh2) g.setColor(minTh2Color);
		else if (value >= maxTh1 && value < maxTh2) g.setColor(maxTh1Color);
		else if (value > maxTh2) g.setColor(maxTh2Color);
		else g.setColor(normalColor);
		g.fillRect(textwidth, actValueY, width, height-actValueY+frame);
		g.drawLine(3 * textwidth / 4, actValueY, width, actValueY);
		
		xPolygon = new int[]{0, (3 * textwidth / 4)-10, 3 * textwidth / 4, (3 * textwidth / 4)-10, 0,0};
		yPolygon = new int[]{actValueY-10, actValueY-10, actValueY, actValueY+10, actValueY+10, actValueY-10};
		g.fillPolygon(xPolygon, yPolygon, 6);
		
		if ( actValueY < 10 ) actValueY = 10;
		g.setColor(Color.white);
		g.drawString(String.valueOf(value), 0, actValueY+4);
		
		// Rahmen und Einteilung
		g.setColor(Color.black);
		g.drawRect(textwidth, frame, width - textwidth, height);
		
		int numSep = (int)(((double)(max-min))/((double)sep))-1;
		double numSepPixD = (double)height / ((((double)(max-min))/(double)sep)-1);
		
		int[] yCoord = new int[numSep];
		double tmp = 0;
		double error = 0;
		
		for ( int i = 0; i < numSep; i++) {
			tmp += numSepPixD;
			yCoord[i] = (int) Math.rint(tmp+error);
			error = tmp - Math.rint(tmp);
		}
		
		for ( int i = 0; i < numSep; i++)
			g.drawLine(textwidth, yCoord[i]+frame, textwidth + 10, yCoord[i]+frame);

		
	}
	
	private void initInteractiveBehaviour() {
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if ( e.getPoint().x <= textwidth && isValueSetable ) 
					setSetValue(e.getPoint());
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
				
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				if ( e.getPoint().x <= textwidth && isValueSetable ) 
					setSetValue(e.getPoint());
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
			
		});
	}

}
