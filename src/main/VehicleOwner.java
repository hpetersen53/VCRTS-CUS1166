package main;
import java.util.ArrayList;
import java.util.List;

import main.Vehicle;

public class VehicleOwner extends User {
	private List <Vehicle> vehicles;

    public VehicleOwner(int id,String fName, String lName, String emailAddr, String password, String licenseNum) {
    	super(id, fName, lName, emailAddr, password, licenseNum);
    	vehicles = new ArrayList<>();
    }
    private Vehicle vehicle;
   
    	
   


    @Override
    public String getDetails() {
        return "Vehicle Owner Details:\n" + "Name: " + fName + " " + lName + "\n" + "Email: " + emailAddr + "\n" + "License Number: " + licenseNum;
    }
	public void lend(/*Vehicle*/) {
		
	}


	public void ownVehicle(Vehicle vehicle2) {
		vehicles.add(vehicle);
	}
}
