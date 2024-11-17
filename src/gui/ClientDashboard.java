package gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;  // <-- Add this import
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.Client;
import main.VCController;

public class ClientDashboard {
	private JFrame frame;
	private JTable table;
	private DefaultTableModel tableModel;
	private Client client;
	private VCController cloudController;

	public ClientDashboard(Client client) {
		if (client == null) {
			throw new IllegalArgumentException("Client cannot be null.");
		}
		this.client = client;

		frame = new JFrame("Client Dashboard");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Initialize table model and add columns
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Client ID");
		tableModel.addColumn("Job Duration");
		tableModel.addColumn("Title");
		tableModel.addColumn("Payout");
		tableModel.addColumn("Deadline");
		tableModel.addColumn("File Attached");

		// Create table and link it to the table model
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);

		loadJobs(client.getID());

		frame.add(new JLabel("Jobs Posted by: " + client.getDetails()), BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);

		JButton btnBackToJobRegistration = new JButton("Register a job");
		btnBackToJobRegistration.addActionListener(e -> {
			new JobRegistration(client);
			frame.dispose();
		});

		JButton btnReturn = new JButton("Go Back");
		btnReturn.addActionListener(e -> {
			new GUIWindow();
			frame.dispose();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnBackToJobRegistration);
		buttonPanel.add(btnReturn);

		frame.add(buttonPanel, BorderLayout.SOUTH);

		// Set frame visibility
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private void loadJobs(int id) {
		String fileName = "JobListings.txt";
		
		String line;
		int clientId = 0;
		String title = "";
		int JobDuration = 0;
		double payout = 0.0;
		LocalDate deadline = null;
		String attachedFileName = "None";

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (line.startsWith("Client ID:")) {
					clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
				} else if (line.startsWith("Job Duration:")) {
					JobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
				} else if (line.startsWith("Title:")) {
					title = line.substring(line.indexOf(":") + 1).trim();
				} else if (line.startsWith("Payout:")) {
					payout = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
				} else if (line.startsWith("Deadline:")) {
					deadline = LocalDate.parse(line.substring(line.indexOf(":") + 1).trim());
				} else if (line.startsWith("FileName:")) {
					attachedFileName = line.substring(line.indexOf(":") + 1).trim();
				} else if (line.isEmpty()) {
//					if (clientId == client.getID()) {
						tableModel.addRow(
								new Object[] { clientId, JobDuration, title, payout, deadline, attachedFileName });
//					}
				}
			}

		} catch (DateTimeParseException e) {
			//System.out.println("Error parsing deadline for job: " + line);
		} catch (IOException | NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
}
