package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import main.Client;
import main.Job;

public class ClientDashboard {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Client client;
    
    public ClientDashboard(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        this.client = client;

        frame = new JFrame("Client Dashboard");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table for displaying job information
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Job ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Payout");
        tableModel.addColumn("Deadline");
        tableModel.addColumn("File Attached");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load job data from file
        loadJobs();

        // Add components to frame
        frame.add(new JLabel("Jobs Posted by: " + client.getDetails()), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow(); // Return to the main page or any other page you'd like to redirect to
            frame.dispose();
        });
        frame.add(btnReturn, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadJobs() {
        String fileName = "JobListings.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            ArrayList<Job> clientJobs = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // Parse job data from the file
                String[] data = line.split(",");
                if (data.length < 5) continue; // Ensure data has all fields
                
                int clientId = Integer.parseInt(data[0]);
                if (clientId == client.getID()) { // Filter jobs by client ID
                    String title = data[1];
                    double payout = Double.parseDouble(data[2]);
                    LocalDate deadline = LocalDate.parse(data[3]);
                    String attachedFileName = data[4].isEmpty() ? "None" : data[4];

                    // Add to table
                    tableModel.addRow(new Object[]{clientId, title, payout, deadline, attachedFileName});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
