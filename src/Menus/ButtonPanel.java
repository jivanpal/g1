package Menus;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.*;
import javax.swing.event.*;



public class ButtonPanel extends JPanel {
	private MainMenu menu;
	
	public ButtonPanel (MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.CENTER;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		add(bpanel, c);
		JLabel title = new JLabel("Space Flying 101");
		title.setForeground(Color.WHITE);
		title.setOpaque(false);
		Font titlefont = title.getFont();
		title.setFont(new Font(titlefont.getName(), Font.BOLD, 36));
		c.anchor = c.NORTH;
		add(title, c);
		setBackground(Color.BLACK);
	}
	
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		JButton play = new JButton ("Play");
		JButton settings = new JButton ("Settings");
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsPanel spanel = new SettingsPanel(menu);
				menu.changeFrame(spanel);
			}
		});
		JButton exit = new JButton ("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		panel.setLayout(new BorderLayout());
		Dimension d = new Dimension (300, 50);
		play.setPreferredSize(d);
		panel.add(play, BorderLayout.NORTH);
		panel.add(settings, BorderLayout.CENTER);
		panel.add(exit, BorderLayout.SOUTH);
		return panel;
	}
}
