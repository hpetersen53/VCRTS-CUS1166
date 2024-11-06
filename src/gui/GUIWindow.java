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
	
	// private VehicleRegistration vehicleReg;
	private UserRegistration userReg;
	

	public GUIWindow() {

		// JButton = Buttons

		JButton user1Btn = new JButton("Register or Manage a Car");
		user1Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// vehicleReg = new VehicleRegistration();
				userReg = new UserRegistration();

			}
		});
		

		JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();  // Opens the LoginUI screen
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

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminate on close
		jFrame.setTitle("Vehicular Cloud Real-Time System"); // window title
		jFrame.setResizable(true); // not resizable
		jFrame.setVisible(true); // visible
		jFrame.setSize(400, 500);
		//jFrame.setLayout(null);
		
		jFrame.add(jPanel);

		
		
		
		

	}

}
