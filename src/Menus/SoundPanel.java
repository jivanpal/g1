package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * The Sound Menu. Able to change the volume of the game.
 */
public class SoundPanel extends JPanel {
	private MainMenu menu;

	public SoundPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back To Settings");
		backtomenu.addActionListener(e -> {
			SettingsPanel spanel = new SettingsPanel(menu);
			menu.changeFrame(spanel);
		});
		add(backtomenu, c);
		JButton resettodefault = new JButton("Reset To Defaults");
		c.anchor = GridBagConstraints.NORTHEAST;
		add(resettodefault, c);
		JPanel spanel = createSliders();
		c.anchor = GridBagConstraints.CENTER;
		spanel.setOpaque(false);
		add(spanel, c);
		setBackground(Color.BLACK);
	}

	/**
	 * Create the sliders and labels for the three categories in sounds. These
	 * sliders are used to change the volume of the game.
	 * 
	 * @return A JPanel which have the slider and labels added.
	 */
	public JPanel createSliders() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel = createSwL(panel, "Master Volume");
		panel = createSwL(panel, "Music Volume");
		panel = createSwL(panel, "Sound Effects");
		return panel;
	}

	/**
	 * Create a single slider with a label with the specified name on the
	 * specified panel.
	 * 
	 * @param panel
	 *            The panel for which the slider and label will be added.
	 * @param name
	 *            The name of the label and what the slider will be changing.
	 * @return The JPanel with the slider and label added.
	 */
	public JPanel createSwL(JPanel panel, String name) {
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		panel.add(label);
		JSlider slider = new JSlider(0, 100);
		// TODO sliders are not white and are not visible
		slider.setValue(100);
		slider.setPaintTicks(true);
		slider.setForeground(Color.RED);
		slider.setBackground(Color.YELLOW);
		panel.add(slider);
		return panel;
	}
}
