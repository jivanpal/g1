package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

/**
 * The menu when Settings is clicked in the Main Menu.
 * 
 * @author Jaren Chin-Hao Liu
 */
public class SettingsPanel extends JPanel {
	private MainMenu menu;
	public Client client;

	public SettingsPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		this.client = client;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back");
		backtomenu = createButton(backtomenu, "Back");
		add(backtomenu, c);
		JPanel bpanel = createButtons();
		c.anchor = GridBagConstraints.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);

		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + client.name
				+ "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);

		setBackground(Color.BLACK);
	}

	/**
	 * Creates buttons for the Settings menu. Can navigate to other places such
	 * as Sound and Controls.
	 * 
	 * @return A JPanel which the buttons are added in BorderLayout.
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		JButton gotosound = new JButton("Sound");
		gotosound = createButton(gotosound, "Sound");
		JButton gotocontrols = new JButton("Controls");
		gotocontrols = createButton(gotocontrols, "Controls");
		panel.setLayout(new BorderLayout());
		panel.add(gotosound, BorderLayout.NORTH);
		panel.add(gotocontrols, BorderLayout.CENTER);
		return panel;
	}

	public JButton createButton(JButton button, String action) {
		button.setForeground(Color.WHITE);
		button.setFont(GameOptions.BUTTON_FONT);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		if (!action.equals("Back")) {
			button.setPreferredSize(new Dimension(300, 50));
		}
		button.addActionListener(e -> {
			switch (action) {
			case "Sound":
				SoundPanel spanel = new SoundPanel(menu, client);
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				menu.changeFrame(spanel);
				break;
			case "Controls":
				ControlsPanel cpanel = new ControlsPanel(menu, client);
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				menu.changeFrame(cpanel);
				break;
			case "Back":
				ButtonPanel bpanel = new ButtonPanel(menu, client);
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				menu.changeFrame(bpanel);
				break;
			}
		});
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setForeground(UIManager.getColor("control"));
			}
		});

		return button;
	}
}
