package gui;

import java.awt.*;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;  // <-- Add this import
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        
        int xPosition = screenWidth - frame.getWidth();  
        int yPosition = (screenHeight - frame.getHeight()) / 2;  
        frame.setLocation(xPosition+5, yPosition);

        
        JPanel mainPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("clientdashboard.jpg"));
                } catch (IOException e) {
                    System.out.println("Background image not found: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Initialize table model and add columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Client ID");
        tableModel.addColumn("Job Duration");
        tableModel.addColumn("Title");
        tableModel.addColumn("Payout");
        tableModel.addColumn("Deadline");
        tableModel.addColumn("File Attached");

        // Create table and link it to the table model
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        loadJobs(client.getID());

        mainPanel.add(new JLabel(client.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a button panel with styled buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // "Logout" button
        JButton btnReturn = new JButton("Logout");
        styleButton(btnReturn);
        btnReturn.setIcon(new ImageIcon("powerButton.png"));
        btnReturn.addActionListener(e -> {
            new GUIWindow();
            frame.dispose();
        });

        // "Add a Job" button
        JButton btnBackToJobRegistration = new JButton("Add a Job");
        styleButton(btnBackToJobRegistration);
        btnBackToJobRegistration.setIcon(new ImageIcon("plus.png"));
        btnBackToJobRegistration.addActionListener(e -> {
            new JobRegistration(client);
            frame.dispose();
        });

        // "Notifications" button
        JButton btnJobAcknowledgment = new JButton("Notifications");
        styleButton(btnJobAcknowledgment);
        btnJobAcknowledgment.setIcon(new ImageIcon("Bell.png"));
        btnJobAcknowledgment.addActionListener(e -> {
            new jobAcknowledgment();
            reloadJobs();
        });

        // Add buttons to the button panel
        buttonPanel.add(btnReturn, BorderLayout.WEST);
        buttonPanel.add(btnBackToJobRegistration, BorderLayout.CENTER);
        buttonPanel.add(btnJobAcknowledgment, BorderLayout.EAST);

        // Add button panel to the frame (below the background)
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);

        // Set frame visibility
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setBackground(new Color(217, 217, 217)); // Light gray
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
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
                    tableModel.addRow(
                            new Object[] { clientId, JobDuration, title, payout, deadline, attachedFileName });
                }
            }

        } catch (DateTimeParseException e) {
            //System.out.println("Error parsing deadline for job: " + line);
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }
    private void reloadJobs() {
        // Clear the existing rows from the table
        tableModel.setRowCount(0);
        
        // Reload jobs from the file
        loadJobs(client.getID());
    }
}

