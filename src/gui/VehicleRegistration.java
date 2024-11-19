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
    private VCController cloudController;

    public VehicleRegistration(VehicleOwner vehicleOwner) {
        if (vehicleOwner == null) {
            throw new IllegalArgumentException("VehicleOwner cannot be null.");
        }
        this.vehicleOwner = vehicleOwner;

        frame = new JFrame("Vehicle Registration");
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Make: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtMake, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Model: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtModel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Year: (Numbers)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtYear, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(new JLabel("Color: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtColor, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        panel.add(new JLabel("VIN: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtVIN, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        panel.add(new JLabel("License Plate: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtLicensePlate, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        panel.add(new JLabel("Time Available: (Numbers, Hours)"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtResidency, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        panel.add(btnRegister, gbc);
        
        panel.add(new JLabel(""), gbc); // Spacer
        
        gbc.gridy = 8;
        panel.add(btnReturn, gbc);
        

        frame.add(panel);
        frame.setLocationRelativeTo(null);
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
        //cloudController.recruitVehicle(vehicle);
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
            writer.write(timestamp + "," + vehicle.getDetails() + "\n");
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
