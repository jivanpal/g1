package Menus;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MuteItemListener implements ItemListener {

	public ArrayList<JSlider> sliders;
	public JCheckBox cb;
	
	public MuteItemListener(JCheckBox cb, ArrayList<JSlider> sliders) {
		this.sliders = sliders;
		this.cb = cb;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		for(JSlider s : sliders) {
			switch(cb.getName()) {
			case "Mute Music":
				if (s.equals("Music Volume")) {
					s.setValue(0);
					notifyChange(s);
				}
				break;
			case "Mute Sound Effects":
				if (s.equals("Sound Effects")) {
					s.setValue(0);
					notifyChange(s);
				}
				break;
			}
		}
		
		
	}
	
	private void notifyChange(JSlider s) {
		ChangeEvent ce = new ChangeEvent(s);
		for(ChangeListener cl : s.getChangeListeners()) {
			cl.stateChanged(ce);
		}
	}
	
}
