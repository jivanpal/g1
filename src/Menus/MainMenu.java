package Menus;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GameLogic.KeyBindings;

/**
 * The Main frame of the game
 * 
 * @author Jaren Chin-Hao Liu
 */
public class MainMenu {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JFrame frame;

	/**
	 * Constructor method of the main frame
	 */
	public MainMenu() {
		frame = new JFrame();
		ButtonPanel comp = new ButtonPanel(this);
		frame.setContentPane(comp);
		// frame.setUndecorated(true);
		Dimension d = new Dimension(800, 600);
		frame.setSize(d);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Main frame changes the existing panel to the specified panel.
	 * 
	 * @param panel
	 *            The JPanel to change to.
	 */
	public void changeFrame(JPanel panel) {
		frame.setContentPane(panel);
		frame.repaint();
		frame.revalidate();
	}

	public static void main(String[] args) {
		KeyBindings.setKeyBindings();
		MainMenu menu = new MainMenu();
		
	}
}
