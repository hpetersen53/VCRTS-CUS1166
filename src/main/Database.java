package main;

import java.sql.*;

public class Database {
	
	static Connection connection = null;
	//this part is the address and name of your database server: jdbc:mysql://localhost:3306/VC3
	//this part of the string is for time adjustment: ?useTimezone=true&serverTimezone=UTC
	static String url = "jdbc:mysql://127.0.0.1:3306/vcrts_db?useTimezone=true&serverTimezone=UTC";
	static String username = "root";
	static String password = "99044";
	

	
	public static void insertVehicle(Vehicle vehicle) {
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			
			String attributes = vehicle.getAttributes();
			String sql = "INSERT INTO vehicle VALUES(" + attributes + ");";
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
			System.out.println("Could not submit to database.");
			System.out.println(vehicle.getAttributes());

		}
	}
	

	
	public static void insertJob(Job job) {
		try {
			//declares a connection to your database
			connection = DriverManager.getConnection(url, username, password);
			//creates an insert query
			String attributes = job.getAttributes();
			String sql = "INSERT INTO job VALUES(" + attributes + ");";
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
			System.out.println("Could not submit to database.");
			System.out.println(job.getAttributes());

		}
	}


}
