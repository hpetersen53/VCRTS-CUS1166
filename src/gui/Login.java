package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.Client;
import main.VehicleOwner;
import main.VCController;

public class Login {
    private JFrame jFrame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login() {
        jFrame = new JFrame("Login");
        jFrame.setSize(400, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(5, 2));  // Updated grid layout to accommodate extra button

        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Create Account");
<<<<<<< HEAD
        JButton btnAdminLogin = new JButton("Login For Admin");
=======
        JButton btnAdminLogin = new JButton("Admin Login");  // New button for admin login
>>>>>>> branch 'master' of https://github.com/hpetersen53/VCRTS-CUS1166

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                Object user = authenticateUser(email, password);
                if (user instanceof Client) {
                    JOptionPane.showMessageDialog(jFrame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    jFrame.dispose();
                    new ClientDashboard((Client) user);
                } else if (user instanceof VehicleOwner) {
                    JOptionPane.showMessageDialog(jFrame, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    jFrame.dispose();
                    new VehicleOwnerDashboard((VehicleOwner)user);          
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
        
        btnAdminLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard();
            }
        });

        btnAdminLogin.addActionListener(new ActionListener() {  // Action for the Admin Login button
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                VCController controller = new VCController(); // Instantiate the controller
                new ControllerDashboard(controller); // Open the ControllerDashboard
            }
        });

        // Add components to panel
        jPanel.add(lblEmail);
        jPanel.add(txtEmail);
        jPanel.add(lblPassword);
        jPanel.add(txtPassword);
        jPanel.add(btnLogin);
        jPanel.add(btnRegister);
<<<<<<< HEAD
        jPanel.add(btnAdminLogin);
=======
        jPanel.add(new JLabel());  // Spacer
        jPanel.add(btnAdminLogin);  // Add Admin Login button
>>>>>>> branch 'master' of https://github.com/hpetersen53/VCRTS-CUS1166

        jFrame.add(jPanel);
        jFrame.setVisible(true);
    }

    private Object authenticateUser(String email, String password) {
        Client client = getClientFromFile("Client.txt", email, password);
        if (client != null) {
            return client;
        }

        VehicleOwner vehicleOwner = VehicleOwnerFromFile("VehicleOwner.txt", email, password);
        if (vehicleOwner != null) {
            return vehicleOwner;
        }

        return null; 
    }

    private Client getClientFromFile(String fileName, String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(": ", 2);
                if (parts.length > 1) {
                    
                    String userDetails = parts[1].trim(); 

                    
                    String[] userFields = userDetails.split(","); 

                    if (userFields.length >= 6) { 
                        
                        try {
                            int clientId = Integer.parseInt(userFields[0].trim());

                            String firstName = userFields[1].trim();
                            String lastName = userFields[2].trim();
                            String storedEmail = userFields[3].trim();
                            String storedPassword = userFields[4].trim();
                            String licenseNumber = userFields[5].trim();

                            if (storedEmail.equals(email) && storedPassword.equals(password)) {
                                return new Client(clientId, firstName, lastName, email, password, licenseNumber);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid ID format for client: " + userFields[0].trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private VehicleOwner VehicleOwnerFromFile(String fileName, String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(": ", 2); 
                if (parts.length > 1) {
                   
                    String userDetails = parts[1].trim();  
                    String[] userFields = userDetails.split(","); 

                    if (userFields.length >= 6) { 
                        try {
                            int VehicleId = Integer.parseInt(userFields[0].trim());

                            String firstName = userFields[1].trim();
                            String lastName = userFields[2].trim();
                            String storedEmail = userFields[3].trim();
                            String storedPassword = userFields[4].trim();
                            String licenseNumber = userFields[5].trim();

                            if (storedEmail.equals(email) && storedPassword.equals(password)) {
                            	return new VehicleOwner(VehicleId, firstName, lastName, email, password, userFields[4].trim());
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid ID format for client: " + userFields[0].trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
