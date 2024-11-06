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

        btnRegister.addActionListener(e -> registerVehicle());
        btnReturn.addActionListener(e -> new UserRegistration());
        btnReturn.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        panel.add(new JLabel("Make: (Any Alphabets)"));
        panel.add(txtMake);
        panel.add(new JLabel("Model: (Any Alphabets)"));
        panel.add(txtModel);
        panel.add(new JLabel("Year: (Numbers)"));
        panel.add(txtYear);
        panel.add(new JLabel("Color: (Any Alphabets)"));
        panel.add(txtColor);
        panel.add(new JLabel("VIN: (Any Alphabets)"));
        panel.add(txtVIN);
        panel.add(new JLabel("License Plate: (Any Alphabets)"));
        panel.add(txtLicensePlate);
        panel.add(new JLabel("Time Available: (Numbers, Hours)"));
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

        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty() ||
            vin.isEmpty() || licensePlate.isEmpty() || residencyStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year;
        double residency;
        try {
            year = Integer.parseInt(yearStr);
            residency = Double.parseDouble(residencyStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Year and Residency must be valid numbers.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the vehicle and add it to the vehicle owner
        Vehicle vehicle = new Vehicle(make, model, year, color, vin, licensePlate, residency);
        vehicleOwner.ownVehicle(vehicle); // Register the vehicle under this owner
        saveVehicleData(vehicle); // Save vehicle data to the file

        JOptionPane.showMessageDialog(frame, vehicle.getDetails(), "Vehicle Registered",
                JOptionPane.INFORMATION_MESSAGE);

        // Now, open the VehicleOwnerDashboard after successful registration
        new VehicleOwnerDashboard(vehicleOwner);
        frame.dispose(); // Close the Vehicle Registration window
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
