package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import GameLogic.Asteroid;
import GameLogic.GameOptions;
import GameLogic.Map;
import GameLogic.Ship;
import Geometry.Rotation;
import Geometry.Vector;
import Graphics.Screen;
import Graphics.Star;
import Views.JLayeredPaneLayoutManager;

/**
 * The Main Menu of the game.
 * 
 * @author Jaren Chin-Hao Liu
 */
// TODO Maybe an animated background
public class ButtonPanel extends JPanel {
	private MainMenu menu;
	public Client client;
	private JLayeredPane UILayers;

	/**
	 * Constructor for the main menu. Adds the buttons and how it looks in the
	 * main menu.
	 * 
	 * @param menu
	 *            The main frame which the game will use
	 */
	public ButtonPanel(MainMenu menu) {
		super();
		this.menu = menu;
		this.client = menu.client;
		this.UILayers = menu.getFrame().getLayeredPane();
	}

	public void makeUI() {
		UILayers.setLayout(new JLayeredPaneLayoutManager());
		Rectangle rect = new Rectangle(0, 0, menu.getFrame().getWidth(), menu.getFrame().getHeight());
		
		Screen screen = new Screen(client.name, true, false);
		Map map = new Map(100, 100, 100);
		Ship ship = new Ship(client.name, client.name);
		ship.setAngularVelocity(new Vector (1.0, 0, 0));
		
		map.add(ship);
		
		Random r = new Random();
		r.ints(0, (int) map.getDimensions().getX());
		for(int i = 0; i < 50; i++){
			map.add(new Asteroid(new Vector(r.nextInt(), r.nextInt(), r.nextInt()), new Rotation(r.nextDouble() * Math.PI * 2, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI * 2)));
		}
		screen.setMap(map);
		screen.setBounds(rect);
		UILayers.add(screen, UILayers.DEFAULT_LAYER);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setOpaque(false);
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JPanel bpanel = createButtons();
		bpanel.setOpaque(false);
		menuPanel.add(bpanel, c);
		JLabel title = new JLabel("<html>Space Flying 101<br><br>Welcome <font color='#66e0ff'>" + client.name +"</font></html>");
		title.setForeground(Color.WHITE);
		title.setOpaque(false);
		title.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		c.anchor = GridBagConstraints.NORTH;
		menuPanel.add(title, c);
		//menuPanel.setBackground(Color.BLACK);
		menuPanel.setBounds(rect);
		UILayers.add(menuPanel, UILayers.PALETTE_LAYER);
		
		this.revalidate();
		this.repaint();
		UILayers.revalidate();
		UILayers.repaint();
		screen.revalidate();
		screen.repaint();
	}
	
	/**
	 * Creates the buttons which will be used on the main menu, such as Play,
	 * Settings and Exit.
	 * 
	 * @return A JPanel which will contain those buttons in BorderLayout.
	 */
	public JPanel createButtons() {
		JPanel panel = new JPanel();
		MyButton play = new MyButton("Play");
		play.addActionListener(e -> {
			UILayers.removeAll();
			PlayPanel ppanel = new PlayPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(ppanel);
			
		});
		MyButton settings = new MyButton("Settings");
		settings.addActionListener(e -> {
			UILayers.removeAll();
			SettingsPanel spanel = new SettingsPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
			
		});
		MyButton exit = new MyButton("Exit");
		exit.addActionListener(e -> {
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			System.exit(0);
		});
		panel.setLayout(new BorderLayout());
		panel.add(play, BorderLayout.NORTH);
		panel.add(settings, BorderLayout.CENTER);
		panel.add(exit, BorderLayout.SOUTH);
		return panel;
	}
	
}
