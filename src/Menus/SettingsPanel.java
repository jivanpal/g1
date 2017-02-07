package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SettingsPanel extends JPanel {
	private MainMenu menu;
	public SettingsPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back To Start");
		backtomenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ButtonPanel bpanel = new ButtonPanel(menu);
				menu.changeFrame(bpanel);
			}
		});
		add(backtomenu, c);
		JPanel bpanel = createButtons();
		c.anchor = c.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
		setBackground(Color.BLACK);
	}
	
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		JButton gotosound = new JButton("Sound");
		gotosound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SoundPanel spanel = new SoundPanel(menu);
				menu.changeFrame(spanel);
			}
		});
		JButton gotocontrols = new JButton("Controls");
		gotocontrols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControlsPanel cpanel = new ControlsPanel(menu);
				menu.changeFrame(cpanel);
			}
		});
		panel.setLayout(new BorderLayout());
		panel.add(gotosound, BorderLayout.NORTH);
		panel.add(gotocontrols, BorderLayout.CENTER);
		return panel;
	}
}
