package gui;

import javax.swing.*;

public class VehicleOwnerDashboard {
    public VehicleOwnerDashboard() {
        JFrame frame = new JFrame("Vehicle Owner Dashboard");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel("Welcome to the Vehicle Owner Dashboard!", SwingConstants.CENTER));
        frame.setVisible(true);
    }
}
