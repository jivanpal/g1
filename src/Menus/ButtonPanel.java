package Menus;

import javax.swing.JPanel;

import ClientNetworking.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
		String name = JOptionPane.showInputDialog(this, "Please Enter your username: ", "Input Username", JOptionPane.PLAIN_MESSAGE);
		while (name.equals("") || name == null) {
			name = JOptionPane.showInputDialog(this, "Please Enter your username: ", "Input Username", JOptionPane.PLAIN_MESSAGE);
		}
		Client client = new Client(name);
		client.start();
		client.updateList();
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
		JLabel title = new JLabel("<html>Space Flying 101<br><br>Welcome " + name +"</html>");
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
			menu.changeFrame(ppanel);
		});
		JButton settings = new JButton("Settings");
		settings.addActionListener(e -> {
			SettingsPanel spanel = new SettingsPanel(menu);
			menu.changeFrame(spanel);
		});
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> {
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
