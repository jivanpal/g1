package Menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ClientNetworking.Client;
import ClientNetworking.GameClient.GameClient;
import GeneralNetworking.Action;
import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import ServerNetworking.Server;
import Views.EngineerView;
import Views.PilotView;
import ClientNetworking.GameHost.*;
/**
 * Panel for the host of the game
 * 
 * @author Jaren Chin-Hao Liu
 */

// TODO Invite function
// TODO Kick function
public class HostLobbyPanel extends JPanel implements Observer {
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
			client.addLobbyObserver(this);
			client.send(lobby);
			player = lobby.getHost();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"An error has occured while creating the game. Please check your connection!", "Create Game Error",
					JOptionPane.ERROR_MESSAGE);
			PlayPanel ppanel = new PlayPanel(menu, client);
			menu.changeFrame(ppanel);
		}
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Play Menu");
		backtostart.addActionListener(e -> {
			PlayPanel ppanel = new PlayPanel(menu, client);
			menu.changeFrame(ppanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		JButton inviteplayers = new JButton("Invite Players");
		inviteplayers.addActionListener(e -> {

		});
		add(inviteplayers, c);

		c.anchor = GridBagConstraints.SOUTH;
		JButton startgame = new JButton("Start Game");
		startgame.addActionListener(e -> {
			try {
				MapServer game = new MapServer(client.getLobby());
				game.start();
				client.send(new Action(client.getLobby().getID(), player, 11));

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});
		startgame.setPreferredSize(new Dimension(300, 50));
		add(startgame, c);

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
			if (p == null) {
				kick.setEnabled(false);
			} else if (this.player.nickname.equals(p.nickname)) {
				move.setEnabled(false);
				kick.setEnabled(false);
			} else {
				move.setEnabled(false);
			}

/*			kick.addActionListener(e -> {
				client.send(new Action(,10));
			});*/
			panel.add(move);
			panel.add(kick);
		}
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Lobby starting ID: " + client.getLobby().getID());
		System.out.println("Lobby started is " + client.getLobby().started);
		if (client.getLobby().started) {
			Player[] players = client.getLobby().getPlayers();
			int pos = 0;
			while (pos < players.length)
			{
				System.out.println("pos = ");
				if(player.equals(players[pos]))
					break;
				pos++;
			}
			System.out.println(pos);
			GameClient gameClient = new GameClient(client.getLobby());
			gameClient.start();
			if(pos % 2 == 0)	// i.e. if player is pilot
			{
				PilotView pv = new PilotView(client.name, gameClient);
				menu.changeFrame(pv);
			}
			else		// else player is engineer
			{
				EngineerView eview = new EngineerView(client.name, gameClient);
				menu.changeFrame(eview);
			}
		} else {
			this.remove(lpanel);
			JPanel newpanel = displayplayers();
			newpanel.setOpaque(false);
			this.add(newpanel, c);
			this.invalidate();
			this.validate();
			this.lpanel = newpanel;
		}

	}
}
