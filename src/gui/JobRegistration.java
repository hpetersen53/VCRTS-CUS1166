package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import main.*;

public class JobRegistration {
    private JFrame frame;
    private JTextField txtClientId, txtTitle, txtPayout, txtEstimatedTime;
    private String attachedFileName = null;
    private JSpinner spinnerDeadline;
    private Client client;

    public JobRegistration(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        this.client = client;

        frame = new JFrame("Job Registration");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        txtClientId = new JTextField(20);
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
        JButton btnReturn = new JButton("Go Back");

        btnAttachFile.addActionListener(e -> attachFile(lblFileStatus));
        btnSubmit.addActionListener(e -> submitJob());
        btnReturn.addActionListener(e -> {
            new UserRegistration();
            frame.dispose();
        });

        // Layout setup
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Client ID: (Numbers)"), gbc);
        gbc.gridx = 1;
        panel.add(txtClientId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Job Title: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Payout: (Numbers)"), gbc);
        gbc.gridx = 1;
        panel.add(txtPayout, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Estimated Time: (Numbers, Hours)"), gbc);
        gbc.gridx = 1;
        panel.add(txtEstimatedTime, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Deadline:"), gbc);
        gbc.gridx = 1;
        panel.add(spinnerDeadline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(btnAttachFile, gbc);
        gbc.gridx = 1;
        panel.add(lblFileStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSubmit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnReturn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void attachFile(JLabel lblFileStatus) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            attachedFileName = fileChooser.getSelectedFile().getName();
            lblFileStatus.setText("Attached: " + attachedFileName);
            JOptionPane.showMessageDialog(frame, "File attached successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void submitJob() {
        String clientIdStr = txtClientId.getText();
        String title = txtTitle.getText();
        String payoutStr = txtPayout.getText();
        String estimatedTimeStr = txtEstimatedTime.getText();
        LocalDate deadline = ((java.util.Date) spinnerDeadline.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        try {
            int clientId = validateClientId(clientIdStr);
            double payout = validatePayout(payoutStr);
            int estimatedTime = validateEstimatedTime(estimatedTimeStr);
            validateTitle(title);

            client.setID(clientId);
            Job job = new Job(client.getID(), 0, estimatedTime, payout, title, deadline, attachedFileName);
            client.submitJob(job);
            saveJobData(job);

            JOptionPane.showMessageDialog(frame, "Job submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new ClientDashboard(client);
            frame.dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int validateClientId(String clientIdStr) {
        if (clientIdStr.isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be empty.");
        }
        try {
            return Integer.parseInt(clientIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client ID must be a valid integer.");
        }
    }

    private double validatePayout(String payoutStr) {
        if (payoutStr.isEmpty()) {
            throw new IllegalArgumentException("Payout cannot be empty.");
        }
        try {
            return Double.parseDouble(payoutStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Payout must be a valid number.");
        }
    }

    private int validateEstimatedTime(String estimatedTimeStr) {
        if (estimatedTimeStr.isEmpty()) {
            throw new IllegalArgumentException("Estimated Time cannot be empty.");
        }
        try {
            return Integer.parseInt(estimatedTimeStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Estimated Time must be a valid integer.");
        }
    }

    private void validateTitle(String title) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Job Title cannot be empty.");
        }
    }

    private void saveJobData(Job job) {
        String fileName = "JobListings.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            writer.write("Timestamp: " + timestamp + "\n" + job.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
