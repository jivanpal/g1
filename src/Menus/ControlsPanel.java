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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import GameLogic.KeyBindings;

/**
 * The Controls Menu. Menu to change key bindings for the game.
 * 
 * @author Jaren Chin-Hao Liu
 */
// TODO Make controls work Space button & Arrow keys
// TODO Bug in changing keys when you switch the keys around. I believe bug is
// in the method changeKeyByCurrentValue in KeyBindings class. Will need to
// inform Ivan to fix it.
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
		panel = repaintbuttons(panel);
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
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Fire 1":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Thrust Rev":
					keybind = KeyBindings.CURRENT_DECELERATE_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Fire 2":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Pitch Down":
					keybind = KeyBindings.CURRENT_PITCH_DOWN_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Fire 3":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Pitch Up":
					keybind = KeyBindings.CURRENT_PITCH_UP_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Manual":
					keybind = KeyBindings.CURRENT_MANUAL_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Roll Left":
					keybind = KeyBindings.CURRENT_ROLL_LEFT_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Manual Prev":
					keybind = KeyBindings.CURRENT_MANUAL_PREV_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Roll Right":
					keybind = KeyBindings.CURRENT_ROLL_RIGHT_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Manual Next":
					keybind = KeyBindings.CURRENT_MANUAL_NEXT_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				case "Overdrive":
					keybind = KeyBindings.CURRENT_OVERDRIVE_BUTTON;
					if (keybind == KeyEvent.VK_ENTER) {
						((JButton) c).setText("Enter");
					} else if (keybind == KeyEvent.VK_SPACE) {
						((JButton) c).setText("Space");
					} else {
						((JButton) c).setText(KeyEvent.getKeyText(keybind));
					}
					break;
				}
			}
		}
		return panel;
	}
	
	public boolean checkkeys (int keycode) {
		boolean keytaken = false;
		if (keycode == KeyBindings.CURRENT_ACCELERATE_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_DECELERATE_BUTTON) {	
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_PITCH_DOWN_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_PITCH_UP_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_MANUAL_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_ROLL_LEFT_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_MANUAL_PREV_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_ROLL_RIGHT_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_MANUAL_NEXT_BUTTON) {
			keytaken = true;
		} else if (keycode == KeyBindings.CURRENT_OVERDRIVE_BUTTON) {
			keytaken = true;
		}
		return keytaken;
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
				String btext = ((JButton) c).getText();
				boolean enter = false;
				boolean space = false;
				switch (btext) {
				case "Enter":
					enter = true;
					break;
				case "Space":
					space = true;
					break;
				}
				switch (button) {
				case "Thrust Fwd":
					if (enter) {
						if (checkkeys(KeyEvent.VK_ENTER)) {
							JOptionPane.showMessageDialog(this, "Key is already being used. Please use another one!", "Key Bind Error", JOptionPane.ERROR_MESSAGE);
							break;
						}
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ACCELERATE_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						if (checkkeys(KeyEvent.VK_SPACE)) {
							JOptionPane.showMessageDialog(this, "Key is already being used. Please use another one!", "Key Bind Error", JOptionPane.ERROR_MESSAGE);
							break;
						}
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ACCELERATE_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						if (checkkeys(ks.getKeyCode())) {
							JOptionPane.showMessageDialog(this, "Key is already being used. Please use another one!", "Key Bind Error", JOptionPane.ERROR_MESSAGE);
							break;
						}
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ACCELERATE_BUTTON, ks.getKeyCode());
					}
					break;
				case "Fire 1":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON,
								KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON,
								KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON, ks.getKeyCode());
					}
					break;
				case "Thrust Rev":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_DECELERATE_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_DECELERATE_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_DECELERATE_BUTTON, ks.getKeyCode());
					}
					break;
				case "Fire 2":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON,
								KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON,
								KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON, ks.getKeyCode());
					}
					break;
				case "Pitch Down":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_DOWN_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_DOWN_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_DOWN_BUTTON, ks.getKeyCode());
					}
					break;
				case "Fire 3":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON,
								KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON,
								KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON, ks.getKeyCode());
					}
					break;
				case "Pitch Up":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_UP_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_UP_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_PITCH_UP_BUTTON, ks.getKeyCode());
					}
					break;
				case "Manual":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_BUTTON, ks.getKeyCode());
					}
					break;
				case "Roll Left":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_LEFT_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_LEFT_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_LEFT_BUTTON, ks.getKeyCode());
					}
					break;
				case "Manual Prev":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_PREV_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_PREV_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_PREV_BUTTON, ks.getKeyCode());
					}
					break;
				case "Roll Right":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_RIGHT_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_RIGHT_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_ROLL_RIGHT_BUTTON, ks.getKeyCode());
					}
					break;
				case "Manual Next":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_NEXT_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_NEXT_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_MANUAL_NEXT_BUTTON, ks.getKeyCode());
					}
					break;
				case "Overdrive":
					if (enter) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_OVERDRIVE_BUTTON, KeyEvent.VK_ENTER);
					} else if (space) {
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_OVERDRIVE_BUTTON, KeyEvent.VK_SPACE);
					} else {
						char changeto = btext.charAt(0);
						KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
						KeyBindings.changeKeyByCurrentvalue(KeyBindings.CURRENT_OVERDRIVE_BUTTON, ks.getKeyCode());
					}
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
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		panel.add(label);
		JButton button = new JButton();
		button.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		button.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "none");
		button.addActionListener(e -> {
			pressed = true;

		});
		button.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (pressed) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						button.setText("Enter");
						pressed = false;
					} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						button.setText("Space");
						pressed = false;
					} else {
						button.setText("" + Character.toUpperCase(e.getKeyChar()));
						pressed = false;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		panel.add(button);
		return panel;
	}
	
	/**
	 * For Debugging purposes for changing controls
	 * 
	 * @param panel
	 *            The button panel which the controls are displayed
	 */
	public void debugging(JPanel panel) {
		String buttontopaint = "";
		for (Component c : panel.getComponents()) {
			if (c instanceof JLabel) {
				buttontopaint = ((JLabel) c).getText();

			} else if (c instanceof JButton) {
				switch (buttontopaint) {
				case "Thrust Fwd":

					int keybind = KeyBindings.CURRENT_ACCELERATE_BUTTON;
					System.out.println("Accelerate: " + KeyEvent.getKeyText(keybind));

					break;
				case "Fire 1":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_1_BUTTON;
					System.out.println("Fire 1: " + KeyEvent.getKeyText(keybind));

					break;
				case "Thrust Rev":
					keybind = KeyBindings.CURRENT_DECELERATE_BUTTON;
					System.out.println("Decelerate: " + KeyEvent.getKeyText(keybind));
					break;
				case "Fire 2":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_2_BUTTON;
					System.out.println("Fire 2: " + KeyEvent.getKeyText(keybind));
					break;
				case "Pitch Down":
					keybind = KeyBindings.CURRENT_PITCH_DOWN_BUTTON;
					System.out.println("Pitch Down: " + KeyEvent.getKeyText(keybind));
					break;
				case "Fire 3":
					keybind = KeyBindings.CURRENT_FIRE_WEAPON_3_BUTTON;
					System.out.println("Fire 3: " + KeyEvent.getKeyText(keybind));
					break;
				case "Pitch Up":
					keybind = KeyBindings.CURRENT_PITCH_UP_BUTTON;
					System.out.println("Pitch Up: " + KeyEvent.getKeyText(keybind));
					break;
				case "Roll Left":
					keybind = KeyBindings.CURRENT_ROLL_LEFT_BUTTON;
					System.out.println("Roll Left: " + KeyEvent.getKeyText(keybind));
					break;
				case "Roll Right":
					keybind = KeyBindings.CURRENT_ROLL_RIGHT_BUTTON;
					System.out.println("Roll Right: " + KeyEvent.getKeyText(keybind));
					break;
				}
			}
		}
	}
}
