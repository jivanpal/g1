package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import ClientNetworking.IpGetter;
import GameLogic.GameOptions;
import GeneralNetworking.Action;
import GeneralNetworking.LobbyInfo;
import GeneralNetworking.Player;

/**
 * Join Panel. Displays lobbies on the server and can choose which one to join.
 * 
 * @author Jaren Chin-Hao Liu
 *
 */

public class JoinPanel extends JPanel {
	private MainMenu menu;
	public Client client;
	private JTable table;
	private DefaultTableModel model;
	private LobbyInfo[] lobbies;
	public static JPanel jpanel;

	/**
	 * Constructor for the panel.
	 * 
	 * @param menu
	 *            The main frame of the game
	 * @param client
	 *            The client thread which is connected to the server.
	 */
	public JoinPanel(MainMenu menu) {
		super();
		this.menu = menu;
		this.client = menu.client;
		setLayout(new BorderLayout());
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Lobby ID");
		model.addColumn("Host");
		model.addColumn("Players");
		client.updateList();
		keepUpdating();
		if (client.getLobbyList().getLobbies().length == 0) {
			JOptionPane.showMessageDialog(this, "No lobbies are found!", "Lobbies Not Found Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			repaintLobbies();
		}
		table = new JTable(model);
		table.setOpaque(true);
		table.setFillsViewportHeight(true);

		table.setBackground(Color.BLACK);
		table.setForeground(Color.WHITE);
		table.setFont(GameOptions.REGULAR_TEXT_FONT);

		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(GameOptions.REGULAR_TEXT_FONT);
		table.setSelectionBackground(Color.decode("#999999"));
		table.setDefaultRenderer(Object.class, new MyTableRenderer());

		JScrollPane pane = new JScrollPane(table);
		pane.setBorder(BorderFactory.createLineBorder(Color.decode("#333333")));
		add(pane, BorderLayout.CENTER);
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, BorderLayout.SOUTH);
		setOpaque(false);
		JoinPanel.jpanel = this;
	}

	/**
	 * Makes the table not have a focus border when clicked on.
	 * 
	 * @author Jaren Liu
	 *
	 */
	public class MyTableRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setBorder(noFocusBorder);
			return this;
		}
	}

	/**
	 * Waits for updates to the list until there's a lobby or 3 seconds have
	 * passed
	 */
	private void keepUpdating() {
		long starttime = System.currentTimeMillis();
		while (client.getLobbyList().getLobbies().length == 0 && (System.currentTimeMillis() - starttime) < 3000) {

		}
	}

	/**
	 * Waits for updates to the list until 3 seconds have passed
	 */
	private void keepUpdatingTime() {
		long starttime = System.currentTimeMillis();
		while (false || (System.currentTimeMillis() - starttime) < 3000) {

		}
	}

	/**
	 * Repaints the scrollpane with the updated lobby list
	 */
	private void repaintLobbies() {
		lobbies = client.getLobbyList().getLobbies();
		for (int i = model.getRowCount() - 1; i > -1; i--) {
			model.removeRow(i);
		}

		for (LobbyInfo lobby : lobbies) {
			if (lobby == null) {
				break;
			}
			System.out.println("lobby not null");
			UUID id = lobby.lobbyID;
			String host = lobby.host;
			int number = lobby.playerCount;
			Object[] row = { id, host, number + "/8" };
			model.addRow(row);
		}
	}

	/**
	 * Create the panel which contains buttons for joining, refreshing, and
	 * going back to the play menu.
	 * 
	 * @return A JPanel with Join, Refresh and Back to Play Menu buttons added
	 *         to it
	 */
	private JPanel createButtons() {
		JPanel panel = new JPanel() {
			@Override
			public Insets getInsets() {
				return new Insets(0,0,50,0);
			}
		};
		panel.setLayout(new BorderLayout());
		
		
		MyButton backtoplay = new MyButton("Back");
		backtoplay.addActionListener(e -> {
			PanelsManager.changePanel(JoinPanel.jpanel, PlayPanel.ppanel, backtoplay);
		});
		MyButton join = new MyButton("Join");
		join.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			int selected = table.getSelectedRow();
			LobbyInfo lInfo = lobbies[selected];
			try {
				Player player = new Player(client.name, IpGetter.getRealIp(), false);
				client.send(new Action(lInfo.lobbyID, player, Action.ADD));
				LobbyPanel lpanel2 = new LobbyPanel(menu, lInfo.lobbyID, player, false);
				PanelsManager.changePanel(JoinPanel.jpanel, lpanel2, join);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Joining Lobby failed. Please check your connection!",
						"Join Lobby Error", JOptionPane.ERROR_MESSAGE);
				JoinPanel jpanel = new JoinPanel(menu);
				PanelsManager.changePanel(JoinPanel.jpanel, jpanel, join);
				e1.printStackTrace();
			}
		});
		MyButton refresh = new MyButton("Refresh");
		refresh.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			client.updateList();
			keepUpdatingTime();
			repaintLobbies();
			
		});
		join.setPreferredSize(new Dimension(500, 50));
		refresh.setPreferredSize(new Dimension(230, 50));
		backtoplay.setPreferredSize(new Dimension(200, 50));
		panel.add(backtoplay, BorderLayout.WEST);
		panel.add(join, BorderLayout.CENTER);
		panel.add(refresh, BorderLayout.EAST);
		return panel;
	}
}
