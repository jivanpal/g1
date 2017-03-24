package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import GameLogic.GameOptions;

/**
 * The menu when Settings is clicked in the Main Menu.
 * 
 * @author Jaren Chin-Hao Liu
 */
public class SettingsPanel extends JPanel {
	private MainMenu menu;
	public static JPanel spanel;

	/**
	 * Constructor for the Settings menu.
	 * @param menu The menu to get the client data and the frame.
	 */
	public SettingsPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		MyButton backtomenu = new MyButton("Back");
		backtomenu.addActionListener(e -> {
			PanelsManager.changePanel(spanel, ButtonPanel.menuPanel, backtomenu);
		});
		add(backtomenu, c);
		JPanel bpanel = createButtons();
		c.anchor = GridBagConstraints.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);

		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + menu.client.name
				+ "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);
		setOpaque(false);
		SettingsPanel.spanel = this;
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
			SoundPanel soundpanel = new SoundPanel(menu);
			PanelsManager.changePanel(spanel, soundpanel, gotosound);
		});
		MyButton gotocontrols = new MyButton("Controls");
		gotocontrols.addActionListener(e -> {
			ControlsPanel cpanel = new ControlsPanel(menu);
			PanelsManager.changePanel(spanel, cpanel, gotocontrols);
		});
		panel.setLayout(new BorderLayout());
		panel.add(gotosound, BorderLayout.NORTH);
		panel.add(gotocontrols, BorderLayout.CENTER);
		return panel;
	}
}
