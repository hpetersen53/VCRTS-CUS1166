package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

            while ((line = reader.readLine()) != null) {
                // Split the line by commas
                String[] data = line.split(",");
                if (data.length < 5) continue; // Ensure data has all fields

                try {
                    // Parse jobId and clientId (assuming they are integers)
                    int jobId = Integer.parseInt(data[0].trim()); // job ID should be the first field
                    int clientId = Integer.parseInt(data[1].trim()); // client ID should be the second field

                    // Check if the job is posted by the current client
                    if (clientId == client.getID()) {
                        String title = data[2].trim(); // job title
                        double payout = Double.parseDouble(data[3].trim()); // job payout
                        LocalDate deadline = LocalDate.parse(data[4].trim()); // job deadline (date format must be yyyy-MM-dd)
                        String attachedFileName = data.length > 5 && !data[5].trim().isEmpty() ? data[5].trim() : "None"; // optional attached file

                        // Add job data to the table
                        tableModel.addRow(new Object[]{jobId, title, payout, deadline, attachedFileName});
                    }
                } catch (NumberFormatException e) {
                    // Handle any number format issues
                    System.out.println("Error parsing job data: " + line);
                } catch (DateTimeParseException e) {
                    // Handle any date format issues (ensure correct date format)
                    System.out.println("Error parsing deadline for job: " + line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
