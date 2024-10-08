package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import input_utils.MouseInputs;
import main.UserRegistration;
import main.VCRTS;
import main.VehicleRegistration;

public class GUIWindow {
	
	private JFrame jFrame;
	private MouseInputs mouseInputs;
	//private VehicleRegistration vehicleReg;
	private UserRegistration userReg;
	
	

		public GUIWindow() {
			
			
		//JButton = Buttons
			
		JButton user1Btn = new JButton("Register/Manage a Car");
		user1Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				//vehicleReg = new VehicleRegistration();
				userReg = new UserRegistration();
				
			}	
		});
		
		JButton user2Btn = new JButton("Post/Manage a Job");
		
			
		
		//JLabel = Panel Contents
		
		JLabel welcome = new JLabel("Welcome"); 
		welcome.setForeground(Color.white);
		welcome.setFont(new Font("Plain",Font.PLAIN, 30));
		
		JLabel chooseUser = new JLabel("I want to...");
		chooseUser.setForeground(Color.white);
		chooseUser.setFont(new Font("Plain", Font.PLAIN, 15));
		
		
		
		
		//JPanel = Window Contents
			
		JPanel banner = new JPanel();
		banner.setBackground(Color.DARK_GRAY);
		banner.setBounds(5,5,975,100);
		banner.add(welcome);
		
		JPanel promptMsg = new JPanel();
		promptMsg.setBackground(Color.DARK_GRAY);
		promptMsg.setBounds(5,110,975,50);
		promptMsg.add(chooseUser);
		
		JPanel user1Select = new JPanel();
		user1Select.setBackground(Color.DARK_GRAY);
		user1Select.setBounds(5,165,485,350);
		user1Select.add(user1Btn);
		
		JPanel user2Select = new JPanel();
		user2Select.setBackground(Color.DARK_GRAY);
		user2Select.setBounds(495,165,485,350);
		user2Select.add(user2Btn);
		
		
		//JFrame = GUI Window
		
		jFrame = new JFrame();
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminate on close
		jFrame.setTitle("Vehicular Cloud Real-Time System"); //window title
		jFrame.setResizable(false); //not resizable
		jFrame.setVisible(true); //visible
		jFrame.setSize(1000,600);
		jFrame.setLayout(null);
		jFrame.getContentPane().setBackground(Color.gray);
		
		//Window Components
		jFrame.add(banner);
		jFrame.add(promptMsg);
		jFrame.add(user1Select);
		jFrame.add(user2Select);
		
		
		
		
		
		
		
	
		
		
	
		
		
		
		
		
		
	}

}
