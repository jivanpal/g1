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
import Audio.AudioPlayer;
import ClientNetworking.Client;

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
	public ButtonPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		
		this.client = client;
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
		JLabel title = new JLabel("<html>Space Flying 101<br><br>Welcome " + client.name +"</html>");
		title.setForeground(Color.WHITE);
		title.setOpaque(false);
		Font titlefont = title.getFont();
		title.setFont(new Font(titlefont.getName(), Font.BOLD, 36));
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
		JButton play = new JButton("Play");
		play.addActionListener(e -> {
			PlayPanel ppanel = new PlayPanel(menu, client);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(ppanel);
		});
		JButton settings = new JButton("Settings");
		settings.addActionListener(e -> {
			SettingsPanel spanel = new SettingsPanel(menu, client);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
		});
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			System.exit(0);
		});

		panel.setLayout(new BorderLayout());
		Dimension d = new Dimension(300, 50);
		play.setPreferredSize(d);
		panel.add(play, BorderLayout.NORTH);
		panel.add(settings, BorderLayout.CENTER);
		panel.add(exit, BorderLayout.SOUTH);
		return panel;
	}
}
