package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import main.Client;
import main.VCController;
import main.VehicleOwner;

public class Login {
    private JFrame jFrame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login() {
        jFrame = new JFrame("Login");
        jFrame.setSize(400, 300);
        

        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Create Account");
        JButton btnAdminLogin = new JButton("Admin Login");
        
        btnAdminLogin.addActionListener(new ActionListener() {  // Action for the Admin Login button
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                VCController controller = new VCController(); // Instantiate the controller
                new ControllerDashboard(controller); // Open the ControllerDashboard
            }
        });
        
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
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(txtPassword, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(btnLogin, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(btnRegister, gbc);
        
        // Position the Admin Login button
        gbc.gridx = 1;
        gbc.gridy = 3;  // Place it on the next row
        gbc.gridwidth = 1; // Span across two columns for better alignment
        gbc.anchor = GridBagConstraints.WEST; // Center-align the button
        jPanel.add(btnAdminLogin, gbc);

        jFrame.add(jPanel);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
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
                            int clientId = Integer.parseInt(userFields[0].trim());  // ID should be the first field

                            
                            String firstName = userFields[1].trim();
                            String lastName = userFields[2].trim();
                            String storedEmail = userFields[3].trim();
                            String storedPassword = userFields[4].trim();
                            String licenseNumber = userFields[5].trim(); // Assuming this is the license number or other info

                            
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

    private VehicleOwner getVehicleOwnerFromFile1(String fileName, String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length > 1) {
                    String userDetails = parts[1];
                    String[] userFields = userDetails.split(", ");

                    String storedEmail = userFields[2].trim();
                    String storedPassword = userFields[3].trim();

                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        int vehicleId = Integer.parseInt(userFields[0].trim());
                        String firstName = userFields[1].trim();
                        String lastName = userFields[2].trim();
                        return new VehicleOwner(vehicleId, firstName, lastName, email, password, userFields[4].trim());
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(jFrame, "Error accessing user data", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
}
