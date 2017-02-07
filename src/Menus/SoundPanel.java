package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

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
		JPanel spanel = createSliders();
		c.anchor = c.CENTER;
		spanel.setOpaque(false);
		add(spanel, c);
		setBackground(Color.BLACK);
	}
	
	public JPanel createSliders() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel = createSwL(panel, "Master Volume");
		panel = createSwL(panel, "Music Volume");
		panel = createSwL(panel, "Sound Effects");
		return panel;
	}
	
	public JPanel createSwL(JPanel panel, String name) {
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		panel.add(label);
		JSlider slider = new JSlider(0, 100);
		//TODO sliders are not white
		slider.setValue(100);
		slider.setPaintTicks(true);
		slider.setForeground(Color.RED);
		slider.setBackground(Color.YELLOW);
		panel.add(slider);
		return panel;
	}
}
