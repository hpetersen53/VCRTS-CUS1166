package main;

import java.util.Date;

public class Vehicle {
	private String make;
	private String model;
	private int year;
	private String color;
	private String vin;
	private String licensePlate;
	private String residency;
	private boolean available;
	

	// initialize a new vehicle object
	public Vehicle(String make, String model, int year, String color, String vin, String licensePlate, double residency) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
		this.vin = vin;
		this.licensePlate = licensePlate;
	}

	// gets vehicle details
	public String getDetails() {
		return "Vehicle Details:\n" + "Make: " + make + "\n" + "Model: " + model + "\n" + "Year: " + year + "\n"
				+ "Color: " + color + "\n" + "VIN: " + vin + "\n" + "License Plate: " + licensePlate + "\n" + "Time Available: " + residency;
	}

	// format to write vehicle details to a file.
	public String toFileString() {
		return make + "," + model + "," + year + "," + color + "," + vin + "," + licensePlate + "," + residency;
	}
	
	public boolean getAvailability() {
		return available;
	}
	
	public void arrive() {
		available = true;
	}
	
	public void depart() {
		available = false;
	}
	
	public String getVIN() {
		return vin;
	}
}