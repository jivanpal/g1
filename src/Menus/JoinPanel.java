package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ClientNetworking.Client;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyInfo;
import GeneralNetworking.LobbyList;
import ServerNetworking.Server;

/**
 * Join Panel. Displays lobbies on the server and can choose which one to join.
 * 
 * @author Jaren Chin-Hao Liu
 *
 */

// TODO JoinPanel still not yet linked with networking. Errors in sending objects over the network.
public class JoinPanel extends JPanel {
	private MainMenu menu;
	public Client client;
	private JTable table;
	private DefaultTableModel model;
	private LobbyInfo[] lobbies;

	/**
	 * Constructor for the panel.
	 * 
	 * @param menu
	 *            The main frame of the game
	 * @param client
	 *            The client thread which is connected to the server.
	 */
	public JoinPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		this.client = client;
		setLayout(new BorderLayout());
		client.updateList();
		client.printsmth();
		lobbies = client.lobbyList.getLobbies();
		System.out.println("Updating");
		System.out.println(Server.lobbies);
		//keepupdating();
		System.out.println("Finished updating");
		System.out.println(Server.lobbies);
		
		
		//ArrayList<Lobby> lobbies2 = Server.lobbies;
		model = new DefaultTableModel();
		model.addColumn("Lobby ID");
		model.addColumn("Host");
		model.addColumn("Players");
		for (LobbyInfo lobby : lobbies) {
			UUID id = lobby.lobbyID;
			String host = lobby.host;
			int number = lobby.playerCount;
			Object[] row = { id, host, number+"/8" };
			model.addRow(row);
		}
		table = new JTable(model);
		JScrollPane pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, BorderLayout.SOUTH);
		setBackground(Color.black);
	}
	
	public void keepupdating() {
		while (client.lobbyList == null) {
			
		}
	}
	/**
	 * Create the buttons for joining, refreshing, and going back to the play
	 * menu.
	 * 
	 * @return A JPanel with Join, Refresh and Back to Play Menu buttons added
	 *         to it
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JButton backtoplay = new JButton("Back To Play Menu");
		backtoplay.addActionListener(e -> {
			PlayPanel ppanel = new PlayPanel(menu);
			menu.changeFrame(ppanel);
		});
		JButton join = new JButton("Join");
		JButton refresh = new JButton("Refresh");
		join.setPreferredSize(new Dimension(500, 50));
		refresh.setPreferredSize(new Dimension(200, 50));
		backtoplay.setPreferredSize(new Dimension(200, 50));
		panel.add(backtoplay, BorderLayout.WEST);
		panel.add(join, BorderLayout.CENTER);
		panel.add(refresh, BorderLayout.EAST);
		return panel;
	}

}
