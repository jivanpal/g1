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

	public SettingsPanel(MainMenu menu) {
		super();
		this.menu = menu;
		this.client = menu.client;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		MyButton backtomenu = new MyButton("Back");
		backtomenu.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(bpanel);
			bpanel.makeUI();
		});
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
		MyButton gotosound = new MyButton("Sound");
		gotosound.addActionListener(e -> {
			SoundPanel spanel = new SoundPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
		});
		MyButton gotocontrols = new MyButton("Controls");
		gotocontrols.addActionListener(e -> {
			ControlsPanel cpanel = new ControlsPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(cpanel);
		});
		panel.setLayout(new BorderLayout());
		panel.add(gotosound, BorderLayout.NORTH);
		panel.add(gotocontrols, BorderLayout.CENTER);
		return panel;
	}
}
