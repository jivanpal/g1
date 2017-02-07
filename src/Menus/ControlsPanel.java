package Menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import GameLogic.KeyBindings;

/**
 * The Controls Menu. Menu to change key bindings for the game.
 */
public class ControlsPanel extends JPanel {
	private MainMenu menu;
	private boolean pressed;

	public ControlsPanel(MainMenu menu) {
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

		JPanel bpanel = createButtons();
		c.anchor = GridBagConstraints.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
		c.anchor = GridBagConstraints.SOUTH;
		JButton apply = new JButton("Apply");
		Dimension d = new Dimension(100, 50);
		apply.setPreferredSize(d);
		apply.addActionListener(e -> {
			changekeys(bpanel);
			repaintbuttons(bpanel);
		});
		add(apply, c);
		JButton resettodefault = new JButton("Reset To Defaults");
		resettodefault.addActionListener(e -> {
			KeyBindings kbs = new KeyBindings();
			kbs.resetKeysToDefaults();
			repaintbuttons(bpanel);
		});

		c.anchor = GridBagConstraints.NORTHEAST;
		add(resettodefault, c);

		setBackground(Color.BLACK);

	}

	/**
	 * Creates buttons and labels for each of the controls in the game.
	 * 
	 * @return A JPanel which contains those buttons in GridLayout.
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 4));
		panel = createBwL(panel, "Thrust Fwd");
		panel = createBwL(panel, "Fire 1");
		panel = createBwL(panel, "Thrust Rev");
		panel = createBwL(panel, "Fire 2");
		panel = createBwL(panel, "Pitch Down");
		panel = createBwL(panel, "Fire 3");
		panel = createBwL(panel, "Pitch Up");
		panel = createBwL(panel, "Manual");
		panel = createBwL(panel, "Roll Left");
		panel = createBwL(panel, "Manual Prev");
		panel = createBwL(panel, "Roll Right");
		panel = createBwL(panel, "Manual Next");
		panel = createBwL(panel, "Overdrive");

		return panel;

	}

	/**
	 * From the KeyBindings class, repaints the text on the buttons to reflect
	 * changes in KeyBindings.
	 * 
	 * @param panel
	 *            The panel which the control buttons are on
	 * @return The exact JPanel but with the text on the buttons changed.
	 */
	public JPanel repaintbuttons(JPanel panel) {
		String buttontopaint = "";
		for (Component c : panel.getComponents()) {
			if (c instanceof JLabel) {
				buttontopaint = ((JLabel) c).getText();

			} else if (c instanceof JButton) {
				switch (buttontopaint) {
				case "Thrust Fwd":
					int keybind = KeyBindings.CURRENT_ACCELERATE_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Fire 1":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Thrust Rev":
					keybind = KeyBindings.CURRENT_DECELERATE_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Fire 2":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Pitch Down":
					keybind = KeyBindings.CURRENT_PITCH_DOWN_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Fire 3":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Pitch Up":
					keybind = KeyBindings.CURRENT_PITCH_UP_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Roll Left":
					keybind = KeyBindings.CURRENT_ROLL_LEFT_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				case "Roll Right":
					keybind = KeyBindings.CURRENT_ROLL_RIGHT_BUTTON;
					((JButton) c).setText(KeyEvent.getKeyText(keybind));
					break;
				}
			}
		}
		return panel;
	}

	/**
	 * Makes changes to the KeyBindings class to reflect changes done in the UI.
	 * 
	 * @param panel
	 *            The panel which the control buttons are on
	 */
	public void changekeys(JPanel panel) {
		String button = "";
		for (Component c : panel.getComponents()) {
			if (c instanceof JLabel) {
				button = ((JLabel) c).getText();

			} else if (c instanceof JButton) {
				switch (button) {
				case "Thrust Fwd":
					char changeto = ((JButton) c).getText().charAt(0);
					KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ACCELERATE_BUTTON, ks.getKeyCode());
					break;
				case "Fire 1":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON, ks.getKeyCode());
					break;
				case "Thrust Rev":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_DECELERATE_BUTTON, ks.getKeyCode());
					break;
				case "Fire 2":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON, ks.getKeyCode());
					break;
				case "Pitch Down":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_DOWN_BUTTON, ks.getKeyCode());
					break;
				case "Fire 3":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON, ks.getKeyCode());
					break;
				case "Pitch Up":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_UP_BUTTON, ks.getKeyCode());
					break;
				case "Roll Left":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_LEFT_BUTTON, ks.getKeyCode());
					break;
				case "Roll Right":
					changeto = ((JButton) c).getText().charAt(0);
					ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
					KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_RIGHT_BUTTON, ks.getKeyCode());
					break;
				}
			}
		}
	}

	/**
	 * Creates a button and a label for the respective controls and adds it to
	 * the panel.
	 * 
	 * @param panel
	 *            The panel which the button and label will be added.
	 * @param name
	 *            The name of the control
	 * @return The JPanel which the button and label have been added.
	 */
	public JPanel createBwL(JPanel panel, String name) {
		// TODO make changing controls work visually
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		panel.add(label);
		JButton button = new JButton();
		int keybind = 0;
		switch (name) {
		case "Thrust Fwd":
			keybind = KeyBindings.CURRENT_ACCELERATE_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Fire 1":
			keybind = KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Thrust Rev":
			keybind = KeyBindings.CURRENT_DECELERATE_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Fire 2":
			keybind = KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Pitch Down":
			keybind = KeyBindings.CURRENT_PITCH_DOWN_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Fire 3":
			keybind = KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Pitch Up":
			keybind = KeyBindings.CURRENT_PITCH_UP_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Roll Left":
			keybind = KeyBindings.CURRENT_ROLL_LEFT_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		case "Roll Right":
			keybind = KeyBindings.CURRENT_ROLL_RIGHT_BUTTON;
			button.setText(KeyEvent.getKeyText(keybind));
			break;
		}
		button.addActionListener(e -> {
			pressed = true;

		});
		button.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (pressed) {
					button.setText("" + Character.toUpperCase(e.getKeyChar()));
					pressed = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		panel.add(button);
		return panel;
	}
}
