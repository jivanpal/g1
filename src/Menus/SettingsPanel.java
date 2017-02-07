package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The menu when Settings is clicked in the Main Menu.
 */
public class SettingsPanel extends JPanel {
	private MainMenu menu;

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
		JButton backtomenu = new JButton("Back To Start");
		backtomenu.addActionListener(e -> {
			ButtonPanel bpanel = new ButtonPanel(menu);
			menu.changeFrame(bpanel);
		});
		add(backtomenu, c);
		JPanel bpanel = createButtons();
		c.anchor = GridBagConstraints.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
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
		gotosound.addActionListener(e -> {
			SoundPanel spanel = new SoundPanel(menu);
			menu.changeFrame(spanel);
		});
		JButton gotocontrols = new JButton("Controls");
		gotocontrols.addActionListener(e -> {
			ControlsPanel cpanel = new ControlsPanel(menu);
			menu.changeFrame(cpanel);
		});
		panel.setLayout(new BorderLayout());
		Dimension d = new Dimension(300, 50);
		gotosound.setPreferredSize(d);
		gotocontrols.setPreferredSize(d);
		panel.add(gotosound, BorderLayout.NORTH);
		panel.add(gotocontrols, BorderLayout.CENTER);
		return panel;
	}
}
