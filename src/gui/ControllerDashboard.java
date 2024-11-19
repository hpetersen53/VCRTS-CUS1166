package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.VCController;
import main.Job;
import main.VCRTS;
import main.Vehicle;

public class ControllerDashboard {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private VCController controller;

    private static final int PORT = 12345;
    private static final int PORT2= 54321;

    public ControllerDashboard(){
        new VCRTS();
    }
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

        JButton btnJobs = new JButton("Job Requests");
        btnJobs.addActionListener(e -> {
            new IncomingJobs();
            frame.dispose();
            });
        JButton btnVehicles = new JButton("Vehicle Requests");
        btnVehicles.addActionListener(e -> {
            new IncomingVehicles();
            frame.dispose();
            });

        // "Completion Time Check" button to display job completion times
        JButton btnCompletionCheck = new JButton("Completion Time Check");
        btnCompletionCheck.addActionListener(e -> showCompletionTimes());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnReturn);
        buttonPanel.add(btnCompletionCheck);
        buttonPanel.add(btnJobs);
        buttonPanel.add(btnVehicles);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

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

            // Append job details to the file
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("VIN: " + vehicle.getVIN() + "\n");
            writer.write("Make: " + vehicle.getMake() + "\n");
            writer.write("Model: " + vehicle.getModel() + "\n");
            writer.write("Year: " + vehicle.getYear() + "\n");
            writer.write("Color: " + vehicle.getColor() + "\n");
            writer.write("License Plate: " + vehicle.getLicensePlate() + "\n");
            writer.write("Time Available: " + vehicle.getResidency()+ "\n");
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
}
