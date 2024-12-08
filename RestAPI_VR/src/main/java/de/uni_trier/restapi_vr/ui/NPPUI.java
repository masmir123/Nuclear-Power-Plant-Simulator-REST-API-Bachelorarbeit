package de.uni_trier.restapi_vr.ui;


//import NPPSystemInterface;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
//import java.util.HashMap;

public class NPPUI {
	private final JSlider sliderCPRPM;
	private final NPPSystemInterface nppSysInterface;
	private JFrame frame = new JFrame();
	private JPanel contentBase; //Basispanel, auf dem alle Komponenten liegen, gleichzeitig LayeredPane
	
	//Labels
	private final JLabel labelCPRPM;
	private final JLabel labelWP1RPM;
	private final JSlider sliderWP1RPM;
	private final JLabel labelWP2RPM;
	private final JSlider sliderWP2RPM;
	private final JLabel labelRodPos;
	private final JSlider sliderRodPos;
	
	private final JLabel labelPowerOutlet;
	private final JLabel lLeistung;
	
	private JLabel lwp1Pump;
	private JLabel lwp2Pump;
	private JLabel lkpPump;
	
	private JLabel wPumpCap;
	private JLabel valvesCap; 
	private JLabel rPosCap;
	
	private JLabel reactorCap;
	private JLabel reactorWatCap;
	private JLabel reactorPresCap;
	
	private JLabel kondensatorCap;
	private JLabel kondensatorWatCap;
	private JLabel kondensatorPresCap;
	
	//Vertical Components Reactor and Condensor
	private final VerticalLevelComponent wLevelReac; //Vertical Component Reactor Waterlevel
	private final VerticalLevelComponent pressReac;  // and Pressure
	private final VerticalLevelComponent wLevelKonden;  // Vertical Component Condensor Waterlevel 
	private final VerticalLevelComponent pressKonden; // and Pressure

	//Lamps - States and PreferredSize
	private final StateLampComponent lampFV1;
	private final StateLampComponent lampFV2;
	private final StateLampComponent lampWV1;
	private final StateLampComponent lampWV2;
	
	//Buttons
	private JButton bOpFV1;
	private JButton bCLFV1; 
	private JButton bOpFV2;
	private JButton bCLFV2; 
	
	private JButton bOpWV1;
	private JButton bCLWV1; 
	private JButton bOpWV2;
	private JButton bCLWV2; 

	//Kontrolllampen und deren Labels
	private JLabel[] lLamps;
	private StateLampComponent[] lamps;
	
	
	
	
	
	public NPPUI() {
		super(); //Initialisierung der Map

		createBaseFrame();
		
	//Variables	 
	 nppSysInterface = NPPSystemInterface.getInstance(); // = Singleton NPPSystemInterface();
	 
	 
	//Beschriftungen
	labelCPRPM = createLabel("0 RPM", new Rectangle(320, 605, 70, 25), "labelKP");
	labelWP1RPM = createLabel("0 RPM", new Rectangle(320, 485, 70, 25), "labelWP1");
	labelWP2RPM = createLabel("0 RPM", new Rectangle(320, 545, 70, 25), "labelWP2"); 
	labelRodPos = createLabel("100 %", new Rectangle(390, 410, 70, 25), "labelRodPos");
	
	lwp1Pump = createLabel("WP1", new Rectangle(15, 485, 35, 25), "lwp1Pump");
	lwp2Pump = createLabel("WP2", new Rectangle(15, 545, 35, 25), "lwp2Pump");
	lkpPump = createLabel("CP", new Rectangle(15, 605, 35, 25), "lkpPump");
	
	wPumpCap = createLabel("Water Pumps", new Rectangle(135, 450, 100, 25), "wPumpCap");
	valvesCap = createLabel("Valves", new Rectangle(540, 470, 70, 25), "valvesCap");
	rPosCap = createLabel("<html><body> Control Rods <br/> Level (%) </body></html>", new Rectangle(15, 410, 100, 30), "rPosCap");	
	
	
	labelPowerOutlet = createLabel("0 MW", new Rectangle(60, 25, 60, 25), "labelPowerOutlet");
	lLeistung = createLabel("Power", new Rectangle(5, 25, 55, 25), "lLeistung");
	
	
	//Reactor - Caption
	reactorCap = createLabel("Reactor", new Rectangle(180, 80, 50, 25), "reactorCap");
	reactorWatCap = createLabel("<html><body> Water Level <br/> (mm) </body></html>", new Rectangle(90, 100, 100, 30), "reactorWatCap");
	reactorPresCap = createLabel("<html><body> Pressure <br/> (bar) </body></html>", new Rectangle(250, 100, 100, 30), "reactorPresCap");

	kondensatorCap = createLabel("Condenser", new Rectangle(560, 80, 100, 25), "kondensatorCap");
	kondensatorWatCap = createLabel("<html><body> Water Level <br/> (mm) </body></html>", new Rectangle(490, 100, 100, 30), "kondensatorWatCap");
	kondensatorPresCap = createLabel("<html><body> Pressure <br/> (bar) </body></html>", new Rectangle(650, 100, 70, 30), "kondensatorPresCap");
	
	//Vertical Components
	//Reactor
	wLevelReac = new VerticalLevelComponent(); //Vertical Component Reactor Waterlevel
	wLevelReac.setBounds(80, 135, 100, 200);
	contentBase.add(wLevelReac);
//	componentMap.put("wLevelReac", wLevelReac);
	pressReac = createVertical(550, 20, new Rectangle(215, 135, 100, 200), "pressReac");
	
	wLevelReac.setThresholds(1500, 1200, 2500, 2800);
	pressReac.setThresholds(-1, -1, 350, 450);
	
	//Kondenser
	wLevelKonden = createVertical(8000, 200, new Rectangle(480, 135, 100, 200), "wLevelKonden");
	pressKonden = createVertical(180, 10, new Rectangle(610, 135, 100, 200), "pressKonden");
	
	wLevelKonden.setThresholds(1500, 800, 6000, 7000);
	pressKonden.setThresholds(-1, -1, 80, 110);
	
	bOpFV1 = createButton("Open SV1", new Rectangle(440, 500, 100, 30), "bOpFV1");
	bCLFV1 = createButton("Close SV1", new Rectangle(440, 530, 100, 30), "bCLFV1");
	bOpFV2 = createButton("Open SV2", new Rectangle(580, 500, 100, 30), "bOpFV2");
	bCLFV2 = createButton("Close SV2", new Rectangle(580, 530, 100, 30), "bCLFV2");
	
	bOpWV1 = createButton("Open WV1", new Rectangle(440, 570, 100, 30), "bOpWV1");
	bCLWV1 = createButton("Close WV1", new Rectangle(440, 600, 100, 30), "bCLWV1");
	bOpWV2 = createButton("Open WV2", new Rectangle(580, 570, 100, 30), "bOpWV2");
	bCLWV2 = createButton("Close WV2", new Rectangle(580, 600, 100, 30), "bCLWV2");
	
	lampFV1 = createLamp(true, new Rectangle(540, 500, 30, 60), "lampFV1");
	lampFV2 = createLamp(true, new Rectangle(680, 500, 30, 60), "lampFV2");
	lampWV1 = createLamp(true, new Rectangle(540, 570, 30, 60), "lampWV1");
	lampWV2 = createLamp(true, new Rectangle(680, 570, 30, 60), "lampWV2");
	
	lLamps = new JLabel[8];
	lamps = new StateLampComponent[lLamps.length];
	String[] lLampsNames = {"RKS", "RKT", "KNT", "TBN", "WP1", "WP2", "CP", "AU"};
	for (int j = 0; j < lLamps.length; j++) {
	  lLamps[j] = new JLabel(lLampsNames[j], JLabel.CENTER);		
	  lLamps[j].setHorizontalTextPosition(JLabel.CENTER);
	  lLamps[j].setBounds(400+j*(35), 40, 30, 25);
	  lamps[j] = new StateLampComponent();
	  lamps[j].setBounds(400+j*(35), 5, 30, 30);
	  contentBase.add(lLamps[j]);
	  contentBase.add(lamps[j]);
//	  componentMap.put("lamp" + lLampsNames[j], lamps[j]);
//	  componentMap.put("lblamp" + lLampsNames[j], lLamps[j]);
//	  componentMap.put("lblamp" + lLamps[j].getText(), lLamps[j]);
	}
	
	
	
	final class MyReader implements Runnable {

		@Override
		public void run() {
			while (true) {
				labelCPRPM.setText(String.valueOf(nppSysInterface.getCPRPM()) + " U/Min");
				labelWP1RPM.setText(String.valueOf(nppSysInterface.getWP1RPM())  + " U/Min");
				labelWP2RPM.setText(String.valueOf(nppSysInterface.getWP2RPM())  + " U/Min");
				labelRodPos.setText(String.valueOf(nppSysInterface.getRodPosition())  + " %");
				labelPowerOutlet.setText(String.valueOf(nppSysInterface.getPowerOutlet() ) + " MW");
				wLevelReac.setValue(nppSysInterface.getWaterLevelReactor());
				pressReac.setValue(nppSysInterface.getPressureReactor());
				wLevelKonden.setValue(nppSysInterface.getWaterLevelCondenser());
				pressKonden.setValue(nppSysInterface.getPressureCondenser());
				if(nppSysInterface.getReactorStatus() == false){
					lamps[0].setStateValue(true);
				}
				if(nppSysInterface.getReactorTankStatus() == false){
					lamps[1].setStateValue(true);
				}
				if(nppSysInterface.getCondenserStatus() == false){
					lamps[2].setStateValue(true);
				}
				if(nppSysInterface.getTurbineStatus() == false){
					lamps[3].setStateValue(true);
				}
				if(nppSysInterface.getWP1Status() == false){
					lamps[4].setStateValue(true);
				}
				if(nppSysInterface.getWP2Status() == false){
					lamps[5].setStateValue(true);
				}
				if(nppSysInterface.getCPStatus() == false){
					lamps[6].setStateValue(true);
				}
				if(nppSysInterface.getAtomicStatus() == false){
					lamps[7].setStateValue(true);
				}
				
			}
		}
	}
	
	
	
	//Slider CPRPM
		sliderCPRPM = new JSlider(0, 2000, 0);  //Slider mit Mind.-, Max.- und Startwert
		configSlider(sliderCPRPM, 50, 595, 250, true, "sliderCPRPM");		
		
		sliderCPRPM.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				nppSysInterface.setCPRPM(sliderCPRPM.getValue());
			}
		});	
	
		
	//Slider WP1RPM	
		sliderWP1RPM = new JSlider(0, 2000, 0);  //Slider mit Mind.-, Max.- und Startwert
		configSlider(sliderWP1RPM, 50, 475, 250, true, "sliderWP1RPM");
		
		sliderWP1RPM.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				nppSysInterface.setWP1RPM(sliderWP1RPM.getValue());
			}
		});	
		
		
	
	//Slider WP2RPM	
		sliderWP2RPM = new JSlider(0, 2000, 0);  //Slider mit Mind.-, Max.- und Startwert
		configSlider(sliderWP2RPM, 50, 535, 250, true, "sliderWP2RPM");
				
		sliderWP2RPM.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				nppSysInterface.setWP2RPM(sliderWP2RPM.getValue());
			}
		});		
		
	
	//Slider Regelstaebe	
		sliderRodPos = new JSlider(0, 100, 0);  //Slider mit Mind.-, Max.- und Startwert
		configSlider(sliderRodPos, 120, 400, 10, false, "sliderRodPos");
		sliderRodPos.setInverted(true);		
		sliderRodPos.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				nppSysInterface.setReactorModeratorPosition(sliderRodPos.getValue());
			}
		});		
	
		
		
	(new Thread(new MyReader())).start();	
	
	//Action-Listener for Buttons
	bOpWV1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setWV1Status(true);
			lampWV1.setStateValue(false);
		}
		
	});
	
	
	bCLWV1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setWV1Status(false);
			lampWV1.setStateValue(true);
		}
		
	});
	
	bOpWV2.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setWV2Status(true);
			lampWV2.setStateValue(false);
		}
		
	});
	
	
	bCLWV2.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setWV2Status(false);
			lampWV2.setStateValue(true);
		}
		
	});

	bOpFV1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setSV1Status(true);
			lampFV1.setStateValue(false);
		}
		
	});
	
	
	bCLFV1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setSV1Status(false);
			lampFV1.setStateValue(true);
		}
		
	});
	
	bOpFV2.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setSV2Status(true);
			lampFV2.setStateValue(false);
		}
		
	});
	
	
	bCLFV2.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			nppSysInterface.setSV2Status(false);
			lampFV2.setStateValue(true);
		}
		
	});
	
//	System.out.println(componentMap.keySet());
	frame.setVisible(true);
	}  //End Constructor

	
	
	private void configSlider(JSlider slider, int xPos, int yPos, int tickSpace, boolean standardLabels, String mapKey){
		slider.setMinorTickSpacing(tickSpace);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		if(standardLabels==true){
		  slider.setLabelTable(slider.createStandardLabels(500, 0));
		}
		slider.setBounds(xPos, yPos, 250, 50);
		contentBase.add(slider);
//		componentMap.put(mapKey, slider);
	}
		
	private JLabel createLabel(String text, Rectangle bounds, String mapKey){
		JLabel lb = new JLabel(text);
		lb.setBounds(bounds);
		lb.setHorizontalAlignment(JLabel.CENTER);
		contentBase.add(lb);
//		componentMap.put(mapKey, lb);
		return lb;
	}
	
	private StateLampComponent createLamp(boolean state, Rectangle bounds, String mapKey){
		StateLampComponent sLC = new StateLampComponent();
		sLC.setStateValue(state);
		sLC.setBounds(bounds);
		contentBase.add(sLC);
//		componentMap.put(mapKey, sLC);
		return sLC;
	}
	
	private VerticalLevelComponent createVertical(int maxValue, int separation, Rectangle bounds, String mapKey){
		VerticalLevelComponent vLC = new VerticalLevelComponent(maxValue, separation);
		vLC.setBounds(bounds);
		contentBase.add(vLC);
//		componentMap.put(mapKey, vLC);
		return vLC;
	}
	
	private void createBaseFrame(){
		//Set the UI to a panel; -> this is the LayeredPane
		contentBase = new JPanel();
		contentBase.setLayout(null);
		contentBase.setSize(900, 650);
		frame.getLayeredPane().add(contentBase);		//Panel is now THE LayeredPane
		frame.setSize(new Dimension(900,680));
//		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
	
	private JButton createButton(String caption, Rectangle bounds, String mapKey){
		JButton bt = new JButton(caption);
		bt.setBounds(bounds);
		contentBase.add(bt);
//		componentMap.put(mapKey, bt);
		return bt;
	}
	
  public JFrame getFrame(){
	  return frame;
  }
  
}
