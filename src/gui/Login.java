package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

                if (authenticateUser(emailAddr, password)) {
                    String userType = userTypes.get(email);
                    if (userType.equals("Car Owner")) {
                        VehicleOwnerDashboard();
                    } else if (userType.equals("Job Lister")) {
                        ClientDashboard();
                    }
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

    // Method to authenticate the user
    private boolean authenticateUser(String emailAddr , String password) {
        return userCredentials.containsKey(emailAddr) && userCredentials.get(emailAddr).equals(password);
    }

    private void VehicleOwnerDashboard() {
        JOptionPane.showMessageDialog(jFrame, "Welcome to the Car Owner Homepage!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

    }

    private void ClientDashboard() {
        JOptionPane.showMessageDialog(jFrame, "Welcome to the Job Lister Homepage!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
      
    }
}

