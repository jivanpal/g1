package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JPanel;

import Audio.AudioPlayer;
import ClientNetworking.Client;

/**
 * PLay Menu after the 'Play' button is clicked from the Main Menu
 * 
 * @author Jaren Chin-Hao Liu
 *
 */
public class PlayPanel extends JPanel {
	private MainMenu menu;
	public Client client;

	/**
	 * Constructor for the Panel. Displays the buttons which can be navigated to
	 * other parts of the game.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 */
	public PlayPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.client = client;
		//client.setLobby(null);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Start");
		backtostart.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu, client);
			menu.changeFrame(bpanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.CENTER;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, c);
		setBackground(Color.BLACK);
	}
	
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton creategame = new JButton("Create Game");
		creategame.setPreferredSize(new Dimension(300, 50));
		panel.add(creategame, BorderLayout.NORTH);
		creategame.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			HostLobbyPanel lpanel = new HostLobbyPanel(menu, client);
			menu.changeFrame(lpanel);
		});
		JButton joingame = new JButton("Join Game");
		joingame.setPreferredSize(new Dimension(300, 50));
		joingame.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			JoinPanel jlpanel = new JoinPanel(menu, client);
			menu.changeFrame(jlpanel);
		});
		panel.add(joingame, BorderLayout.CENTER);
		return panel;
	}
}
