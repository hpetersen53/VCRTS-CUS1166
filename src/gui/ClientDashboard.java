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

        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Job ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Payout");
        tableModel.addColumn("Deadline");
        tableModel.addColumn("File Attached");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

       
        loadJobs();

        
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
        
        JButton btnCalc = new JButton("Calculate Completion Time");
        btnCalc.setPreferredSize(new Dimension(20,20));
        btnCalc.addActionListener(e -> cloudController.calculateCompletion());
        frame.add(btnCalc);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        
        frame.setVisible(true);
    }

    private void loadJobs() {
        String fileName = "JobListings.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
               
                String[] data = line.split(",");
                if (data.length < 5) continue; 

                try {
                   
                    int jobId = Integer.parseInt(data[0].trim()); 
                    int clientId = Integer.parseInt(data[1].trim()); 

                   
                    if (clientId == client.getID()) {
                        String title = data[2].trim(); 
                        double payout = Double.parseDouble(data[3].trim()); 
                        LocalDate deadline = LocalDate.parse(data[4].trim());
                        String attachedFileName = data.length > 5 && !data[5].trim().isEmpty() ? data[5].trim() : "None"; 

                        
                        tableModel.addRow(new Object[]{jobId, title, payout, deadline, attachedFileName});
                    }
                } catch (NumberFormatException e) {
                    
                    System.out.println("Error parsing job data: " + line);
                } catch (DateTimeParseException e) {
                    
                    System.out.println("Error parsing deadline for job: " + line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
