package Menus;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import GameLogic.GameOptions;

public class MyCheckBox extends JCheckBox {
	public MyCheckBox (String name, ArrayList<JSlider> sliders) {
		this.setText(name);
		this.setName(name);
		this.setOpaque(false);
		this.setForeground(Color.WHITE);
		this.setFont(GameOptions.REGULAR_TEXT_FONT);
		this.addItemListener(new MuteItemListener(this, sliders));
	}
}
