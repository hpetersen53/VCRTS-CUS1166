package main;

import java.sql.*;

public class Database {
	
	static Connection connection = null;
	//this part is the address and name of your database server: jdbc:mysql://localhost:3306/VC3
	//this part of the string is for time adjustment: ?useTimezone=true&serverTimezone=UTC
	static String url = "jdbc:mysql://localhost:3306/vcrts_db";
	static String username = "root";
	static String password = "99044";
	
	public static void insertVehicleOwner(VehicleOwner vehicleOwner) {
		
		
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			String sql = "INSERT INTO vehicleOwner VALUES(" + vehicleOwner.id + ", " + vehicleOwner.fName + ", " + vehicleOwner.lName + ", " + vehicleOwner.emailAddr + ", " + vehicleOwner.password + ", " + vehicleOwner.licenseNum + ");";
			//establishes the connection session
			Statement statement = connection.createStatement();
			//executes the query 
			int row = statement.executeUpdate(sql);
			//the return value is the indication of success or failure of the query execution
			if (row > 0)
				System.out.println("Data was inserted!");

			connection.close();
			
		} catch (SQLException e) {
			e.getMessage();

		}
	}
	
	public static void insertVehicle(Vehicle vehicle, VehicleOwner vehicleOwner) {
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			String sql = "INSERT INTO vehicle VALUES(" + vehicle.vin + ", " + vehicle.make + ", " + vehicle.model + ", " + vehicle.color + ", " + vehicle.year + ", " + vehicle.licensePlate + ", " + vehicle.residency + ", " + vehicleOwner.id + ");";
			//establishes the connection session
			Statement statement = connection.createStatement();
			//executes the query 
			int row = statement.executeUpdate(sql);
			//the return value is the indication of success or failure of the query execution
			if (row > 0)
				System.out.println("Data was inserted!");

			connection.close();
			
		} catch (SQLException e) {
			e.getMessage();

		}
	}
	
	public static void insertClient(Client client) {
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			String sql = "INSERT INTO vehicle VALUES(" + client.id + ", " + client.fName + ", " + client.lName + ", " + client.emailAddr + ", " + client.password + ", " + client.licenseNum + ");";
			//establishes the connection session
			Statement statement = connection.createStatement();
			//executes the query 
			int row = statement.executeUpdate(sql);
			//the return value is the indication of success or failure of the query execution
			if (row > 0)
				System.out.println("Data was inserted!");

			connection.close();
			
		} catch (SQLException e) {
			e.getMessage();

		}
	}
	
	public static void insertJob(Job job) {
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			String sql = "INSERT INTO vehicle VALUES(" + job.jobID + ", " + job.clientID + ", " + job.levelOfRedundancy + ", " + job.jobDuration + ", " + job.payout + ", " + job.title + ", " + job.deadline + ", " + job.attachedFileName + ");";
			//establishes the connection session
			Statement statement = connection.createStatement();
			//executes the query 
			int row = statement.executeUpdate(sql);
			//the return value is the indication of success or failure of the query execution
			if (row > 0)
				System.out.println("Data was inserted!");

			connection.close();
			
		} catch (SQLException e) {
			e.getMessage();

		}
	}


}
