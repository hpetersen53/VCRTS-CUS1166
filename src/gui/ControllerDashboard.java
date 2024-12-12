package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import main.VCController;
import main.Job;
import main.VCRTS;
import main.Vehicle;

public class ControllerDashboard {
    private JFrame frame;
    private JTable jobTable, incomingJobsTable, vehicleTable, incomingVehiclesTable;
    private DefaultTableModel jobTableModel, incomingJobsTableModel, vehicleTableModel, incomingVehiclesTableModel;
    private JTabbedPane tabbedPane;
    private VCController controller;

    private static final int PORT = 12345;
    private static final int PORT2 = 54321;

    public ControllerDashboard() {
        new VCRTS();
    }

    public ControllerDashboard(VCController controller, int SelectedTabIndex) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null.");
        }
        this.controller = controller;

        frame = new JFrame("Controller Dashboard");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        
        int xPosition = 0;  
        int yPosition = (screenHeight - frame.getHeight()) / 2;  
        frame.setLocation(xPosition-5, yPosition);

        // Initialize the tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(e -> {
            int currentTab = tabbedPane.getSelectedIndex();
            switch (currentTab) {
                case 0:
                    jobTableModel.setRowCount(0);
                    loadJobsRegistered();
                    break;
                case 1:
                    incomingJobsTableModel.setRowCount(0);
                    loadIncomingJobs();
                    break;
                case 2:
                    vehicleTableModel.setRowCount(0);
                    loadVehiclesRegistration();
                    break;
                case 3:
                    incomingVehiclesTableModel.setRowCount(0);
                    loadIncomingVehicles();
                    break;
            }
        });

         initializeJobPanel();
         initializeIncomingJobsPanel();
         initializeVehiclePanel();
         initializeIncomingVehiclesPanel();
 
         frame.add(tabbedPane, BorderLayout.CENTER);

         tabbedPane.setSelectedIndex(SelectedTabIndex);
 
         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
 
         // "Logout" button
         JButton btnReturn = new JButton("Logout");
         styleButton(btnReturn);
         btnReturn.setIcon(new ImageIcon("powerButton.png"));
         btnReturn.addActionListener(e -> {
             new GUIWindow();
             frame.dispose();
         });
 
         // "Completion Time Check" button
         JButton btnCompletionCheck = new JButton("Completion Time");
         styleButton(btnCompletionCheck);
         btnCompletionCheck.setIcon(new ImageIcon("Clock.png"));
         btnCompletionCheck.addActionListener(e -> showCompletionTimes());
 
         // "Job Requests" button
         JButton btnJobs = new JButton("Job Requests");
         styleButton(btnJobs);
         btnJobs.setIcon(new ImageIcon("tick.png"));
         btnJobs.addActionListener(e -> {
             new IncomingJobs();
             frame.dispose();
         });
 
         // "Vehicle Requests" button
         JButton btnVehicles = new JButton("Vehicle Requests");
         styleButton(btnVehicles);
         btnVehicles.setIcon(new ImageIcon("tick.png"));
         btnVehicles.addActionListener(e -> {
             new IncomingVehicles();
             frame.dispose();
         });
 
         buttonPanel.add(btnReturn);
         buttonPanel.add(btnCompletionCheck);
         buttonPanel.add(btnJobs);
         buttonPanel.add(btnVehicles);
 
         frame.add(buttonPanel, BorderLayout.SOUTH);
 
         frame.setVisible(true);
     }
 
     private void styleButton(JButton button) {
         button.setPreferredSize(new Dimension(160, 40));
         button.setFont(new Font("Inter", Font.BOLD, 12));
         button.setBackground(new Color(217, 217, 217)); // Light gray
         button.setForeground(Color.BLACK);
         button.setFocusPainted(false);
     }
 

    /*private void loadJobs() {
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
    }*/


    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                handleClientRequest(clientSocket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startServer2() {
        try (ServerSocket serverSocket2 = new ServerSocket(PORT2)) {
            System.out.println("Server started on port " + PORT2);

            while (true) {
                Socket vehicleSocket = serverSocket2.accept();

                handleVehicleRequest(vehicleSocket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleVehicleRequest(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Read job submission
            Vehicle vehicle = (Vehicle) in.readObject();
            System.out.println("Received vehicle: " + vehicle.getDetails());
            addVehicleRequest(vehicle);

            // Decide whether to accept or reject
//            boolean isAccepted = decideAcceptance(job);

            // Send response back to client
//            out.writeBoolean(isAccepted);
            out.flush();

//            if (isAccepted) {
//                saveJobData(job);
//                System.out.println("Saved");// Save the job if accepted
//            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void handleClientRequest(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Read job submission
            Job job = (Job) in.readObject();
            System.out.println("Received job: " + job.getDetails());
            addClientRequest(job);

            // Decide whether to accept or reject
//            boolean isAccepted = decideAcceptance(job);

            // Send response back to client
//            out.writeBoolean(isAccepted);
            out.flush();

//            if (isAccepted) {
//                saveJobData(job);
//                System.out.println("Saved");// Save the job if accepted
//            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean decideAcceptance(Job job) {
        // Simple logic for acceptance - can be expanded
        return true;
    }

    public static void saveJobData(Job job) {
        String fileName = "JobListings.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Append job details to the file
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Client ID: " + job.getClientID() + "\n");
            writer.write("Level of Redundancy: " + job.getLevelOfRedundancy() + "\n");
            writer.write("Job Duration: " + job.getJobDuration() + "\n");
            writer.write("Payout: " + job.getPayout() + "\n");
            writer.write("Title: " + job.getTitle() + "\n");
            writer.write("Deadline: " + job.getDeadline() + "\n");
            writer.write("FileName: " + (job.getAttachedFileName() != null ? job.getAttachedFileName() : "N/A") + "\n");
            writer.newLine(); // Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
    public static void saveVehicleData(Vehicle vehicle) {

        String fileName = "VehicleRegistrations.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            String VIN= vehicle.getVIN();
            String Make= vehicle.getMake();
            String Model= vehicle.getModel();
            String Color=vehicle.getColor();
            String LicensePlate= vehicle.getLicensePlate();
            int year= vehicle.getYear();
            double residency= vehicle.getResidency();

            // Append job details to the file
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("VIN: " + VIN + "\n");
            writer.write("Make: " + Make + "\n");
            writer.write("Model: " + Model + "\n");
            writer.write("Year: " + year + "\n");
            writer.write("Color: " + Color + "\n");
            writer.write("License Plate: " + LicensePlate + "\n");
            writer.write("Time Available: " + residency + "\n");
            writer.newLine();// Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
    private void addVehicleRequest(Vehicle vehicle) {

        String fileName = "IncomingVehicles.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Append job details to the file
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("VIN: " + vehicle.getVIN() + "\n");
            writer.write("Make: " + vehicle.getMake() + "\n");
            writer.write("Model: " + vehicle.getModel() + "\n");
            writer.write("Year: " + vehicle.getYear() + "\n");
            writer.write("Color: " + vehicle.getColor() + "\n");
            writer.write("License Plate: " + vehicle.getLicensePlate() + "\n");
            writer.write("Time Available: " + vehicle.getResidency()+ "\n");
            writer.newLine(); // Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
    private void addClientRequest(Job job) {
        String fileName = "IncomingJobs.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Append job details to the file
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Client ID: " + job.getClientID() + "\n");
            writer.write("Level of Redundancy: " + job.getLevelOfRedundancy() + "\n");
            writer.write("Job Duration: " + job.getJobDuration() + "\n");
            writer.write("Payout: " + job.getPayout() + "\n");
            writer.write("Title: " + job.getTitle() + "\n");
            writer.write("Deadline: " + job.getDeadline() + "\n");
            writer.write("FileName: " + (job.getAttachedFileName() != null ? job.getAttachedFileName() : "N/A") + "\n");
            writer.newLine(); // Add an empty line between jobs
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }

    private void showCompletionTimes() {
        ArrayList<Integer> clientIDs = new ArrayList<>();
        Queue<Integer> cumulativeDurations = new LinkedList<>();
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

        // Format Cumulative Durations by using a temporary queue to iterate without removing elements
        Queue<Integer> tempQueue = new LinkedList<>(cumulativeDurations);
        while (!tempQueue.isEmpty()) {
            message.append(tempQueue.poll());
            if (!tempQueue.isEmpty()) {
                message.append(", ");
            }
        }

        // Display the popup
        JOptionPane.showMessageDialog(frame, message.toString(), "Completion Times", JOptionPane.PLAIN_MESSAGE);
    }
    private void loadJobsRegistered() {
        String fileName = "JobListings.txt";
        String line;
        int clientId = 0;
        String title = "";
        int jobDuration = 0;
        double payout = 0.0;
        LocalDate deadline = null;
        String attachedFileName = "None";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Client ID:")) {
                    clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Job Duration:")) {
                    jobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Title:")) {
                    title = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Payout:")) {
                    payout = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Deadline:")) {
                    deadline = LocalDate.parse(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("FileName:")) {
                    attachedFileName = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.isEmpty()) {
                    jobTableModel.addRow(new Object[]{clientId, jobDuration, title, payout, deadline, attachedFileName});
                }
            }
        } catch (DateTimeParseException | IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void loadIncomingJobs() {
        String fileName = "IncomingJobs.txt";
        String line;
        int clientId = 0;
        String title = "";
        int jobDuration = 0;
        double payout = 0.0;
        LocalDate deadline = null;
        String attachedFileName = "None";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Client ID:")) {
                    clientId = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Job Duration:")) {
                    jobDuration = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Title:")) {
                    title = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Payout:")) {
                    payout = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Deadline:")) {
                    deadline = LocalDate.parse(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("FileName:")) {
                    attachedFileName = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.isEmpty()) {
                    incomingJobsTableModel.addRow(new Object[]{clientId, jobDuration, title, payout, deadline, attachedFileName});
                }
            }
        } catch (DateTimeParseException | IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void loadVehiclesRegistration() {
        String fileName = "VehicleRegistrations.txt";
        loadVehicleData(fileName, vehicleTableModel);
    }

    private void loadIncomingVehicles() {
        String fileName = "IncomingVehicles.txt";
        loadVehicleData(fileName, incomingVehiclesTableModel);
    }

    private void loadVehicleData(String fileName, DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String vehicleId = null, make = null, model = null, licensePlate = null, color = null;
            int year = 0;
            double residency = 0.0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("VIN:")) {
                    vehicleId = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Make:")) {
                    make = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Model:")) {
                    model = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Color:")) {
                    color = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Year:")) {
                    year = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("License Plate:")) {
                    licensePlate = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Time Available:")) {
                    residency = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                }

                if (vehicleId != null && make != null && model != null && color != null && licensePlate != null && year != 0 && residency != 0) {
                    tableModel.addRow(new Object[]{vehicleId, make, model, color, year, licensePlate, residency});
                    vehicleId = make = model = color = licensePlate = null;
                    year = 0;
                    residency = 0.0;
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeJobPanel() {
        // Create table model for jobs
        jobTableModel = new DefaultTableModel(
                new Object[]{"Client ID", "Job Duration", "Title", "Payout", "Deadline", "File Name"}, 0);
        jobTable = new JTable(jobTableModel);

        // Load data for jobs
        loadJobsRegistered();

        // Add table to panel
        JScrollPane scrollPane = new JScrollPane(jobTable);
        JPanel jobPanel = new JPanel(new BorderLayout());
        jobPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to tabbed pane
        tabbedPane.addTab("Registered Jobs", jobPanel);
    }

    private void initializeIncomingJobsPanel() {
        // Create table model for incoming jobs
        incomingJobsTableModel = new DefaultTableModel(
                new Object[]{"Client ID", "Job Duration", "Title", "Payout", "Deadline", "File Name"}, 0);
        incomingJobsTable = new JTable(incomingJobsTableModel);

        // Load data for incoming jobs
        loadIncomingJobs();

        // Add table to panel
        JScrollPane scrollPane = new JScrollPane(incomingJobsTable);
        JPanel incomingJobsPanel = new JPanel(new BorderLayout());
        incomingJobsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to tabbed pane
        tabbedPane.addTab("Incoming Jobs", incomingJobsPanel);
    }

    private void initializeVehiclePanel() {
        // Create table model for vehicle registrations
        vehicleTableModel = new DefaultTableModel(
                new Object[]{"VIN", "Make", "Model", "Color", "Year", "License Plate", "Time Available"}, 0);
        vehicleTable = new JTable(vehicleTableModel);

        // Load data for vehicles
        loadVehiclesRegistration();

        // Add table to panel
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        vehiclePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to tabbed pane
        tabbedPane.addTab("Registrated Vehicles", vehiclePanel);
    }

    private void initializeIncomingVehiclesPanel() {
        // Create table model for incoming vehicles
        incomingVehiclesTableModel = new DefaultTableModel(
                new Object[]{"VIN", "Make", "Model", "Color", "Year", "License Plate", "Time Available"}, 0);
        incomingVehiclesTable = new JTable(incomingVehiclesTableModel);

        // Load data for incoming vehicles
        loadIncomingVehicles();

        // Add table to panel
        JScrollPane scrollPane = new JScrollPane(incomingVehiclesTable);
        JPanel incomingVehiclesPanel = new JPanel(new BorderLayout());
        incomingVehiclesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to tabbed pane
        tabbedPane.addTab("Incoming Vehicles", incomingVehiclesPanel);
    }

}