package Menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ClientNetworking.Client;
import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import ServerNetworking.Server;

/**
 * Panel for the host of the game
 * 
 * @author Jaren Chin-Hao Liu
 */

// TODO Invite function
public class HostLobbyPanel extends JPanel {
	private MainMenu menu;
	public Client client;
	private Player player;
	private JPanel lpanel;
	private GridBagConstraints c;

	/**
	 * Constructor of the panel.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 * @param client
	 *            The Client Thread which connects to the Server
	 */
	public HostLobbyPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		this.client = client;
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		try {
			Lobby lobby = new Lobby(client.name, InetAddress.getLocalHost());
			client.setLobby(lobby);
			client.send(lobby);
			
			player = lobby.getPlayers()[0];
		} catch (Exception e) {

		}
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Play Menu");
		backtostart.addActionListener(e -> {
			PlayPanel ppanel = new PlayPanel(menu);
			menu.changeFrame(ppanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		JButton inviteplayers = new JButton("Invite Players");
		inviteplayers.addActionListener(e -> {

		});
		add(inviteplayers, c);

		c.anchor = GridBagConstraints.CENTER;
		JPanel ppanel = displayplayers();
		ppanel.setOpaque(false);
		this.lpanel = ppanel;
		add(ppanel, c);
		setBackground(Color.BLACK);
	}

	/**
	 * Displays the lobby itself with the player names, move and kick button.
	 * 
	 * @return A JPanel with the lobby information
	 */
	public JPanel displayplayers() {
		int number = 0;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 4));
		Player[] players = client.getLobby().getPlayers();
		for (Player p : players) {
			int position = number;
			number++;
			JLabel label = new JLabel(number + ".");
			label.setForeground(Color.WHITE);
			panel.add(label);
			if (p != null) {
				JLabel name = new JLabel(p.nickname);
				name.setForeground(Color.WHITE);
				panel.add(name);
			} else {
				JLabel blank = new JLabel(" ");
				panel.add(blank);
			}
			JButton move = new JButton("Move");
			move.addActionListener(e -> {
				client.getLobby().move(this.player, position);
				this.remove(lpanel);
				JPanel newpanel = displayplayers();
				newpanel.setOpaque(false);
				this.add(newpanel, c);
				this.invalidate();
				this.validate();
				this.lpanel = newpanel;

			});
			JButton kick = new JButton("Kick");
			if (this.player.equals(p)) {
				move.setEnabled(false);
				kick.setEnabled(false);
			} else if (p == null) {
				kick.setEnabled(false);
			}
			panel.add(move);
			panel.add(kick);

		}
		return panel;
	}
}
