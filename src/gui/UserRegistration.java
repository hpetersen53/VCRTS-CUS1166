// UserRegistration.java
package gui;

import javax.swing.*;
import main.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserRegistration {

    private JFrame jFrame;
    private VehicleRegistration vehicleReg;
    private JobRegistration jobReg;

    public UserRegistration() {
        jFrame = new JFrame("User Registration");
        jFrame.setSize(400, 400);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(8, 2));

        JLabel lblFName = new JLabel("First Name:");
        JTextField txtFName = new JTextField(20);

        JLabel lblLName = new JLabel("Last Name:");
        JTextField txtLName = new JTextField(20);

        JLabel lblEmail = new JLabel("Email Address:");
        JTextField txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(20);

        JLabel lblLicense = new JLabel("License Number:");
        JTextField txtLicense = new JTextField(20);

        JLabel lblRadio = new JLabel("Select User Type:");
        JRadioButton btnU1 = new JRadioButton("Car Owner");
        JRadioButton btnU2 = new JRadioButton("Job Lister");
        ButtonGroup group = new ButtonGroup();
        group.add(btnU1);
        group.add(btnU2);

        JButton btnRegister = new JButton("Submit & Continue");
        JButton btnReturn = new JButton("Go Back");

        btnReturn.addActionListener(e ->  new GUIWindow());
        btnReturn.addActionListener(e ->  jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING)));
        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fName = txtFName.getText();
                String lName = txtLName.getText();
                String emailAddr = txtEmail.getText();
                String password = new String(txtPassword.getPassword());
                String licenseNum = txtLicense.getText();

                if (fName.isEmpty() || lName.isEmpty() || emailAddr.isEmpty() || password.isEmpty() || licenseNum.isEmpty() || (!btnU1.isSelected() && !btnU2.isSelected())) {
                    JOptionPane.showMessageDialog(jFrame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user;
                int clientId= 0;
                int vehicleId=0;
                VehicleOwner VehicleOwner;
                // Initialize `user` as either `VehicleOwner` or `Client`
                if (btnU1.isSelected()) {
                	VehicleOwner = new VehicleOwner(vehicleId,fName, lName, emailAddr, password, licenseNum);
                	user = VehicleOwner;  
                    vehicleReg = new VehicleRegistration(VehicleOwner);
                    JOptionPane.showMessageDialog(jFrame, VehicleOwner.getDetails(), "User Registered as Car Owner", JOptionPane.INFORMATION_MESSAGE);
                } else {
                	Client client = new Client(clientId, fName, lName, emailAddr, password, licenseNum);
                    user = client;  // Assign `client` to `user` to pass to `saveUserData`
                    jobReg = new JobRegistration(client);
                    JOptionPane.showMessageDialog(jFrame, client.getDetails(), "User Registered as Job Lister", JOptionPane.INFORMATION_MESSAGE);
                }

                // Now `user` is properly initialized before being passed to `saveUserData`
                saveUserData(user);

                // Clear text fields and radio selection
                txtFName.setText("");
                txtLName.setText("");
                txtEmail.setText("");
                txtLicense.setText("");
                group.clearSelection();
                
            }
        });
        
        btnRegister.addActionListener(e -> jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING)));

        jPanel.add(lblFName);
        jPanel.add(txtFName);
        jPanel.add(lblLName);
        jPanel.add(txtLName);
        jPanel.add(lblEmail);
        jPanel.add(txtEmail);
        jPanel.add(lblPassword);
        jPanel.add(txtPassword);
        jPanel.add(lblLicense);
        jPanel.add(txtLicense);
        jPanel.add(lblRadio);
        jPanel.add(btnU1);
        jPanel.add(btnU2);
        jPanel.add(btnRegister);
        jPanel.add(btnReturn);

        jFrame.add(jPanel);
        jFrame.setVisible(true);
    }

    private static void saveUserData(User user) {
        String fileName;
        if (user instanceof VehicleOwner) {
            fileName = "VehicleOwner.txt";
        } else if (user instanceof Client) {
            fileName = "Client.txt";
        } else {
            // Default filename in case of an unexpected user type
            fileName = "UserRegistrations.txt";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            writer.write(timestamp + ": " + user.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
