package main;

import javax.swing.*;
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
	
	
	
	public User(String fName, String lName, String emailAddr, String licenseNum, String userType) {
		this.fName = fName;
		this.lName = lName;
		this.emailAddr = emailAddr;
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
	
	public UserRegistration() {
		jFrame = new JFrame("User Registration");
		//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(400,400);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(7,2));
		
		JLabel lblFName = new JLabel("First Name:");
		JTextField txtFName = new JTextField(20);
		
		JLabel lblLName = new JLabel("Last Name:");
		JTextField txtLName = new JTextField(20);
		
		JLabel lblEmail = new JLabel("Email Address:");
		JTextField txtEmail = new JTextField(20);
		
		JLabel lblLicense= new JLabel("License Number:");
		JTextField txtLicense = new JTextField(20);
		
		//make radio buttons instead
		JLabel lblUType = new JLabel("UserType:");
		JTextField txtUType = new JTextField(20); 
		
		JButton btnRegister = new JButton("Submit & Continue");
		
		
		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fName = txtFName.getText();
				String lName = txtLName.getText();
				String emailAddr = txtEmail.getText();
				String licenseNum = txtLicense.getText();
				String userType = txtUType.getText();
				
				
				//Validates information. Makes sure all boxes are filled.
				if(fName.isEmpty() || lName.isEmpty() || emailAddr.isEmpty() || licenseNum.isEmpty() || userType.isEmpty()) {
					JOptionPane.showMessageDialog(jFrame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				User user = new User(fName, lName, emailAddr, licenseNum, userType);
				//saveUserData(user);
				JOptionPane.showMessageDialog(jFrame, user.getDetails(), "User Registered", JOptionPane.INFORMATION_MESSAGE);
				
				//Clears text fields
				txtFName.setText("");
				txtLName.setText("");
				txtEmail.setText("");
				txtLicense.setText("");
				txtUType.setText("");
				
				//if car owner then bring to vehicle registration
				//if business owner then bring to job posting
				vehicleReg = new VehicleRegistration();
				
			}	
		});
		
		jPanel.add(lblFName);
		jPanel.add(txtFName);
		jPanel.add(lblLName);
		jPanel.add(txtLName);
		jPanel.add(lblEmail);
		jPanel.add(txtEmail);
		jPanel.add(lblLicense);
		jPanel.add(txtLicense);
		jPanel.add(lblUType);
		jPanel.add(txtUType);
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
