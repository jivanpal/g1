package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ClientNetworking.Client;
import GeneralNetworking.Action;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyInfo;
import GeneralNetworking.LobbyList;
import GeneralNetworking.Player;
import ServerNetworking.Server;

/**
 * Join Panel. Displays lobbies on the server and can choose which one to join.
 * 
 * @author Jaren Chin-Hao Liu
 *
 */

// TODO Joining function. Erroring when trying to display the clientlobbypanel.
public class JoinPanel extends JPanel implements Observer {
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
		model = new DefaultTableModel();
		model.addColumn("Lobby ID");
		model.addColumn("Host");
		model.addColumn("Players");
		client.updateList();
		keepupdating();
		if (client.getLobbyList().getLobbies().length == 0) {
			JOptionPane.showMessageDialog(this, "No lobbies are found!", "Lobbies Not Found Error", JOptionPane.ERROR_MESSAGE);
		} else {
			repaintlobbies();
		}
		
		//System.out.println("lobby 1 host" + lobbies[0].host);
		//System.out.println(Server.lobbies);
		System.out.println("Finished updating");
		//System.out.println(Server.lobbies);
		//ArrayList<Lobby> lobbies2 = Server.lobbies;
		table = new JTable(model);
		JScrollPane pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, BorderLayout.SOUTH);
		setBackground(Color.black);
	}
	
	public void keepupdating() {
		long starttime = System.currentTimeMillis();
		while(client.getLobbyList().getLobbies().length == 0 && (System.currentTimeMillis()-starttime)<3000){
			
		}
	}
	
	public void keepupdatingtime() {
		long starttime = System.currentTimeMillis();
		while(false || (System.currentTimeMillis()-starttime)<3000){
			
		}
	}
	
	public void repaintlobbies() {
		lobbies = client.getLobbyList().getLobbies();
		for (int i = model.getRowCount()-1; i > -1; i--) {
			model.removeRow(i);
		}
		for (LobbyInfo lobby : lobbies) {
			if(lobby==null) {
				break;
			}
			System.out.println("lobby not null");
			UUID id = lobby.lobbyID;
			String host = lobby.host;
			int number = lobby.playerCount;
			Object[] row = { id, host, number+"/8" };
			model.addRow(row);
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
			PlayPanel ppanel = new PlayPanel(menu, client);
			menu.changeFrame(ppanel);
		});
		JButton join = new JButton("Join");
		join.addActionListener(e -> {
			int selected = table.getSelectedRow();
			LobbyInfo lInfo = lobbies[selected];
			try
			{
				Player player = new Player(client.name, InetAddress.getLocalHost(), false);
				client.send(new Action(lInfo.lobbyID,player,9));
				//Error here since when creating the clientlobbypanel, the client's lobby isn't set so throws a nullpointerexception.
				ClientLobbyPanel clpanel = new ClientLobbyPanel(menu, client, lInfo.lobbyID, player);
				menu.changeFrame(clpanel);
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(this, "Joining Lobby failed. Please check your connection!", "Join Lobby Error", JOptionPane.ERROR_MESSAGE);
				JoinPanel jpanel = new JoinPanel(menu, client);
				menu.changeFrame(jpanel);
				e1.printStackTrace();
			}
		});
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(e -> {
			client.updateList();
			keepupdatingtime();
			repaintlobbies();
		});
		join.setPreferredSize(new Dimension(500, 50));
		refresh.setPreferredSize(new Dimension(200, 50));
		backtoplay.setPreferredSize(new Dimension(200, 50));
		panel.add(backtoplay, BorderLayout.WEST);
		panel.add(join, BorderLayout.CENTER);
		panel.add(refresh, BorderLayout.EAST);
		return panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		// Should update the UI here !!!!
		
	}

}
