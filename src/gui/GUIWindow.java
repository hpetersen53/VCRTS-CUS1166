package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.VCRTS;
import java.awt.GridBagLayout;

public class GUIWindow {

	private JFrame jFrame;
	
	
	private UserRegistration userReg;
	

	public GUIWindow() {

		

		JButton user1Btn = new JButton("Sign Up");
		user1Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
				userReg = new UserRegistration();

			}
		});
		

		JButton loginBtn = new JButton("Sign In");
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();  
            }
        });
        

		JLabel welcome = new JLabel("Welcome to the VCRTS!");
		welcome.setFont(new Font("Verdana", Font.BOLD, 15));
		
		JLabel introduction = new JLabel("The Vehicular Cloud Real Time System was made for managing and organizing computation resources and jobs in vehicular cloud.");
introduction.setFont(new Font("Verdana", Font.PLAIN,12));

		JPanel jPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(10,10,10,10);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		jPanel.add(welcome, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		gbc.weighty = 0.2;
		jPanel.add(introduction, gbc);
		
		gbc.gridx = 0;
        gbc.gridy = 2; 
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0.5;
        gbc.insets = new java.awt.Insets(10, 10, 10, 5); 
        jPanel.add(user1Btn, gbc);
		
		gbc.gridx = 1;
        gbc.gridy = 2; 
        gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0.5;
        gbc.insets = new java.awt.Insets(10, 5, 10, 10); 
        jPanel.add(loginBtn, gbc);
		
		
		jFrame = new JFrame();

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		jFrame.setTitle("Vehicular Cloud Real-Time System"); 
		jFrame.setVisible(true); 
		jFrame.setSize(400, 500);
		jFrame.add(jPanel);
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
		
		
		

		
		
		
		

	}

}
