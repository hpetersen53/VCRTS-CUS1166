package gui;

import main.Vehicle;
import main.VCController;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class IncomingVehicles {
    private JFrame jframe;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private List<JButton> vehicles;
    private List<String> vehicleDetails;

    public IncomingVehicles() {
        jframe = new JFrame("Incoming Vehicle Requests");
        jframe.setSize(300, 300);

        vehicles = new ArrayList<>();
        vehicleDetails = readVehicles();

        if (!vehicleDetails.isEmpty()) {
            resizeButtons();

            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

            for (JButton vehicle : vehicles) {
                vehicle.addActionListener(e -> {
                    String detail = vehicleDetails.get(vehicles.indexOf(vehicle));
                    int result = JOptionPane.showConfirmDialog(null, detail, "Vehicle Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        System.out.println("Accepted");
                        Vehicle objVehicle = addToVehicleList(detail);
                        ControllerDashboard.saveVehicleData(objVehicle);
                        removeVehicle(vehicle, detail);
                    } else if (result == JOptionPane.NO_OPTION) {
                        System.out.println("Rejected");
                        removeVehicle(vehicle, detail);
                    } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                        System.out.println("Cancelled");
                        jframe.dispose();
                        new IncomingVehicles(); // Reload the frame
                    }
                });
            }

            // Back Button
            JButton backButton = new JButton("Back");
            backButton.setMaximumSize(new Dimension(80, 30));
            backButton.addActionListener(e -> {
                jframe.dispose(); // Close current frame
                VCController controller = new VCController();
                new ControllerDashboard(controller); // Navigate to the previous screen
            });

            // Add Buttons and Table
            for (JButton vehicleButton : vehicles) {
                jPanel.add(vehicleButton);
            }
            jScrollPane = new JScrollPane(jPanel);
            jframe.add(jScrollPane, BorderLayout.CENTER);
            jframe.add(backButton, BorderLayout.SOUTH);

            jframe.setVisible(true);
            jframe.setLocationRelativeTo(null);
        } else {
            JOptionPane.showMessageDialog(null, "No vehicle requests currently");
            VCController controller = new VCController();
            new ControllerDashboard(controller);
        }
    }

    private Vehicle addToVehicleList(String detail) {
        String[] vehicleDetail = detail.split("\n");
        List<String> vehicleObject = new ArrayList<>();
        for (String s : vehicleDetail) {
            String[] smallPart = s.split(":");
            vehicleObject.add(smallPart[1].trim());
        }

        return new Vehicle(
                vehicleObject.get(0), // VIN
                vehicleObject.get(1), // Make
                Integer.parseInt(vehicleObject.get(3)), //Year
                vehicleObject.get(2),//Model
                vehicleObject.get(4), // Color
                vehicleObject.get(5), // License Plate
                Double.parseDouble(vehicleObject.get(6)) // Residency
        );
    }

    public void resizeButtons() {
        for (JButton vehicle : vehicles) {
            vehicle.setMaximumSize(new Dimension(250, 50));
        }
    }

    public void removeVehicle(JButton vehicle, String detail) {
        if (vehicles.contains(vehicle) && vehicleDetails.contains(detail)) {
            vehicles.remove(vehicle);
            vehicleDetails.remove(detail);
            jPanel.remove(vehicle);

            if (vehicles.isEmpty()) {
                VCController controller = new VCController();
                new ControllerDashboard(controller);
                jframe.dispose();
            }

            rewriteVehicleFile();

            jPanel.revalidate();
            jPanel.repaint();
        }
    }

    private void rewriteVehicleFile() {
        String fileName = "IncomingVehicles.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String vehicleDetail : vehicleDetails) {
                writer.write(vehicleDetail);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readVehicles() {
        List<String> vehicleDetails = new ArrayList<>();
        String fileName = "IncomingVehicles.txt";

        String line;
        String details = null;
        String vehicleId = null, make = null, model = null, color = null, licensePlate = null;
        int year = 0;
        double residency = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("VIN:")) {
                    vehicleId = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Make:")) {
                    make = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Model:")) {
                    model = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Year:")) {
                    year = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("Color:")) {
                    color = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("License Plate:")) {
                    licensePlate = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Time Available:")) {
                    residency = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                } else if (line.isEmpty()) {
                    // Create a button and add details only when all fields are filled
                    if (vehicleId != null && make != null && model != null && color != null && licensePlate != null) {
                        vehicles.add(new JButton("VIN: " + vehicleId));
                        details = "VIN: " + vehicleId + "\nMake: " + make + "\nModel: " + model + "\nYear: " + year +
                                "\nColor: " + color + "\nLicense Plate: " + licensePlate + "\nResidency: " + residency;
                        vehicleDetails.add(details);

                        // Reset variables for the next vehicle
                        vehicleId = make = model = color = licensePlate = null;
                        year = 0;
                        residency = 0.0;
                    }
                }
            }

            // Handle the last vehicle entry if the file does not end with a blank line
            if (vehicleId != null && make != null && model != null && color != null && licensePlate != null) {
                vehicles.add(new JButton("VIN: " + vehicleId));
                details = "VIN: " + vehicleId + "\nMake: " + make + "\nModel: " + model + "\nYear: " + year +
                        "\nColor: " + color + "\nLicense Plate: " + licensePlate + "\nResidency: " + residency;
                vehicleDetails.add(details);
            }

        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return vehicleDetails;
    }

}