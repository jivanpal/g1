package Menus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ClientNetworking.Client;

public class LobbyPanel extends JPanel{
	private MainMenu menu;
	
	
	public LobbyPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Client client = new Client("player1");
		client.start();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtostart = new JButton("Back To Start");
		backtostart.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu);
			menu.changeFrame(bpanel);
		});
		add(backtostart, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		JButton inviteplayers = new JButton("Invite Players");
		add(inviteplayers, c);
		
		c.anchor = GridBagConstraints.CENTER;
	}
	
	public JPanel displayplayers() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		for (int k = 1; k < 9; k++) {
			JLabel label = new JLabel ("1.");
			
		}
		return panel;
	}
}
