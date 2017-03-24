package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import GameLogic.GameOptions;

/**
 * Play Menu after the 'Play' button is clicked from the Main Menu
 * 
 * @author Jaren Chin-Hao Liu
 *
 */
public class PlayPanel extends JPanel {
	private MainMenu menu;
	public static JPanel ppanel;
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
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		MyButton backtostart = new MyButton("Back");
		backtostart.addActionListener(e -> {
			PanelsManager.changePanel(ppanel, ButtonPanel.menuPanel, backtostart);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.CENTER;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, c);
		
		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + menu.client.name + "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);
		setOpaque(false);
		PlayPanel.ppanel = this;
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
			LobbyPanel lpanel = new LobbyPanel(menu, null, null, true);
			PanelsManager.changePanel(ppanel, lpanel, creategame);
		});
		MyButton joingame = new MyButton("Join Game");
		joingame.addActionListener(e -> {
			JoinPanel jlpanel = new JoinPanel(menu);
			PanelsManager.changePanel(ppanel, jlpanel, joingame);
		});
		panel.add(creategame, BorderLayout.NORTH);
		panel.add(joingame, BorderLayout.CENTER);
		return panel;
	}
}
