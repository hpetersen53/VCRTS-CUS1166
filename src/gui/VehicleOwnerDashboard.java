package gui;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.VCController;
import main.VehicleOwner;

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

        
        frame = new JFrame("Vehicle Owner Dashboard");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Make");
        tableModel.addColumn("Model");
        tableModel.addColumn("Year");
        tableModel.addColumn("License Plate");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        
        loadVehicles();

        
        frame.add(new JLabel(vehicleOwner.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);  
        frame.add(scrollPane, BorderLayout.CENTER);
        
        //JButton btnCalc = new JButton("Calculate Completion Time");
        //btnCalc.addActionListener(e -> cloudController.calculateCompletion());
        //frame.add(btnCalc);
        

        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow(); 
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
                
                String[] data = line.split(",");
                if (data.length < 5) continue; 

                try {
                    
                    int ownerId = Integer.parseInt(data[0].trim()); 
                    if (ownerId == vehicleOwner.getID()) { 
                        String make = data[1].trim(); 
                        String model = data[2].trim(); 
                        int year = Integer.parseInt(data[3].trim()); 
                        String licensePlate = data[4].trim(); 

                        
                        tableModel.addRow(new Object[]{ownerId, make, model, year, licensePlate});
                    }
                } catch (NumberFormatException e) {
                    
                    System.err.println("Error: Invalid number format in vehicle data: " + line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading vehicle data", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
