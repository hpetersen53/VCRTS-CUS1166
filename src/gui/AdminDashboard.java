package gui;

import java.awt.BorderLayout;
import main.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;



public class AdminDashboard {
	
	private VCController cloudController;

	public AdminDashboard() {

		JFrame frame = new JFrame();

		frame = new JFrame("Vehicle Owner Dashboard");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		

		 JButton btnCalc = new JButton("Calculate Completion Time");
		 btnCalc.addActionListener(e -> cloudController.calculateCompletion());
		 frame.add(btnCalc);

		JButton btnReturn = new JButton("Go Back");
		btnReturn.addActionListener(e -> {
			new GUIWindow();
			//frame.dispose();
		});
		frame.add(btnReturn, BorderLayout.SOUTH);

		frame.setVisible(true);

	}

}
