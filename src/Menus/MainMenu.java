package Menus;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

/**
 * The Main frame of the game
 * 
 * @author Jaren Chin-Hao Liu
 */
public class MainMenu {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static JFrame frame;

	/**
	 * Constructor method of the main frame
	 */
	public MainMenu(String name) {
		frame = new JFrame();
		Client client = new Client(name);
		client.start();
		client.updateList();
		ButtonPanel comp = new ButtonPanel(this, client);
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
		// frame.pack();
		frame.repaint();
		frame.revalidate();
	}

	public JFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {
		GameOptions.setKeyBindings();
		GameOptions.setSoundValues();

		String name = "test";

		if(args.length == 0) {
			name = JOptionPane.showInputDialog(frame, "Please Enter your username: ", "Input Username", JOptionPane.PLAIN_MESSAGE);
			if (name == null) {
				System.exit(0);
			}
			while (name.equals("")) {
				name = JOptionPane.showInputDialog(frame, "Please Enter your username: ", "Input Username", JOptionPane.PLAIN_MESSAGE);
			}

		} else {
			name = args[0];
		}

		MainMenu menu = new MainMenu(name);
		AudioPlayer.playMusic(AudioPlayer.MENU_SCREEN_TUNE);
	}
}
