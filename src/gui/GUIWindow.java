package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GUIWindow extends JFrame {

    private JButton signUpButton, signInButton;
    private JLabel logoLabel, welcomeLabel, descriptionLabel;
    private JPanel mainPanel, buttonPanel;

    public GUIWindow() {
       
        setTitle("Vehicular Cloud Real-Time System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setBackground(new Color(160, 208, 240)); 

        // Welcome label
        welcomeLabel = new JLabel(
                "<html><span style='font-size:18px; color:white; font-weight:bold;'>Welcome to the VCRTS!</span></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(welcomeLabel); 

        // Logo
        try {
            BufferedImage logoImage = ImageIO.read(new File("car.png"));
            Image scaledImage = logoImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(logoLabel); 
        } catch (IOException e) {
            System.out.println("Logo image not found: " + e.getMessage());
        }

        // Description label
        descriptionLabel = new JLabel(
                "<html><center><span style='font-size:12px; color:white;'>"
                        + "The Vehicular Cloud Real-Time System is designed to manage computation resources "
                        + "and job assignments in a vehicular cloud. Get started by signing up or logging in."
                        + "</span></center></html>");
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER); 
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        mainPanel.add(descriptionLabel); 

        // Buttons panel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(160, 208, 240)); 
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Sign Up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(120, 40)); 
        signUpButton.setBackground(new Color(217, 217, 217));
        signUpButton.setForeground(new Color(0, 0, 0));
        signUpButton.setFont(new Font("Inter", Font.BOLD, 16));
        signUpButton.addActionListener(e -> {
            new UserRegistration(); // Redirect to UserRegistration window
            dispose(); 
        });
        buttonPanel.add(signUpButton);

        // Sign In button
        signInButton = new JButton("Sign In");
        signInButton.setPreferredSize(new Dimension(120, 40));
        signInButton.setBackground(new Color(217, 217, 217));
        signInButton.setForeground(new Color(0, 0, 0));
        signInButton.setFont(new Font("Inter", Font.BOLD, 16));
        signInButton.addActionListener(e -> {
            new Login(); // Redirect to Login window
            dispose(); 
        });
        buttonPanel.add(signInButton);

        // Add panels to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GUIWindow();
    }
}
