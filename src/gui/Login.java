package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Login {
    private JFrame jFrame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private Map<String, String> userCredentials;
    private Map<String, String> userTypes;

    public Login() {
        userCredentials = new HashMap<>();
        userTypes = new HashMap<>();

        jFrame = new JFrame("Login");
        jFrame.setSize(400, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(4, 2));

        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Create Account");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                if (authenticateUser(email, password)) {
                    JOptionPane.showMessageDialog(jFrame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    jFrame.dispose();
                    
                    
                    new VehicleOwnerDashboard();
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserRegistration();
            }
        });

        jPanel.add(lblEmail);
        jPanel.add(txtEmail);
        jPanel.add(lblPassword);
        jPanel.add(txtPassword);
        jPanel.add(btnLogin);
        jPanel.add(btnRegister);

        jFrame.add(jPanel);
        jFrame.setVisible(true);
    }

    
    private boolean authenticateUser(String email, String password) {
        String fileName = "UserRegistrations.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Separate timestamp from user details
                String[] parts = line.split(": ");
                if (parts.length > 1) {
                    String userDetails = parts[1];
                    String[] userFields = userDetails.split(", ");
                    
                    
                    String storedEmail = userFields[2].trim();
                    String storedPassword = userFields[3].trim();

                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(jFrame, "Error accessing user data", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(jFrame, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        return false; // Authentication failed
    }
}
