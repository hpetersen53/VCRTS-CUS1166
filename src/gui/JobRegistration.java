package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
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
        frame.setSize(800, 600); 
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
        styleButton(btnAttachFile);
        JLabel lblFileStatus = new JLabel("No file attached");
        JButton btnSubmit = new JButton("Submit Job");
        styleButton(btnSubmit);
        JButton btnReturn = new JButton("Go Back");
        styleButton(btnReturn);

        btnAttachFile.addActionListener(e -> attachFile(lblFileStatus));
        btnSubmit.addActionListener(e -> submitJob());
        btnReturn.addActionListener(e -> {
            new ClientDashboard(client);
            frame.dispose();
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(160, 208, 240)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Client ID: (Numbers)"), gbc);
        gbc.gridx = 1;
        panel.add(txtClientId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Job Title: (Any Alphabets)"), gbc);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Payout: (Numbers)"), gbc);
        gbc.gridx = 1;
        panel.add(txtPayout, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Estimated Time: (Numbers, Hours)"), gbc);
        gbc.gridx = 1;
        panel.add(txtEstimatedTime, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Deadline:"), gbc);
        gbc.gridx = 1;
        panel.add(spinnerDeadline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(btnAttachFile, gbc);
        gbc.gridx = 1;
        panel.add(lblFileStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSubmit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnReturn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(217, 217, 217)); 
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setFocusPainted(false);
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


            sendJobToServer(job);

            new ClientDashboard(client);
            frame.dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendJobToServer(Job job) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {

            // Send the Job object
            outputStream.writeObject(job);
            outputStream.flush();
            JOptionPane.showMessageDialog(frame, "Job submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to send job data to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
}