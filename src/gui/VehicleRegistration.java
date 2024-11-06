package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import main.*;

public class VehicleRegistration {
	private JFrame frame;
	private JTextField txtMake, txtModel, txtYear, txtColor, txtVIN, txtLicensePlate, txtResidency;
	private VehicleOwner vehicleOwner;

	public VehicleRegistration(VehicleOwner vehicleOwner) {
		if (vehicleOwner == null) {
			throw new IllegalArgumentException("VehicleOwner cannot be null.");
		}
		this.vehicleOwner = vehicleOwner;

		frame = new JFrame("Vehicle Registration");
		frame.setSize(400, 400);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(9, 2));

		txtMake = new JTextField(20);
		txtModel = new JTextField(20);
		txtYear = new JTextField(20);
		txtColor = new JTextField(20);
		txtVIN = new JTextField(20);
		txtLicensePlate = new JTextField(20);
		txtResidency = new JTextField(20);

		JButton btnRegister = new JButton("Register");
		JButton btnReturn = new JButton("Go Back");

		// Action listener for the register button
		btnRegister.addActionListener(e -> registerVehicle());
		btnReturn.addActionListener(e -> new UserRegistration());
		btnReturn.addActionListener(e ->  frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

		// Add labels and fields to panel
		panel.add(new JLabel("Make:"));
		panel.add(txtMake);
		panel.add(new JLabel("Model:"));
		panel.add(txtModel);
		panel.add(new JLabel("Year:"));
		panel.add(txtYear);
		panel.add(new JLabel("Color:"));
		panel.add(txtColor);
		panel.add(new JLabel("VIN:"));
		panel.add(txtVIN);
		panel.add(new JLabel("License Plate:"));
		panel.add(txtLicensePlate);
		panel.add(new JLabel("Time Available:"));
		panel.add(txtResidency);
		panel.add(new JLabel("")); // Spacer
		panel.add(btnRegister);
		panel.add(btnReturn);

		frame.add(panel);
		frame.setVisible(true);
	}

	private void registerVehicle() {
        String make = txtMake.getText();
        String model = txtModel.getText();
        String yearStr = txtYear.getText();
        String color = txtColor.getText();
        String vin = txtVIN.getText();
        String licensePlate = txtLicensePlate.getText();
        String residencyStr = txtResidency.getText();
        double residency = Double.parseDouble(residencyStr);


        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty() ||
            vin.isEmpty() || licensePlate.isEmpty() || residencyStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Year must be a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        Vehicle vehicle = new Vehicle(make, model, year, color, vin, licensePlate, residency);
        vehicleOwner.ownVehicle(vehicle); 
        saveVehicleData(vehicle);

        JOptionPane.showMessageDialog(frame, vehicle.getDetails(), "Vehicle Registered",
                JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

	private static void saveVehicleData(Vehicle vehicle) {
		String fileName = "VehicleRegistrations.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = now.format(formatter);
			writer.write(timestamp + "," + vehicle.getDetails());
			writer.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}