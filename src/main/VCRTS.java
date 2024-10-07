package main;

import gui.GUIPanel;
import gui.GUIWindow;

public class VCRTS implements Runnable {

	private GUIWindow guiWindow;
	private GUIPanel guiPanel;
	
	
	public VCRTS() {
		
		guiPanel = new GUIPanel(this);
		guiWindow = new GUIWindow(guiPanel);
		guiPanel.requestFocus();
		
		
		
	}
	
	
	@Override
	public void run() {
		
		
	}

}
