package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import main.VCController;
import main.Job;
import java.util.ArrayList;

public class ControllerDashboard {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private VCController controller;

    public ControllerDashboard(VCController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null.");
        }
        this.controller = controller;

        frame = new JFrame("Controller Dashboard");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize table model and add columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Client ID");
        tableModel.addColumn("Job Duration");

        // Create table and link it to the table model
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        loadJobs();

        frame.add(new JLabel("Controller Dashboard"), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // "Go Back" button to return to main GUI
        JButton btnReturn = new JButton("Go Back");
        btnReturn.addActionListener(e -> {
            new GUIWindow();
            frame.dispose();
        });

        // "Completion Time Check" button to display job completion times
        JButton btnCompletionCheck = new JButton("Completion Time Check");
        btnCompletionCheck.addActionListener(e -> showCompletionTimes());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnReturn);
        buttonPanel.add(btnCompletionCheck);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadJobs() {
        String fileName = "JobListings.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int clientId = 0;
            int jobDuration = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Client ID:")) {
                    clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Job Duration:")) {
                    jobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.isEmpty()) {
                    // Add each job to the table model
                    tableModel.addRow(new Object[]{clientId, jobDuration});

                    // Reset values for the next job entry
                    clientId = 0;
                    jobDuration = 0;
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    // Method to display client IDs and job durations in a small popup
 // Method to display client IDs and cumulative job durations in a small popup
    private void showCompletionTimes() {
        ArrayList<Integer> clientIDs = new ArrayList<>();
        ArrayList<Integer> cumulativeDurations = new ArrayList<>();
        int cumulativeDuration = 0;

        String fileName = "JobListings.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int clientId = 0;
            int jobDuration = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Client ID:")) {
                    clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Job Duration:")) {
                    jobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    cumulativeDuration += jobDuration; // Increment cumulative duration
                    clientIDs.add(clientId);
                    cumulativeDurations.add(cumulativeDuration);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }

        // Build message for popup
        StringBuilder message = new StringBuilder("ID: ");
        
        // Format Client IDs
        for (int i = 0; i < clientIDs.size(); i++) {
            message.append(clientIDs.get(i));
            if (i < clientIDs.size() - 1) {
                message.append(", ");
            }
        }

        message.append("\nDuration: ");
        
        // Format Cumulative Durations
        for (int i = 0; i < cumulativeDurations.size(); i++) {
            message.append(cumulativeDurations.get(i));
            if (i < cumulativeDurations.size() - 1) {
                message.append(", ");
            }
        }

        // Display the popup
        JOptionPane.showMessageDialog(frame, message.toString(), "Completion Times", JOptionPane.PLAIN_MESSAGE);
    }
}

