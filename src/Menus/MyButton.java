package Menus;

import java.awt.Color;

import javax.swing.JButton;

import GameLogic.GameOptions;

/**
 * A customised JButton for the menus.
 * @author Jaren Liu
 *
 */
public class MyButton extends JButton {
	public MyButton(String text) {
		this.setText(text);
		this.setForeground(Color.WHITE);
		this.setFont(GameOptions.BUTTON_FONT);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setOpaque(false);
		this.setFocusable(false);
		this.addMouseListener(new HoverMouseListener(this));

	}

}
