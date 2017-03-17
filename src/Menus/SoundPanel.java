package Menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

/**
 * The Sound Menu. Able to change the volume of the game.
 * 
 * @author Jaren Chin-Hao Liu
 */

// TODO Background is yellow but only for debugging
public class SoundPanel extends JPanel {
	private MainMenu menu;
	public Client client;

	public SoundPanel(MainMenu menu) {
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
			SettingsPanel spanel = new SettingsPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
		});
		add(backtomenu, c);
		MyButton resettodefault = new MyButton("Reset");
		resettodefault.addActionListener(e -> {
			GameOptions.resetSoundValuesToDefaults();
			SoundPanel panel = new SoundPanel(menu);
			menu.changeFrame(panel);
		});
		c.anchor = GridBagConstraints.NORTHEAST;
		add(resettodefault, c);
		JPanel spanel = createSliders();
		c.anchor = GridBagConstraints.CENTER;
		spanel.setOpaque(false);
		add(spanel, c);

		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='16'>Player:     <font color='#66e0ff'>" + client.name
				+ "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);

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
		label.setFont(GameOptions.REGULAR_TEXT_FONT);
		label.setForeground(Color.WHITE);
		panel.add(label);

		JSlider slider;
		if (AudioPlayer.isUsingMaster) {
			slider = new JSlider(-80, 0);
		} else {
			slider = new JSlider(0, Integer.valueOf(GameOptions.DEFAULT_MUSIC_VOLUME));
		}

		switch (name) {
		case "Master Volume":
			if (GameOptions.getCurrentMusicValue() == GameOptions.getCurrentSoundValue()) {
				slider.setValue((int) GameOptions.getCurrentMusicValue());
			} else {
				slider.setValue(
						AudioPlayer.isUsingMaster ? -40 : (Integer.valueOf(GameOptions.DEFAULT_MUSIC_VOLUME) / 2));
			}
			break;

		case "Sound Effects":
			slider.setValue((int) GameOptions.getCurrentSoundValue());
			break;

		case "Music Volume":
			slider.setValue((int) GameOptions.getCurrentMusicValue());
			break;

		default:
			slider.setValue(0);
		}

		slider.setPaintTicks(true);
		// slider.setForeground(Color.RED);
		slider.setBackground(Color.BLACK);
		slider.setOpaque(true);
		slider.setPreferredSize(new Dimension(200, 50));
		System.out.println("Height: " + slider.getPreferredSize().height);
		System.out.println(("Width: " + slider.getPreferredSize().width));
		slider.addChangeListener(e -> {
			float volume = (float) slider.getValue();
			if (name.equals("Master Volume")) {
				AudioPlayer.setMusicVolume(volume);
				AudioPlayer.setSoundEffectVolume(volume);
				GameOptions.changeSound(String.valueOf(volume));
				GameOptions.changeMusic(String.valueOf(volume));
				GameOptions.saveSoundValuesInFile();
			} else if (name.equals("Music Volume")) {
				AudioPlayer.setMusicVolume(volume);
				GameOptions.changeMusic(String.valueOf(volume));
				GameOptions.saveSoundValuesInFile();
			} else if (name.equals("Sound Effects")) {
				AudioPlayer.setSoundEffectVolume(volume);
				GameOptions.changeSound(String.valueOf(volume));
				GameOptions.saveSoundValuesInFile();
			}
		});
		panel.add(slider);
		return panel;
	}
}
