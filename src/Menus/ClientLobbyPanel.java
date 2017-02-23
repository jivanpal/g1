package Menus;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import Audio.AudioPlayer;
import ClientNetworking.*;
import ClientNetworking.GameClient.GameClient;
import GeneralNetworking.*;
import GeneralNetworking.Action;
import Views.*;

/**
 * Panel for the host of the game
 * 
 * @author Jaren Chin-Hao Liu
 */

// TODO Invite function
// TODO Kick function
// TODO Merge Host and client lobby panel.
public class ClientLobbyPanel extends JPanel implements Observer {
	private MainMenu menu;
	public Client client;
	private Player player;
	private JPanel lpanel;
	private boolean leftserver;
	private GridBagConstraints c;

	/**
	 * Constructor of the panel.
	 * 
	 * @param menu
	 *            The Main frame of the game
	 * @param client
	 *            The Client Thread which connects to the Server
	 */
	public ClientLobbyPanel(MainMenu menu, Client client, UUID lobbyID, Player player) {
		super();
		this.menu = menu;
		this.client = client;
		this.player = player;
		leftserver = false;
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Play Menu");
		backtostart.addActionListener(e -> {
			client.send(new Action(lobbyID, player, player, 10));
			leftserver = true;
			PlayPanel ppanel = new PlayPanel(menu, client);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(ppanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		JButton inviteplayers = new JButton("Invite Players");
		inviteplayers.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			// TODO Add panel to get recipient's nickname and send them an
			// invite
			// client.send(new Invite());
		});
		add(inviteplayers, c);

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
		for (int pos = 0; pos < players.length; pos++) {
			String role = "";
			if (pos % 2 == 0) {
				role = "Pilot";
			} else {
				role = "Engineer";
			}
			JLabel label = new JLabel(role);
			if (pos < 2) {
				label.setForeground(Color.RED);
			} else if (pos < 4) {
				label.setForeground(Color.YELLOW);
			} else if (pos < 6) {
				label.setForeground(Color.GREEN);
			} else {
				label.setForeground(Color.WHITE);
			}
			panel.add(label);
			if (players[pos] != null) {
				JLabel name = new JLabel(players[pos].nickname);
				name.setForeground(Color.WHITE);
				panel.add(name);
			} else {
				JLabel blank = new JLabel(" ");
				panel.add(blank);
			}
			JButton move = new JButton("Move");
			final int finalPos = pos;
			move.addActionListener(e -> {
				client.send(new Action(client.getLobby().getID(), player, finalPos));
			});
			JButton kick = new JButton("Kick");
			kick.setEnabled(false);
			if (players[pos] != null) {
				move.setEnabled(false);
			}
			panel.add(move);
			panel.add(kick);
		}
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		
		Lobby l = client.getLobby();
		if (l == null) {
			return;
		}
		if (l.getHost().equals(player)) {
			HostLobbyPanel hlpanel = new HostLobbyPanel(menu, client);
			menu.changeFrame(hlpanel);
			return;
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
			JOptionPane.showMessageDialog(this, "You have been kicked from the lobby!", "Kicked From Lobby", JOptionPane.INFORMATION_MESSAGE);
			ButtonPanel bpanel = new ButtonPanel(menu, client);
			menu.changeFrame(bpanel);
		} else if (l.started)
		{
			// Player[] players = l.getPlayers();
			int pos = 0;
			while (pos < players.length) {
				if (player.equals(players[pos]))
					break;
				pos++;
			}
			System.out.println(pos);
			GameClient gameClient = new GameClient(l,player);
			if (pos % 2 == 0) // i.e. if player is pilot
			{

				PilotView pv = new PilotView(client.name, gameClient);
				menu.changeFrame(pv);
			} else // else player is engineer
			{
				EngineerView eview = new EngineerView(client.name, gameClient);
				menu.changeFrame(eview);
			}
		} else

		{
			while (lpanel == null) {
				
			}
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
