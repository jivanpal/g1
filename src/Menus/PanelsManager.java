package Menus;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import Graphics.Screen;

public class PanelsManager {
	public static Rectangle PANEL_BOUNDS;
	public static JLayeredPane UILayers;
	public static Screen screen;
	public static JPanel currentPanel;

	public static void changePanel(JPanel oldPanel, JPanel newPanel, JButton button) {
		UILayers.remove(oldPanel);
		AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
		newPanel.setBounds(PANEL_BOUNDS);
		UILayers.add(newPanel, JLayeredPane.PALETTE_LAYER);
		if (button != null) {
			button.setForeground(UIManager.getColor("control"));
		}
		currentPanel = newPanel;
	}

	public static void removeAll() {
		UILayers.removeAll();
	}
	
	public static void resizeComponents() {
		screen.setBounds(PANEL_BOUNDS);
		currentPanel.setBounds(PANEL_BOUNDS);
	}
}
