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
		
		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='24'>Player:     <font color='#66e0ff'>" + client.name + "</font></font></b></html>");
		name.setForeground(Color.WHITE);
		add(name, c);
		
		setBackground(Color.BLACK);
	}
	
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton creategame = new JButton("Create Game");
		panel.add(creategame, BorderLayout.NORTH);
		creategame = createButton(creategame, "create");
		JButton joingame = new JButton("Join Game");
		joingame = createButton(joingame, "join");
		panel.add(joingame, BorderLayout.CENTER);
		return panel;
	}
	
	public JButton createButton(JButton button, String action) {
		button.setForeground(Color.WHITE);
		button.setFont(new Font(GameOptions.REGULAR_TEXT_FONT.getName(), Font.PLAIN, 24));
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setPreferredSize(new Dimension(300, 50));
		button.addActionListener(e -> {
			switch(action) {
			case "create":
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				LobbyPanel lpanel = new LobbyPanel(menu, client, null, null, true);
				menu.changeFrame(lpanel);
				break;
			case "join":
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				JoinPanel jlpanel = new JoinPanel(menu, client);
				menu.changeFrame(jlpanel);
				break;
			}
		});
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button.setForeground(Color.GREEN);
		    }
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button.setForeground(UIManager.getColor("control"));
		    }
		});
		return button;
	}
}
