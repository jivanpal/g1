package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import ClientNetworking.IpGetter;
import ClientNetworking.GameClient.GameClient;
import GeneralNetworking.Action;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import Views.EngineerView;
import Views.PilotView;
import ClientNetworking.GameHost.*;
import GameLogic.GameOptions;

/**
 * Panel for the lobby
 * 
 * @author Jaren Chin-Hao Liu
 */
public class LobbyPanel extends JPanel implements Observer {
	private MainMenu menu;
	public Client client;
	public Player player;
	private JPanel lpanel;
	private boolean leftServer;
	private boolean isHost;
	private GridBagConstraints c;
	private static JPanel lobbypanel;

	/**
	 * Constructor of the panel.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 * @param client
	 *            The Client Thread which connects to the Server
	 */
	public LobbyPanel(MainMenu menu, UUID lobbyid, Player player, boolean ishost) {
		super();

		this.menu = menu;
		this.client = menu.client;
		if (player != null) {
			this.player = player;
		}
		leftServer = false;
		this.isHost = ishost;
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		if (lobbyid == null) {

			try {
				Lobby lobby = new Lobby(client.name, IpGetter.getRealIp());
				client.setLobby(lobby);
				client.addLobbyObserver(this);
				client.send(lobby);
				this.player = lobby.getHost();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"An error has occured while creating the game. Please check your connection!",
						"Create Game Error", JOptionPane.ERROR_MESSAGE);
				PlayPanel ppanel = new PlayPanel(menu);
				PanelsManager.changePanel(lobbypanel, ppanel, null);
				client.setLobby(null);
			}
		}
		c.anchor = GridBagConstraints.NORTHWEST;
		MyButton backtostart = new MyButton("Back");
		backtostart.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave the lobby?",
					"Leave Lobby", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				client.send(new Action(client.getLobby().getID(), this.player, this.player, Action.KICK));
				leftServer = true;
				PanelsManager.changePanel(LobbyPanel.lobbypanel, PlayPanel.ppanel, backtostart);
				client.setLobby(null);
			}
		});
		add(backtostart, c);

		if (ishost) {
			c.anchor = GridBagConstraints.SOUTH;
			c.insets = new Insets(0,0,50,0);
			MyButton startgame = new MyButton("Start Game");
			startgame.addActionListener(e -> {
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				try {
					GameHost game = new GameHost(client.getLobby());
					game.start();
					client.send(new Action(client.getLobby().getID(), this.player, Action.START));

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			add(startgame, c);
			c.insets = new Insets(0,0,0,0);
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
		JPanel ppanel = displayPlayers();
		ppanel.setOpaque(false);
		this.lpanel = ppanel;
		add(ppanel, c);

		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + client.name
				+ "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);
		setOpaque(false);
		LobbyPanel.lobbypanel = this;

	}

	/**
	 * Displays the lobby itself with the player names, move and kick button.
	 * 
	 * @return A JPanel with the lobby information
	 */
	private JPanel displayPlayers() {
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
			label.setFont(GameOptions.REGULAR_TEXT_FONT);
			if (position < 2) {
				label.setForeground(Color.decode("#ff4d4d"));
			} else if (position < 4) {
				label.setForeground(Color.decode("#ffff66"));
			} else if (position < 6) {
				label.setForeground(Color.decode("#80ff80"));
			} else {
				label.setForeground(Color.WHITE);
			}
			panel.add(label);
			if (p != null) {
				JLabel name = new JLabel(p.nickname);
				name.setFont(GameOptions.REGULAR_TEXT_FONT);

				name.setForeground(Color.WHITE);
				panel.add(name);
			} else {
				JLabel blank = new JLabel(" ");
				panel.add(blank);
			}
			MyButton move = new MyButton("Move");
			move.addActionListener(e -> {
				client.send(new Action(client.getLobby().getID(), player, position));

			});
			MyButton kick = new MyButton("Kick");
			kick.addActionListener(e -> {
				client.send(new Action(client.getLobby().getID(), player, players[position], Action.KICK));
			});
			
			if (isHost) {
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
			panel.add(move);
			panel.add(kick);
		}
		return panel;
	}

	/**
	 * Whenever the lobby changes, this method is called. This will repaint the
	 * panel and display the latest lobby information.
	 */
	@Override
	public void update(Observable o, Object arg) {
		Lobby l;
		if (client.getLobby() == null) {
			return;
		} else {
			l = client.getLobby();
			System.out.println("Lobby has started is " + l.started);
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
		if (inlobby == false && leftServer == false) {
			JOptionPane.showMessageDialog(this, "You have been kicked from the lobby!", "Kicked From Lobby",
					JOptionPane.INFORMATION_MESSAGE);
			client.deleteLobbyObserver(this);
			client.setLobby(null);
			PanelsManager.changePanel(LobbyPanel.lobbypanel, PlayPanel.ppanel, null);
			
		} else if (leftServer) {
			client.deleteLobbyObserver(this);
			client.setLobby(null);
			return;
		} else if (inlobby && l.getHost().nickname.equals(player.nickname) && !l.started) {
			System.out.println("Is a host");
			player.isHost = true;
			client.deleteLobbyObserver(this);
			JPanel templpanel = LobbyPanel.lobbypanel;
			LobbyPanel lpanel = new LobbyPanel(menu, l.getID(), player, true);
			PanelsManager.changePanel(templpanel, lpanel, null);
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

			client.disconnect();
			JFrame gameFrame = newFrame();
			menu.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			menu.getFrame().dispatchEvent(new WindowEvent(menu.getFrame(), WindowEvent.WINDOW_CLOSING));
			JPanel gameView;
			if (pos % 2 == 0) // i.e. if player is pilot
			{
				System.out.println("Player is a pilot");
				
				if (players[pos + 1] == null) {
					gameView= new PilotView(client.name, gameClient, gameFrame, true);
				} else {
					gameView = new PilotView(client.name, gameClient, gameFrame, false);
				}
				System.out.println("Swapping to PilotView");
			} else { // else player is engineer
				//PanelsManager.removeAll();
				System.out.println("Player is an Engineer");
				gameView = new EngineerView(client.name, gameClient, gameFrame);
				System.out.println("Swapping to EngineerView");
			}
			gameFrame.setContentPane(gameView);
			gameFrame.repaint();
			gameFrame.revalidate();
		} else {
			while (lpanel == null) {

			}
			this.remove(lpanel);
			JPanel newpanel = displayPlayers();
			
			newpanel.setOpaque(false);
			c.anchor = GridBagConstraints.CENTER;
			this.add(newpanel, c);
			this.invalidate();
			this.validate();
			this.lpanel = newpanel;
		}

	}
	
	private JFrame newFrame() {
		JFrame newFrame = new JFrame();
		newFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		newFrame.setUndecorated(true);
		newFrame.setVisible(true);
		newFrame.repaint();
		newFrame.revalidate();
		return newFrame;
	}
}
