package main;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {
	String make;
	String model;
	int year;
	String color;
	String vin;
	String licensePlate;
	double residency;
	boolean available;

	// initialize a new vehicle object
	public Vehicle(String make, String model, int year, String color, String vin, String licensePlate,
			double residency) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
		this.vin = vin;
		this.licensePlate = licensePlate;
		this.residency = residency;
	}

	public String getVIN() {
		return vin;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public int getYear() {
		return year;
	}

	public String getColor() {
		return color;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public double getResidency() {
		return residency;
	}

	public boolean getAvailability() {
		if (residency == 0) {
			return false;
		} else {
			return available;
		}
	}

	public void arrive() {
		available = true;
	}

	public void depart() {
		available = false;
	}

	// -----------------

	// gets vehicle details
	public String getDetails() {
		return "Vehicle Details:\n" + "Make: " + make + "\n" + "Model: " + model + "\n" + "Year: " + year + "\n"
				+ "Color: " + color + "\n" + "VIN: " + vin + "\n" + "License Plate: " + licensePlate + "\n"
				+ "Time Available: " + residency;
	}

}