package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SoundPanel extends JPanel {
	private MainMenu menu;
	public SoundPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back To Settings");
		backtomenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsPanel spanel = new SettingsPanel(menu);
				menu.changeFrame(spanel);
			}
		});
		add(backtomenu, c);
		JButton resettodefault = new JButton("Reset To Defaults");
		c.anchor = c.NORTHEAST;
		add(resettodefault, c);
		//JPanel bpanel = createButtons();
		c.anchor = c.CENTER;
		//bpanel.setOpaque(false);
		//add(bpanel, c);
		setBackground(Color.BLACK);
	}
}
