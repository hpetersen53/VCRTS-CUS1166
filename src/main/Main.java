package main;


import gui.ControllerDashboard;

public class Main {
	
	public static void main(String[] args){

		VCController vcController = new VCController();
		ControllerDashboard controllerDashboard = new ControllerDashboard();
		controllerDashboard.startServer();
		
	}

}
