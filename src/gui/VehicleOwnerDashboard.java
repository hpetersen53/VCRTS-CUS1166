package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import main.VehicleOwner;

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
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table for displaying vehicle information
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Make");
        tableModel.addColumn("Model");
        tableModel.addColumn("Year");
        tableModel.addColumn("License Plate");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load vehicle data from file
        loadVehicles();

        // Add components to frame
        frame.add(new JLabel(vehicleOwner.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);  // Display vehicle owner details
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow(); // Return to the main page or any other page you'd like to redirect to
            frame.dispose();
        });
        frame.add(btnReturn, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadVehicles() {
        String fileName = "VehicleRegistrations.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Parse vehicle data from the file
                String[] data = line.split(",");
                if (data.length < 5) continue; // Ensure data has all fields

                try {
                    // Assuming the data format is: ownerId, make, model, year, licensePlate
                    int ownerId = Integer.parseInt(data[0].trim()); // Parse ownerId
                    if (ownerId == vehicleOwner.getID()) { // Filter vehicles by owner ID
                        String make = data[1].trim(); // Make of the vehicle
                        String model = data[2].trim(); // Model of the vehicle
                        int year = Integer.parseInt(data[3].trim()); // Parse year
                        String licensePlate = data[4].trim(); // License plate of the vehicle

                        // Add to table
                        tableModel.addRow(new Object[]{ownerId, make, model, year, licensePlate});
                    }
                } catch (NumberFormatException e) {
                    // Catch invalid number format in case of incorrect data like non-numeric values for ID or year
                    System.err.println("Error: Invalid number format in vehicle data: " + line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading vehicle data", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
