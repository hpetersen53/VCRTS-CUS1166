package main;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dash {
	
	public Dash() {
		
		JLabel title = new JLabel("Welcome to Your Dashboard");
		title.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel info = new JLabel("Info Coming Soon");
		info.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(2,1));
		jPanel.add(title);
		jPanel.add(info);
		
		
		JFrame jFrame = new JFrame();
		jFrame.setSize(400,400);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jFrame.add(jPanel);
		
	}

}
