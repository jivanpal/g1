package Menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.GameOptions;

/**
 * The Controls Menu. Menu to change key bindings for the game.
 * 
 * @author Jaren Chin-Hao Liu
 */
// TODO Make controls work Arrow keys

public class ControlsPanel extends JPanel {
	private MainMenu menu;
	private boolean pressed;
	public Client client;

	public ControlsPanel(MainMenu menu, Client client) {
		super();
		this.menu = menu;
		this.client = client;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back");
		backtomenu = createButton(backtomenu, "Back", null);
		add(backtomenu, c);

		JPanel bpanel = createButtons();
		c.anchor = GridBagConstraints.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
		c.anchor = GridBagConstraints.SOUTH;
		JButton apply = new JButton("Apply");
		
		apply = createButton(apply, "Apply", bpanel);
		add(apply, c);
		JButton resettodefault = new JButton("Reset");
		resettodefault = createButton(resettodefault, "Reset", bpanel);

		c.anchor = GridBagConstraints.NORTHEAST;
		add(resettodefault, c);
		
		c.anchor = GridBagConstraints.NORTH;
		JLabel name = new JLabel("<html><b><font size='20'>Player:     <font color='#66e0ff'>" + client.name + "</font></font></b></html>");
		name.setFont(GameOptions.REGULAR_TEXT_FONT);
		name.setForeground(Color.WHITE);
		add(name, c);
		
		setBackground(Color.BLACK);

	}

	public JButton createButton(JButton button, String action, JPanel bpanel) {
		button.setForeground(Color.WHITE);
		button.setFont(GameOptions.BUTTON_FONT);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusable(false);
		if (action.equals("Back") || action.equals("Reset")) {
			
		} else {
			button.setPreferredSize(new Dimension(300, 50));
		}
		button.addActionListener(e -> {
			switch (action) {
			case "Back":
				SettingsPanel spanel = new SettingsPanel(menu, client);
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				menu.changeFrame(spanel);
				break;
			case "Reset":
				GameOptions.resetKeysToDefaults();
				GameOptions.saveKeyBindingsInFile();
				AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
				changebuttons(bpanel, true);
				break;
			case "Apply":
				changebuttons(bpanel, false);
				changebuttons(bpanel, true);
				GameOptions.saveKeyBindingsInFile();
				JOptionPane.showMessageDialog(this, "Changes have been applied!", "Changes Applied",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		});
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button.setForeground(Color.GREEN);
		    }
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button.setForeground(UIManager.getColor("control"));
		    }
		});
		return button;
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
		panel = changebuttons(panel, true);
		return panel;

	}

	/**
	 * Displays a message stating the key has already been taken
	 */
	public void displaykeyerror() {
		JOptionPane.showMessageDialog(this, "Key is already being used. Please use another one!", "Key Bind Error",
				JOptionPane.ERROR_MESSAGE);
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
						String btext = button.getText();
						button.setText("Enter");
						if (GameOptions.checkIfKeyTaken(e.getKeyCode())) {
							displaykeyerror();
							button.setText(btext);
							pressed = false;
							return;
						}
						pressed = false;
					} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						String btext = button.getText();
						button.setText("Space");
						if (GameOptions.checkIfKeyTaken(e.getKeyCode())) {
							displaykeyerror();
							button.setText(btext);
							pressed = false;
							return;
						}
						pressed = false;
					} else {
						String btext = button.getText();
						button.setText("" + Character.toUpperCase(e.getKeyChar()));
						if (GameOptions.checkIfKeyTaken(e.getKeyCode())) {
							displaykeyerror();
							button.setText(btext);
							pressed = false;
							return;
						}
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
	 * Repaints the key given the key binding
	 * 
	 * @param currentkey
	 *            The key in which you want to paint onto the button
	 * @param button
	 *            The button to be painted
	 */
	public void repaintkey(String currentkey, JButton button) {
		int keybind = GameOptions.getCurrentKeyValueByDefault(currentkey);
		if (keybind == KeyEvent.VK_ENTER) {
			button.setText("Enter");
		} else if (keybind == KeyEvent.VK_SPACE) {
			button.setText("Space");
		} else {
			button.setText(KeyEvent.getKeyText(keybind));
		}
	}

	/**
	 * Changes a specific key to the new key value
	 * 
	 * @param currentkey
	 *            The current key binding which you want to change
	 * @param button
	 *            The button where we get the new value from
	 */
	public void changekey(String currentkey, JButton button) {
		String btext = button.getText();
		if (btext.equals("Enter")) {
			GameOptions.changeKeyByDefaultValue(currentkey, KeyEvent.VK_ENTER);
		} else if (btext.equals("Space")) {
			GameOptions.changeKeyByDefaultValue(currentkey, KeyEvent.VK_SPACE);
		} else {
			char changeto = btext.charAt(0);
			KeyStroke ks = KeyStroke.getKeyStroke(Character.toUpperCase(changeto), 0);
			GameOptions.changeKeyByDefaultValue(currentkey, ks.getKeyCode());
		}
	}

	/**
	 * Either changes key or repaints based on the boolean
	 * @param key Key which you want to repaint or change
	 * @param button Button which you want to repaint
	 * @param repaint Boolean indicating whether to repaint or change the key
	 */
	public void changerepaint(String key, JButton button, boolean repaint) {
		if (repaint) {
			repaintkey(key, button);
		} else {
			changekey(key, button);
		}
	}

	/**
	 * From the KeyBindings class, repaints the text on the buttons to reflect
	 * changes in KeyBindings.
	 * 
	 * @param panel
	 *            The panel which the control buttons are on
	 * @return The exact JPanel but with the text on the buttons changed.
	 */
	public JPanel changebuttons(JPanel panel, boolean repaint) {
		String buttontopaint = "";
		for (Component c : panel.getComponents()) {
			if (c instanceof JLabel) {
				buttontopaint = ((JLabel) c).getText();
			} else if (c instanceof JButton) {
				switch (buttontopaint) {
				case "Thrust Fwd":
					changerepaint(GameOptions.DEFAULT_ACCELERATE_BUTTON, (JButton) c, repaint);
					break;
				case "Fire 1":
					changerepaint(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON, (JButton) c, repaint);
					break;
				case "Thrust Rev":
					changerepaint(GameOptions.DEFAULT_DECELERATE_BUTTON, (JButton) c, repaint);
					break;
				case "Fire 2":
					changerepaint(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON, (JButton) c, repaint);
					break;
				case "Pitch Down":
					changerepaint(GameOptions.DEFAULT_PITCH_DOWN_BUTTON, (JButton) c, repaint);
					break;
				case "Fire 3":
					changerepaint(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON, (JButton) c, repaint);
					break;
				case "Pitch Up":
					changerepaint(GameOptions.DEFAULT_PITCH_UP_BUTTON, (JButton) c, repaint);
					break;
				case "Manual":
					changerepaint(GameOptions.DEFAULT_MANUAL_BUTTON, (JButton) c, repaint);
					break;
				case "Roll Left":
					changerepaint(GameOptions.DEFAULT_ROLL_LEFT_BUTTON, (JButton) c, repaint);
					break;
				case "Manual Prev":
					changerepaint(GameOptions.DEFAULT_MANUAL_PREV_BUTTON, (JButton) c, repaint);
					break;
				case "Roll Right":
					changerepaint(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON, (JButton) c, repaint);
					break;
				case "Manual Next":
					changerepaint(GameOptions.DEFAULT_MANUAL_NEXT_BUTTON, (JButton) c, repaint);
					break;
				case "Overdrive":
					changerepaint(GameOptions.DEFAULT_OVERDRIVE_BUTTON, (JButton) c, repaint);
					break;
				}
			}
		}
		return panel;
	}

}
