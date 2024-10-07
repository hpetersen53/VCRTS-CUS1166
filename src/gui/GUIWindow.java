package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import input_utils.MouseInputs;
import main.VCRTS;

public class GUIWindow {
	
	private JFrame jFrame;
	private MouseInputs mouseInputs;
	
	

		public GUIWindow() {
			
			
		//JLabel = Panel Contents
		
		JLabel welcome = new JLabel(); 
		welcome.setText("Welcome");
		welcome.setForeground(Color.white);
		welcome.setFont(new Font("Plain",Font.PLAIN, 30));
		
		//JPanel = Window Contents
			
		JPanel banner = new JPanel();
		banner.setBackground(Color.DARK_GRAY);
		banner.setBounds(5,5,975,190);
		banner.add(welcome);
		
		JPanel user1Select = new JPanel();
		user1Select.setBackground(Color.DARK_GRAY);
		user1Select.setBounds(5,200,975,375);
		
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
		jFrame.add(user1Select);
		
		
		
		
		
		
		
	
		
		
	
		
		
		
		
		
		
	}

}
