package Menus;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainMenu {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JFrame frame;
	
	public MainMenu() {
		frame = new JFrame();
		ButtonPanel comp = new ButtonPanel(this);
		frame.setContentPane(comp);
		//frame.setUndecorated(true);
		Dimension d = new Dimension(800, 600);
		frame.setSize(d);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void changeFrame(JPanel panel) {
		frame.setContentPane(panel);
		frame.repaint();
		frame.revalidate();
	}
	
	public static void main(String[] args){
		MainMenu menu = new MainMenu();
	}
}
