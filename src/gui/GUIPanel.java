package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import main.VCRTS;

public class GUIPanel extends JPanel{
	
	//private MouseInputs mouseInputs;
	private VCRTS vcrts;
	
	public GUIPanel(VCRTS vcrts){
		
		this.vcrts = vcrts;
		setPanelSize();
		
	}
	
	//window will be fixed 1000x600
	public void setPanelSize() {
		Dimension size = new Dimension(1000,600);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}

}
