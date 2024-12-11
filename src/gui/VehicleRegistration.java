package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
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
        frame.setSize(770, 600); 
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        
        int xPosition = screenWidth - frame.getWidth();  
        int yPosition = (screenHeight - frame.getHeight()) / 2;  
        frame.setLocation(xPosition, yPosition);


        txtMake = new JTextField(20);
        txtModel = new JTextField(20);
        txtYear = new JTextField(20);
        txtColor = new JTextField(20);
        txtVIN = new JTextField(20);
        txtLicensePlate = new JTextField(20);
        txtResidency = new JTextField(20);

        JButton btnSubmit = new JButton("Register Vehicle");
        styleButton(btnSubmit);
        JButton btnReturn = new JButton("Go Back");
        styleButton(btnReturn);

        btnSubmit.addActionListener(e -> registerVehicle());
        btnReturn.addActionListener(e -> {
            new VehicleOwnerDashboard(vehicleOwner);
            frame.dispose();
        });

        // Layout setup
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(160, 208, 240)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Make: (Any Alphabets)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtMake, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Model: (Any Alphabets)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtModel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Year: (Numbers)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtYear, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Color: (Any Alphabets)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtColor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Vehicle ID: (Any Alphabets)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtVIN, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("License Plate: (Any Alphabets)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtLicensePlate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Residency: (Numbers, Hours)", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(txtResidency, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSubmit, gbc);

        gbc.gridy = 8;
        panel.add(btnReturn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(217, 217, 217)); 
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setFocusPainted(false);
    }

    private void registerVehicle() {
        String make = txtMake.getText();
        String model = txtModel.getText();
        String yearStr = txtYear.getText();
        String color = txtColor.getText();
        String vin = txtVIN.getText();
        String licensePlate = txtLicensePlate.getText();
        String residencyStr = txtResidency.getText();

        try {
            validateInputs(make, model, yearStr, color, vin, licensePlate, residencyStr);

            int year = Integer.parseInt(yearStr);
            double residency = Double.parseDouble(residencyStr);

            // Create the vehicle object
            Vehicle vehicle = new Vehicle(make, model, year, color, vin, licensePlate, residency);

            // Send vehicle data to server
            sendVehicleToServer(vehicle);

            // Return to the dashboard
            new VehicleOwnerDashboard(vehicleOwner);
            frame.dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendVehicleToServer(Vehicle vehicle) {
        try (Socket socket = new Socket("localhost", 54321);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {

            // Send the Vehicle object to the server
            outputStream.writeObject(vehicle);
            outputStream.flush();

            JOptionPane.showMessageDialog(frame, "Vehicle submitted successfully to the server!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to send vehicle data to the server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void validateInputs(String make, String model, String yearStr, String color, String vin, String licensePlate, String residencyStr) {
        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty() ||
                vin.isEmpty() || licensePlate.isEmpty() || residencyStr.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled out.");
        }

        try {
            Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year must be a valid number.");
        }

        try {
            Double.parseDouble(residencyStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Residency must be a valid number.");
        }
    }
}

