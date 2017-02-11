package Menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import ClientNetworking.Client;

/**
 * PLay Menu after the 'Play' button is clicked from the Main Menu
 * 
 * @author Jaren Chin-Hao Liu
 *
 */
public class PlayPanel extends JPanel {
	private MainMenu menu;

	/**
	 * Constructor for the Panel. Displays the buttons which can be navigated to
	 * other parts of the game.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 */
	public PlayPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Client client = new Client("player1");
		client.start();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Start");
		backtostart.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu);
			menu.changeFrame(bpanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.CENTER;
		JButton creategame = new JButton("Create Game");
		creategame.setPreferredSize(new Dimension(300, 50));
		add(creategame, c);
		creategame.addActionListener(e -> {
			HostLobbyPanel lpanel = new HostLobbyPanel(menu, client);
			menu.changeFrame(lpanel);
		});
		JButton joingame = new JButton("Join Game");
		joingame.setPreferredSize(new Dimension(300, 50));
		add(joingame, c);
		setBackground(Color.BLACK);
	}
}
