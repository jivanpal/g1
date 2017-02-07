package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlsPanel extends JPanel{
	private MainMenu menu;
	public ControlsPanel (MainMenu menu) {
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
		JPanel bpanel = createButtons();
		c.anchor = c.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
		setBackground(Color.BLACK);
		
	}
	
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		JButton thrustfwd = new JButton("Thrust Fwd");
		JButton thrustrev = new JButton("Thrust Rev");
		JButton pitchdown = new JButton("Pitch Down");
		JButton pitchup = new JButton("Pitch Up");
		JButton rollleft = new JButton("Roll Left");
		JButton rollright = new JButton("Roll Right");
		JButton fire1 = new JButton("Fire 1");
		JButton fire2 = new JButton("Fire 2");
		JButton fire3 = new JButton("Fire 3");
		JButton manual = new JButton("Manual");
		JButton manualprev = new JButton("Manual Prev");
		JButton manualnext = new JButton("Manual Next");
		JButton overdrive = new JButton("Overdrive");
		panel.add(thrustfwd);
		panel.add(thrustrev);
		panel.add(pitchdown);
		panel.add(pitchup);
		panel.add(rollleft);
		panel.add(rollright);
		panel.add(fire1);
		panel.add(fire2);
		panel.add(fire3);
		panel.add(manual);
		panel.add(manualprev);
		panel.add(manualnext);
		panel.add(overdrive);
		return panel;
		
	}
}
