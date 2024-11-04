package main;

import java.util.Date;

class Vehicle {
	private String make;
	private String model;
	private int year;
	private String color;
	private String vin;
	private String licensePlate;
	private String residency;
	

	// initialize a new vehicle object
	public Vehicle(String make, String model, int year, String color, String vin, String licensePlate, String residency2) {
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
	
	public Date getAvailability() {
		return null;
	}
	
	public void arrive() {
		
	}
	
	public void depart() {
		
	}
}