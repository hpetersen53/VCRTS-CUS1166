package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// stores vehicle information
class Vehicle {
	private String make;
	private String model;
	private int year;
	private String color;
	private String vin;
	private String licensePlate;
	private String residency;
	

	// initialize a new vehicle object
	public Vehicle(String make, String model, int year, String color, String vin, String licensePlate, String residency) {
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
				+ "Color: " + color + "\n" + "VIN: " + vin + "\n" + "License Plate: " + licensePlate + "Time Available: " + residency;
	}

	// format to write vehicle details to a file.
	public String toFileString() {
		return make + "," + model + "," + year + "," + color + "," + vin + "," + licensePlate + "," + residency;
	}
}

// user interface and registration
public class VehicleRegistration {
	private JFrame frame;
	private Dash dashBoard;

	public VehicleRegistration() {
		frame = new JFrame("Vehicle Registration");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8, 2));

		JLabel lblMake = new JLabel("Make:");
		JTextField txtMake = new JTextField(20);

		JLabel lblModel = new JLabel("Model:");
		JTextField txtModel = new JTextField(20);

		JLabel lblYear = new JLabel("Year:");
		JTextField txtYear = new JTextField(20);

		JLabel lblColor = new JLabel("Color:");
		JTextField txtColor = new JTextField(20);

		JLabel lblVIN = new JLabel("VIN:");
		JTextField txtVIN = new JTextField(20);

		JLabel lblLicensePlate = new JLabel("License Plate:");
		JTextField txtLicensePlate = new JTextField(20);
		
		JLabel lblResidency = new JLabel("Time Available:");
		JTextField txtResidency = new JTextField(20);

		JButton btnRegister = new JButton("Register");

		// action listener to register button click
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String make = txtMake.getText();
				String model = txtModel.getText();
				String yearStr = txtYear.getText();
				String color = txtColor.getText();
				String vin = txtVIN.getText();
				String licensePlate = txtLicensePlate.getText();
				String residency = txtResidency.getText();

				// data validation
				if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty() || vin.isEmpty()
						|| licensePlate.isEmpty() || residency.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int year;
				try {
					year = Integer.parseInt(yearStr); // converts year to integer
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Year must be a valid number.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// creates a new vehicle object and saves the data to a file
				Vehicle vehicle = new Vehicle(make, model, year, color, vin, licensePlate, residency);
				saveVehicleData(vehicle);
				JOptionPane.showMessageDialog(frame, vehicle.getDetails(), "Vehicle Registered",
						JOptionPane.INFORMATION_MESSAGE);
						frame.dispose();

				txtMake.setText("");
				txtModel.setText("");
				txtYear.setText("");
				txtColor.setText("");
				txtVIN.setText("");
				txtLicensePlate.setText("");
				txtResidency.setText("");
				
				dashBoard = new Dash();
			}
		});
		// adds labels and text fields to the panel
		panel.add(lblMake);
		panel.add(txtMake);
		panel.add(lblModel);
		panel.add(txtModel);
		panel.add(lblYear);
		panel.add(txtYear);
		panel.add(lblColor);
		panel.add(txtColor);
		panel.add(lblVIN);
		panel.add(txtVIN);
		panel.add(lblLicensePlate);
		panel.add(txtLicensePlate);
		panel.add(lblResidency);
		panel.add(txtResidency);
		panel.add(new JLabel(""));
		panel.add(btnRegister);

		frame.add(panel);
		frame.setVisible(true);
	}

// method to save vehicle data to a file
	private static void saveVehicleData(Vehicle vehicle) {
		String fileName = "VehicleRegistrations.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = now.format(formatter);

			// writes timestamp and vehicle details to the file.
			writer.write(timestamp + "," + vehicle.toFileString());
			writer.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
