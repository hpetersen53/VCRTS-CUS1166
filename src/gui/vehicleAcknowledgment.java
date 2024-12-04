package gui;

import main.Vehicle;
import main.VCController;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class vehicleAcknowledgment {

    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;
    private List<JButton>vehicleButtons;
    private List<String> vehicleDetails;

    public vehicleAcknowledgment() {
        frame = new JFrame("Vehicle Acknowledgment");
        frame.setSize(300, 300);

        vehicleButtons = new ArrayList<>();
        vehicleDetails = readAcknowledgedVehciles();

        if (!vehicleDetails.isEmpty()) {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (JButton vehicleButton : vehicleButtons) {
                vehicleButton.addActionListener(e -> {
                    String detail = vehicleDetails.get(vehicleButtons.indexOf(vehicleButton));
                    JOptionPane.showMessageDialog(frame, detail, "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);

                    int result = JOptionPane.showConfirmDialog(frame, "Click OK to remove this vehicle.", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        removeVehicle(vehicleButton, detail);
                    }
                });
                panel.add(vehicleButton);
            }

            scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

        } else {
            JOptionPane.showMessageDialog(null, "No vehicles currently acknowledged.");
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();

        });

        frame.add(backButton, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void saveacceptedVehicleData(Vehicle vehicle) {
        String fileName = "VehicleAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

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

            writer.write("This Vehicle has been accepted, details below\n");
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("VIN: " + VIN + "\n");
            writer.write("Make: " + Make + "\n");
            writer.write("Model: " + Model + "\n");
            writer.write("Year: " + year + "\n");
            writer.write("Color: " + Color + "\n");
            writer.write("License Plate: " + LicensePlate + "\n");
            writer.write("Time Available: " + residency + "\n");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }

    public static void saverejectedVehicleData(Vehicle vehicle) {
        String fileName = "VehicleAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

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

            writer.write("This Vehicle has been rejected, details below\n");
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("VIN: " + VIN + "\n");
            writer.write("Make: " + Make + "\n");
            writer.write("Model: " + Model + "\n");
            writer.write("Year: " + year + "\n");
            writer.write("Color: " + Color + "\n");
            writer.write("License Plate: " + LicensePlate + "\n");
            writer.write("Time Available: " + residency + "\n");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readAcknowledgedVehciles() {
        List<String> vehicleDetails = new ArrayList<>();
        String fileName = "VehicleAcknowledgment.txt";

        String line;
        String details = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    details += line + "\n";
                } else if (!details.isEmpty()) {
                    vehicleButtons.add(new JButton("Vehicle ID: " + extractVehicleID(details)));
                    vehicleDetails.add(details.trim());
                    details = "";
                }
            }

            if (!details.isEmpty()) {
                vehicleButtons.add(new JButton("Vehicle ID: " + extractVehicleID(details)));
                vehicleDetails.add(details.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicleDetails;
    }

    private String extractVehicleID(String details) {
        for (String line : details.split("\n")) {
            if (line.startsWith("VIN:")) {
                return line.substring(line.indexOf(":") + 1).trim();
            }
        }
        return "Unknown";
    }

    private void removeVehicle(JButton vehicleButton, String detail) {
        if (vehicleButtons.contains(vehicleButton) && vehicleDetails.contains(detail)) {
            vehicleButtons.remove(vehicleButton);
            vehicleDetails.remove(detail);
            panel.remove(vehicleButton);

            rewriteAcknowledgmentFile();

            panel.revalidate();
            panel.repaint();
        }

        if (vehicleButtons.isEmpty()) {
            frame.dispose();
            JOptionPane.showMessageDialog(null, "No vehicles currently acknowledged.");

        }
    }

    private void rewriteAcknowledgmentFile() {
        String fileName = "VehicleAcknowledgment.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String detail : vehicleDetails) {
                writer.write(detail);
                writer.newLine();
                writer.newLine(); // Ensure spacing between entries
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
