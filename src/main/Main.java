package main;


import gui.ControllerDashboard;

public class Main {
	
	public static void main(String[] args){

		VCController vcController = new VCController();
		ControllerDashboard controllerDashboard = new ControllerDashboard();
		new Thread(() -> {
			System.out.println("Starting Server 1...");
			controllerDashboard.startServer();
		}).start();
		new Thread(() -> {
			System.out.println("Starting Server 2...");
			controllerDashboard.startServer2();
		}).start();

	}

}
