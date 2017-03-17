package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

/**
 * The Main Menu of the game.
 * 
 * @author Jaren Chin-Hao Liu
 */
// TODO Maybe an animated background
public class ButtonPanel extends JPanel {
	private MainMenu menu;
	public Client client;

	/**
	 * Constructor for the main menu. Adds the buttons and how it looks in the
	 * main menu.
	 * 
	 * @param menu
	 *            The main frame which the game will use
	 */
	public ButtonPanel(MainMenu menu) {
		super();
		this.menu = menu;
		
		this.client = menu.client;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, c);
		JLabel title = new JLabel("<html>Space Flying 101<br><br>Welcome <font color='#66e0ff'>" + client.name +"</font></html>");
		title.setForeground(Color.WHITE);
		title.setOpaque(false);
		title.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		c.anchor = GridBagConstraints.NORTH;
		add(title, c);
		setBackground(Color.BLACK);
		
	}

	/**
	 * Creates the buttons which will be used on the main menu, such as Play,
	 * Settings and Exit.
	 * 
	 * @return A JPanel which will contain those buttons in BorderLayout.
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		MyButton play = new MyButton("Play");
		play.addActionListener(e -> {
			PlayPanel ppanel = new PlayPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(ppanel);
		});
		MyButton settings = new MyButton("Settings");
		settings.addActionListener(e -> {
			SettingsPanel spanel = new SettingsPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
		});
		MyButton exit = new MyButton("Exit");
		exit.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			System.exit(0);
		});
		panel.setLayout(new BorderLayout());
		panel.add(play, BorderLayout.NORTH);
		panel.add(settings, BorderLayout.CENTER);
		panel.add(exit, BorderLayout.SOUTH);
		return panel;
	}
	
}
