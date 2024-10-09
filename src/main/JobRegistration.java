package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate; 
import java.time.format.DateTimeFormatterBuilder; 


class JobListing {
    private String client;
    private String businessName;
    private String title;
    private double payout;
    private String estimatedTime;
    private String attachedFileName;
    private LocalDate deadline; 

    
    public JobListing(String client, String businessName, String title, double payout, String estimatedTime, String attachedFileName, LocalDate deadline) {
        this.client = client;
        this.businessName = businessName;
        this.title = title;
        this.payout = payout;
        this.estimatedTime = estimatedTime;
        this.attachedFileName = attachedFileName;
        this.deadline = deadline; 
    }

    
    public String getDetails() {
        return "Job Listing Details:\n" +
                "Name: " + client + "\n" +
                "Business Name: " + (businessName.isEmpty() ? "N/A" : businessName) + "\n" +
                "Title: " + title + "\n" +
                "Payout: $" + payout + "\n" +
                "Estimated Time: " + estimatedTime + "\n" +
                "Deadline: " + (deadline != null ? deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "None") + "\n" +
                "Attached File: " + (attachedFileName != null ? attachedFileName : "None");
    }

    
    public String toFileString() {
        return client + "," + (businessName.isEmpty() ? "N/A" : businessName) + "," + title + "," + payout + "," + estimatedTime + "," + (attachedFileName != null ? attachedFileName : "None") + "," + (deadline != null ? deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "None");
    }
}


public class JobRegistration {
    private JFrame frame;
    private JTextField txtClient, txtBusinessName, txtTitle, txtPayout, txtEstimatedTime;
    private String attachedFileName = null; 
    private JSpinner spinnerDeadline; 
    private Dash dashBoard;

    public JobRegistration() {
        frame = new JFrame("Job Registration");
        frame.setSize(500, 550); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        txtClient = new JTextField(20);
        txtBusinessName = new JTextField(20);
        txtTitle = new JTextField(20);
        txtPayout = new JTextField(20);
        txtEstimatedTime = new JTextField(20);

        
        spinnerDeadline = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDeadline, "yyyy-MM-dd");
        spinnerDeadline.setEditor(dateEditor);
        spinnerDeadline.setValue(new java.util.Date()); 

        JButton btnAttachFile = new JButton("Attach File");
        JLabel lblFileStatus = new JLabel("No file attached");
        JButton btnSubmit = new JButton("Submit Job");

        
        btnAttachFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                attachedFileName = fileChooser.getSelectedFile().getName();
                lblFileStatus.setText("Attached: " + attachedFileName);
            }
        });

        
        btnSubmit.addActionListener(e -> submitJob());

        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.WEST; 

        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtClient, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Business/Lister Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtBusinessName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtTitle, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Payout:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtPayout, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Estimated Time:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtEstimatedTime, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Deadline:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(spinnerDeadline, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(btnAttachFile, gbc);
        gbc.gridx = 1; gbc.gridy = 6; panel.add(lblFileStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; // Center the submit button
        panel.add(btnSubmit, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    
    private void submitJob() {
        String client = txtClient.getText();
        String businessName = txtBusinessName.getText();
        String title = txtTitle.getText();
        String payoutStr = txtPayout.getText();
        String estimatedTime = txtEstimatedTime.getText();
        LocalDate deadline = ((java.util.Date) spinnerDeadline.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(); // Convert spinner value to LocalDate

        
        if (client.isEmpty() || title.isEmpty() || payoutStr.isEmpty() || estimatedTime.isEmpty()) {
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

        
        JobListing job = new JobListing(client, businessName, title, payout, estimatedTime, attachedFileName, deadline);
        saveJobData(job);
        JOptionPane.showMessageDialog(frame, job.getDetails(), "Job Submitted", JOptionPane.INFORMATION_MESSAGE);

        
        clearFields();
        dashBoard = new Dash();
    }

    
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

    
    private void clearFields() {
        txtClient.setText("");
        txtBusinessName.setText("");
        txtTitle.setText("");
        txtPayout.setText("");
        txtEstimatedTime.setText("");
        attachedFileName = null;
        spinnerDeadline.setValue(new java.util.Date()); 
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobRegistration::new);
    }
}
