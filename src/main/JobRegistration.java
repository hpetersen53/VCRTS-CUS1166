package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Stores job listing information
class JobListing {
    private String name;
    private String businessName;
    private String title;
    private double payout;
    private String estimatedTime;
    private String attachedFileName;

    // Initializes a new job listing object
    public JobListing(String name, String businessName, String title, double payout, String estimatedTime, String attachedFileName) {
        this.name = name;
        this.businessName = businessName;
        this.title = title;
        this.payout = payout;
        this.estimatedTime = estimatedTime;
        this.attachedFileName = attachedFileName;
    }

    // Gets job listing details
    public String getDetails() {
        return "Job Listing Details:\n" +
                "Name: " + name + "\n" +
                "Business Name: " + (businessName.isEmpty() ? "N/A" : businessName) + "\n" +
                "Title: " + title + "\n" +
                "Payout: $" + payout + "\n" +
                "Estimated Time: " + estimatedTime + "\n" +
                "Attached File: " + (attachedFileName != null ? attachedFileName : "None");
    }

    // Formats to write job details to a file
    public String toFileString() {
        return name + "," + (businessName.isEmpty() ? "N/A" : businessName) + "," + title + "," + payout + "," + estimatedTime + "," + (attachedFileName != null ? attachedFileName : "None");
    }
}

// User registration and job listing system
public class JobRegistration {
    private JFrame frame;
    private JTextField txtName, txtBusinessName, txtTitle, txtPayout, txtEstimatedTime;
    private String attachedFileName = null; // Variable to store the selected file name

    public JobRegistration() {
        frame = new JFrame("Job Registration");
        frame.setSize(500, 500); // Increased size for a bigger panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize form components with larger size
        txtName = new JTextField(20);
        txtBusinessName = new JTextField(20);
        txtTitle = new JTextField(20);
        txtPayout = new JTextField(20);
        txtEstimatedTime = new JTextField(20);

        JButton btnAttachFile = new JButton("Attach File");
        JLabel lblFileStatus = new JLabel("No file attached");
        JButton btnSubmit = new JButton("Submit Job");

        // File chooser action
        btnAttachFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                attachedFileName = fileChooser.getSelectedFile().getName();
                lblFileStatus.setText("Attached: " + attachedFileName);
            }
        });

        // Submit button action
        btnSubmit.addActionListener(e -> submitJob());

        // Arrange components in the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing around components
        gbc.anchor = GridBagConstraints.WEST; // Align components to the west

        // Add components to the panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Your Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Business Name (if applicable):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtBusinessName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtTitle, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Payout:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtPayout, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Estimated Time:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtEstimatedTime, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(btnAttachFile, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(lblFileStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; // Center the submit button
        panel.add(btnSubmit, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    // Submit job listing
    private void submitJob() {
        String name = txtName.getText();
        String businessName = txtBusinessName.getText();
        String title = txtTitle.getText();
        String payoutStr = txtPayout.getText();
        String estimatedTime = txtEstimatedTime.getText();

        // Validate input
        if (name.isEmpty() || title.isEmpty() || payoutStr.isEmpty() || estimatedTime.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double payout;
        try {
            payout = Double.parseDouble(payoutStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Payout must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create job listing and save
        JobListing job = new JobListing(name, businessName, title, payout, estimatedTime, attachedFileName);
        saveJobData(job);
        JOptionPane.showMessageDialog(frame, job.getDetails(), "Job Submitted", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        clearFields();
    }

    // Save job listing data to a file
    private void saveJobData(JobListing job) {
        String fileName = "JobListings.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.write(timestamp + "," + job.toFileString());
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Clear input fields
    private void clearFields() {
        txtName.setText("");
        txtBusinessName.setText("");
        txtTitle.setText("");
        txtPayout.setText("");
        txtEstimatedTime.setText("");
        attachedFileName = null;
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobRegistration::new);
    }
}
