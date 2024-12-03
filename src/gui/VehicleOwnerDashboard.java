package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.VehicleOwner;
import main.VCController;

public class VehicleOwnerDashboard {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private VehicleOwner vehicleOwner;

    public VehicleOwnerDashboard(VehicleOwner vehicleOwner) {
        if (vehicleOwner == null) {
            throw new IllegalArgumentException("VehicleOwner cannot be null.");
        }
        this.vehicleOwner = vehicleOwner;

        // Set up the frame
        frame = new JFrame("Vehicle Owner Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and add columns
        tableModel = new DefaultTableModel(new Object[]{
                "Vehicle ID", "Make", "Model", "Color", "Year", "License Plate", "Residency"
        }, 0);

        // Create table and add to a scroll pane
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load vehicles from file
        loadVehicles(vehicleOwner.getID());

        // Add components to the frame
        frame.add(new JLabel("Vehicles Owned by: " + vehicleOwner.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // "Register New Vehicle" button
        JButton btnRegisterVehicle = new JButton("Register New Vehicle");
        btnRegisterVehicle.addActionListener(e -> {
            new VehicleRegistration(vehicleOwner);
            frame.dispose();
        });

        // "Go Back" button
        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow(); // Navigate to the main GUI
            frame.dispose();
        });

        // Add buttons to a panel and set to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRegisterVehicle);
        buttonPanel.add(btnReturn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    // Method to load vehicles from "VehicleRegistrations.txt" and display in the table
    private void loadVehicles(int ownerId) {
        String fileName = "VehicleRegistrations.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            String vehicleId = null, make = null, model = null, licensePlate = null, color = null;
            int year = 0;
            double residency = 0.0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Parse fields based on labels in the text file
                if (line.startsWith("VIN:")) {
                    vehicleId = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Make:")) {
                    make = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Model:")) {
                    model = line.substring(line.indexOf(":") + 1).trim();
                }
                else if (line.startsWith("Time Available:")) {
                    residency = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
            }else if (line.startsWith("Color:")) {
                    color = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Year:")) {
                    try {
                        year = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    } catch (NumberFormatException e) {
                        year = 0; // Default year if parsing fails
                    }
                } else if (line.startsWith("License Plate:")) {
                    licensePlate = line.substring(line.indexOf(":") + 1).trim();


                }

                // Debugging log for residency
                //System.out.println("Residency parsed: " + residency);

                // When all fields are parsed, add a row to the table and reset variables
                if (vehicleId != null && make != null && model != null && color != null && licensePlate != null && year != 0 && residency !=0) {
                    Object[] row = new Object[]{vehicleId, make, model, color, year, licensePlate, residency};
                    //System.out.println("Adding row to table: " + java.util.Arrays.toString(row)); // Debug row data
                    tableModel.addRow(row);

                    // Reset variables for the next entry
                    vehicleId = make = model = color = licensePlate = null;
                    year = 0;
                    residency = 0.0;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading vehicle data", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
