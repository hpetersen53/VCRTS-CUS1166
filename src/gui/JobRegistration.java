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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // New text field to allow user to input client ID
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

        btnAttachFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                attachedFileName = fileChooser.getSelectedFile().getName();
                lblFileStatus.setText("Attached: " + attachedFileName);
            }
        });

        btnSubmit.addActionListener(e -> submitJob());
        btnReturn.addActionListener(e ->  new UserRegistration());
        btnReturn.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        // Layout setup
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtClientId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Payout:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPayout, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Estimated Time:"), gbc);
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
        
        gbc.gridx=0;
        gbc.gridy=7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnReturn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

	private void submitJob() {
		// Get the Client ID from the input field
		String clientIdStr = txtClientId.getText();
		int clientId;

		try {
			clientId = Integer.parseInt(clientIdStr);
			client.setID(clientId); // Set the client ID for the Client object
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Client ID must be a valid integer.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Other fields
		String title = txtTitle.getText();
		String payoutStr = txtPayout.getText();
		String estimatedTime = txtEstimatedTime.getText();
		LocalDate deadline = ((java.util.Date) spinnerDeadline.getValue()).toInstant()
				.atZone(java.time.ZoneId.systemDefault()).toLocalDate();

		// Validation
		if (title.isEmpty() || payoutStr.isEmpty() || estimatedTime.isEmpty()) {
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

		// Create and submit job
		Job job = new Job(client.getID(), 0, 0, payout, title, deadline, attachedFileName);
		client.submitJob(job);
		saveJobData(job);
		JOptionPane.showMessageDialog(frame, job.getDetails(), "Job Submitted", JOptionPane.INFORMATION_MESSAGE);

		clearFields();
	}

	private void saveJobData(Job job) {
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
		txtClientId.setText("");
		txtTitle.setText("");
		txtPayout.setText("");
		txtEstimatedTime.setText("");
		attachedFileName = null;
		spinnerDeadline.setValue(new java.util.Date());
	}
}