package Menus;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	public Client client;

	/**
	 * Constructor method of the main frame
	 */
	public MainMenu(String name) {

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font xirod = Font.createFont(Font.TRUETYPE_FONT, new File(GameOptions.XIROD_FONT_FILE));
			ge.registerFont(xirod);

			GameOptions.LARGE_BOLD_TEXT_FONT = new Font("xirod", Font.BOLD, 36);
			GameOptions.REGULAR_TEXT_FONT = new Font("xirod", Font.PLAIN, 12);
			GameOptions.BUTTON_FONT = new Font("xirod", Font.PLAIN, 24);
			GameOptions.FULLSCREEN_BOLD_TEXT_FONT = new Font("xirod", Font.BOLD, 72);
			GameOptions.FULLSCREEN_BOLD_REAL_BIG_TEXT_FONT = new Font("xirod", Font.BOLD, 200);

		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client = new Client(name);
		frame = new JFrame();
		client.updateList();
		ButtonPanel comp = new ButtonPanel(this);
		Dimension d = new Dimension(800, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(d);
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
		PanelsManager.PANEL_BOUNDS = new Rectangle(0, 0, getFrame().getWidth(), getFrame().getHeight());
		PanelsManager.UILayers = getFrame().getLayeredPane();
		comp.makeUI();
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				PanelsManager.PANEL_BOUNDS = new Rectangle(0, 0, getFrame().getWidth(), getFrame().getHeight());
				PanelsManager.resizeComponents();
				frame.repaint();
				frame.revalidate();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

		});
		frame.setContentPane(comp);
		frame.repaint();
		frame.revalidate();
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

	/**
	 * Gets the frame of the JFrame
	 * 
	 * @return The frame of the program
	 */
	public JFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {
		// Initialise the key bindings and sound values.
		GameOptions.setKeyBindings();
		GameOptions.setSoundValues();

		String name = "test";

		// Enter the username which will be used to play the games.
		if (args.length == 0) {
			name = JOptionPane.showInputDialog(frame, "Please Enter your username: ", "Input Username",
					JOptionPane.PLAIN_MESSAGE);
			if (name == null) {
				System.exit(0);
			}
			try {
				while (name.equals("") || name.contains("#")) {

					name = JOptionPane.showInputDialog(frame, "Please Enter your username: ", "Input Username",
							JOptionPane.PLAIN_MESSAGE);

				}
			} catch (NullPointerException e) {
				System.exit(0);
			}

		} else {
			name = args[0];
		}

		MainMenu menu = new MainMenu(name);
		AudioPlayer.playMusic(AudioPlayer.MENU_SCREEN_TUNE);
	}
}
