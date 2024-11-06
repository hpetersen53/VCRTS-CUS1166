package gui;
import javax.swing.*;
import java.awt.*;
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
        panel.setLayout(new GridLayout(8, 2));

        txtMake = new JTextField(20);
        txtModel = new JTextField(20);
        txtYear = new JTextField(20);
        txtColor = new JTextField(20);
        txtVIN = new JTextField(20);
        txtLicensePlate = new JTextField(20);
        txtResidency = new JTextField(20);

        JButton btnRegister = new JButton("Register");

        // Action listener for the register button
        btnRegister.addActionListener(e -> registerVehicle());

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
        String residency = txtResidency.getText();

        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty() ||
            vin.isEmpty() || licensePlate.isEmpty() || residency.isEmpty()) {
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

        // Create a new vehicle object and save the data to the vehicle owner
        Vehicle vehicle = new Vehicle(make, model, year, color, vin, licensePlate, residency);
        vehicleOwner.ownVehicle(vehicle);  // Register vehicle with the owner
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
            writer.write(timestamp + "," + vehicle.toFileString());
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
