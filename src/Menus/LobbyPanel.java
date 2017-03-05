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
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Audio.AudioPlayer;
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
// TODO Merge host and client lobby panel
public class LobbyPanel extends JPanel implements Observer {
	private MainMenu menu;
	public Client client;
	public Player player;
	private JPanel lpanel;
	private boolean leftserver;
	private boolean ishost;
	private GridBagConstraints c;

	/**
	 * Constructor of the panel.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 * @param client
	 *            The Client Thread which connects to the Server
	 */
	public LobbyPanel(MainMenu menu, Client client, UUID lobbyid, Player player, boolean ishost) {
		super();

		this.menu = menu;
		this.client = client;
		if (player != null) {
			this.player = player;
		}
		leftserver = false;
		this.ishost = ishost;
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		if (ishost) {
			JLabel host = new JLabel("Host");
			host.setForeground(Color.WHITE);
			add(host, c);
		}

		if (lobbyid == null) {

			try {
				Lobby lobby = new Lobby(client.name, InetAddress.getLocalHost());
				client.setLobby(lobby);
				client.addLobbyObserver(this);
				client.send(lobby);
				this.player = lobby.getHost();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"An error has occured while creating the game. Please check your connection!",
						"Create Game Error", JOptionPane.ERROR_MESSAGE);
				PlayPanel ppanel = new PlayPanel(menu, client);
				menu.changeFrame(ppanel);
				client.setLobby(null);
			}
		}
		/*
		 * System.out.println(client.getLobby().getID()); System.out.println(
		 * "Changed from clientlobby to hostlobby"); player =
		 * client.getLobby().getHost();
		 * 
		 * }
		 */
		c.anchor = GridBagConstraints.NORTHWEST;
		JButton backtostart = new JButton("Back To Play Menu");
		backtostart.addActionListener(e -> {
			client.send(new Action(client.getLobby().getID(), this.player, this.player, Action.KICK));
			leftserver = true;
			PlayPanel ppanel = new PlayPanel(menu, client);
			menu.changeFrame(ppanel);
			client.setLobby(null);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		JButton inviteplayers = new JButton("Invite Players");
		inviteplayers.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
		});
		add(inviteplayers, c);

		if (ishost) {
			c.anchor = GridBagConstraints.SOUTH;
			JButton startgame = new JButton("Start Game");
			startgame.addActionListener(e -> {
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				try {
					MapServer game = new MapServer(client.getLobby());
					game.start();
					client.send(new Action(client.getLobby().getID(), this.player, Action.START));

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			});
			startgame.setPreferredSize(new Dimension(300, 50));
			add(startgame, c);
		}

		c.anchor = GridBagConstraints.CENTER;
		while (client.getLobby() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		client.addLobbyObserver(this);
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
			String role = "";
			number++;
			if (position % 2 == 0) {
				role = "Pilot";
			} else {
				role = "Engineer";
			}
			JLabel label = new JLabel(role);
			if (position < 2) {
				label.setForeground(Color.RED);
			} else if (position < 4) {
				label.setForeground(Color.YELLOW);
			} else if (position < 6) {
				label.setForeground(Color.GREEN);
			} else {
				label.setForeground(Color.WHITE);
			}
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
				client.send(new Action(client.getLobby().getID(), player, position));

			});
			JButton kick = new JButton("Kick");
			kick.addActionListener(e -> {
				client.send(new Action(client.getLobby().getID(), player, players[position], Action.KICK));
			});
			if (ishost) {
				if (p == null) {
					kick.setEnabled(false);
				} else if (this.player.nickname.equals(p.nickname)) {
					move.setEnabled(false);
					kick.setEnabled(false);
				} else {
					move.setEnabled(false);
				}
			} else {
				kick.setEnabled(false);
				if (players[position] != null) {
					move.setEnabled(false);
				}
			}
			/*
			 * kick.addActionListener(e -> { client.send(new Action(,10)); });
			 */
			panel.add(move);
			panel.add(kick);
		}
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		Lobby l;
		if (client.getLobby() == null) {
			return;
		} else {
			l = client.getLobby();
			System.out.println("Lobby has started is "+l.started);
		}
		Player[] players = l.getPlayers();
		boolean inlobby = false;
		for (Player p : players) {
			if (p == null) {
				continue;
			} else if (p.nickname.equals(client.name)) {
				inlobby = true;
				break;
			}
		}
		if (inlobby == false && leftserver == false) {
			JOptionPane.showMessageDialog(this, "You have been kicked from the lobby!", "Kicked From Lobby",
					JOptionPane.INFORMATION_MESSAGE);
			ButtonPanel bpanel = new ButtonPanel(menu, client);
			menu.changeFrame(bpanel);
			client.setLobby(null);
		} else if (leftserver) {
			return;
		} else if (inlobby && l.getHost().nickname.equals(player.nickname) && !l.started) {
			player.isHost = true;
			LobbyPanel lpanel = new LobbyPanel(menu, client, l.getID(), player, true);
			menu.changeFrame(lpanel);
		} else if (l.started) {
			int pos = 0;
			while (pos < players.length) {
				if (player.equals(players[pos]))
					break;
				pos++;
			}
			System.out.println(pos);
			GameClient gameClient = new GameClient(client.getLobby(), player);
			System.out.println("Pos: " + String.valueOf(pos % 2 == 0));
			if (pos % 2 == 0) // i.e. if player is pilot
			{
				System.out.println("Player is a pilot");
				PilotView pv;
				if (players[pos + 1] == null) {
					pv = new PilotView(client.name, gameClient, menu.getFrame(), true);
				} else {
					pv = new PilotView(client.name, gameClient, menu.getFrame(), false);
				}
				System.out.println("Swapping to PilotView");
				menu.changeFrame(pv);
			} else // else player is engineer
			{
				System.out.println("Player is an Engineer");
				EngineerView eview = new EngineerView(client.name, gameClient, menu.getFrame());
				System.out.println("Swapping to EngineerView");
				menu.changeFrame(eview);
			}
		} else {
			while (lpanel == null) {

			}
			//if (player.nickname.equals(l.getHost().nickname)) {
				//LobbyPanel lpanel2 = new LobbyPanel(menu, client, l.getID(), player, true);
				//menu.changeFrame(lpanel2);
			//} else {
				this.remove(lpanel);
				JPanel newpanel = displayplayers();
				newpanel.setOpaque(false);
				this.add(newpanel, c);
				this.invalidate();
				this.validate();
				this.lpanel = newpanel;
			//}
		}

	}
}
