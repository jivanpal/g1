package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

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
	public PlayPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.client = menu.client;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		MyButton backtostart = new MyButton("Back");
		backtostart.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(bpanel);
			bpanel.makeUI();
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.CENTER;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, c);
		
		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + client.name + "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);
		
		setBackground(Color.BLACK);
	}
	
	/**
	 * Creates the buttons for the play menu
	 * @return Returns a JPanel with the buttons on it
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		MyButton creategame = new MyButton("Create Game");
		creategame.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			LobbyPanel lpanel = new LobbyPanel(menu, null, null, true);
			menu.changeFrame(lpanel);
		});
		MyButton joingame = new MyButton("Join Game");
		joingame.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			JoinPanel jlpanel = new JoinPanel(menu);
			menu.changeFrame(jlpanel);
		});
		panel.add(creategame, BorderLayout.NORTH);
		panel.add(joingame, BorderLayout.CENTER);
		return panel;
	}
}
