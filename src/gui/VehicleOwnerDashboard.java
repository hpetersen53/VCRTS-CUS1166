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
    private VCController cloudController;

    public VehicleOwnerDashboard(VehicleOwner vehicleOwner) {
        if (vehicleOwner == null) {
            throw new IllegalArgumentException("VehicleOwner cannot be null.");
        }
        this.vehicleOwner = vehicleOwner;

        // Set up the frame
        frame = new JFrame("Vehicle Owner Dashboard");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and add columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Make");
        tableModel.addColumn("Model");
        tableModel.addColumn("Year");
        tableModel.addColumn("License Plate");

        // Create table and add to a scroll pane
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load vehicles from file
        loadVehicles(vehicleOwner.getID());

        // Add components to the frame
        frame.add(new JLabel("Vehicles Owned by: " + vehicleOwner.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        
      
        

        
        JButton btnRegisterVehicle = new JButton("Register New Vehicle");
		btnRegisterVehicle.addActionListener(e -> {
			new VehicleRegistration(vehicleOwner);
			frame.dispose();
		});

        // "Go Back" button to return to the main GUI
        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow(); 
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
    private void loadVehicles(int id) {
        String fileName = "VehicleRegistrations.txt";
        
        String line;
        String make = null, model = null, year = null, licensePlate = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
           

            while ((line = reader.readLine()) != null) {
                // Trim whitespace and skip if the line is empty
                line = line.trim();
//                if (line.isEmpty()) continue;

                // Parse fields based on labels in the text file
                if (line.contains("Make:")) {
                    make = line.substring(line.indexOf("Make:") + 5).trim();
                } else if (line.contains("Model:")) {
                    model = line.substring(line.indexOf("Model:") + 6).trim();
                } else if (line.contains("Year:")) {
                    year = line.substring(line.indexOf("Year:") + 5).trim();
                } else if (line.contains("License Plate:")) {
                    licensePlate = line.substring(line.indexOf("License Plate:") + 13).trim();
                }

                // When all fields are parsed, add a row to the table and reset variables
                if (make != null && model != null && year != null && licensePlate != null) {
                    try {
                        int yearInt = Integer.parseInt(year);
                        // Check if vehicleOwner ID matches the vehicle's owner ID before adding
                        if (vehicleOwner.getID() == 1) { // Adjust condition to use vehicleOwner's actual ID method
                            tableModel.addRow(new Object[]{vehicleOwner.getID(), make, model, yearInt, licensePlate});
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Invalid year format for vehicle data.");
                    }
                    // Reset variables for the next vehicle entry
                    make = model = year = licensePlate = null;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading vehicle data", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
