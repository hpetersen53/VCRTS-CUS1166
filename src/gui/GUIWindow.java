package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.VCRTS;


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

		JLabel welcome = new JLabel("Welcome");
		welcome.setHorizontalAlignment(JLabel.CENTER);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(8,2));
		
		jPanel.add(welcome);
		jPanel.add(user1Btn);
		jPanel.add(loginBtn);
		
		jFrame = new JFrame();

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		jFrame.setTitle("Vehicular Cloud Real-Time System"); 
		jFrame.setResizable(true); 
		jFrame.setVisible(true); 
		jFrame.setSize(400, 500);
		//jFrame.setLayout(null);
		
		jFrame.add(jPanel);

		
		
		
		

	}

}
