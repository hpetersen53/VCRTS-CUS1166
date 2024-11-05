package gui;

import javax.swing.*;
import main.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Stores user information - follows style of VehicleRegistration
class User {
	
	private String fName;
	private String lName;
	private String emailAddr;
	private String licenseNum;
	private String userType;
	private String password;
	
	
	
	
	public User(String fName, String lName, String emailAddr, String password, String licenseNum, String userType) {
		this.fName = fName;
		this.lName = lName;
		this.emailAddr = emailAddr;
		this.password = password;
		this.licenseNum = licenseNum;
		this.userType = userType;
	}
	
	public String getDetails() {
		return "User Details:\n" + "Name: " + fName + "" + lName + "\n" + "Email: " + emailAddr + "\n" + "License Number: " + licenseNum + "\n" + "User Type: " + userType;
		
	}
	public String toFileString() {
		return fName + ", " + lName + ", " + emailAddr + ", " + licenseNum + ", " + userType;
	}

	
}

public class UserRegistration {
	
	private JFrame jFrame;
	private VehicleRegistration vehicleReg;
	private JobRegistration jobReg;
	
	public UserRegistration() {
		jFrame = new JFrame("User Registration");
		//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(400,400);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(8,2));
		
		JLabel lblFName = new JLabel("First Name:");
		JTextField txtFName = new JTextField(20);
		
		JLabel lblLName = new JLabel("Last Name:");
		JTextField txtLName = new JTextField(20);
		
		JLabel lblEmail = new JLabel("Email Address:");
		JTextField txtEmail = new JTextField(20);
		
		JLabel lblPassword = new JLabel("Password:");
		JPasswordField txtPassword = new JPasswordField(20);
		
		JLabel lblLicense= new JLabel("License Number:");
		JTextField txtLicense = new JTextField(20);
		
		//make radio buttons instead
		
		JLabel lblRadio = new JLabel();
		JRadioButton btnU1 = new JRadioButton("Car Owner");
		JRadioButton btnU2 = new JRadioButton("Job Lister");
		ButtonGroup group = new ButtonGroup();
		group.add(btnU1);
		group.add(btnU2);
		
		JButton btnRegister = new JButton("Submit & Continue");
		
		
		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fName = txtFName.getText();
				String lName = txtLName.getText();
				String emailAddr = txtEmail.getText();
				String password = txtPassword.getText();
				String licenseNum = txtLicense.getText();
				String userType;
				
				if(btnU1.isSelected()) {
					userType = "Car Owner";
					vehicleReg = new VehicleRegistration();
				} else {
					userType = "Job Lister";
					jobReg = new JobRegistration();
				}
				
				
				//Validates information. Makes sure all boxes are filled.
				if(fName.isEmpty() || lName.isEmpty() || emailAddr.isEmpty() || licenseNum.isEmpty() || userType.isEmpty()) {
					JOptionPane.showMessageDialog(jFrame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				User user = new User(fName, lName, emailAddr, password, licenseNum, userType);
				saveUserData(user);
				JOptionPane.showMessageDialog(jFrame, user.getDetails(), "User Registered", JOptionPane.INFORMATION_MESSAGE);
				
				
				//Clears text fields
				txtFName.setText("");
				txtLName.setText("");
				txtEmail.setText("");
				txtLicense.setText("");
				
				if(btnU1.isSelected()) {
					vehicleReg = new VehicleRegistration();
				} else {
					jobReg = new JobRegistration();
				}
				
				
				
			}	
		});
		
		
		
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
		jPanel.add(btnU1);
		jPanel.add(btnU2);
		jPanel.add(btnRegister);
		
		jFrame.add(jPanel);
		jFrame.setVisible(true);
		
	}
	
	private static void saveUserData(User user) {
		String fileName = "UserRegistrations.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
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
