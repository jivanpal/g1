package Menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import Audio.AudioPlayer;
import ClientNetworking.Client;
import ClientNetworking.GameClient.GameVariables;
import GameLogic.Asteroid;
import GameLogic.GameOptions;
import GameLogic.Global;
import GameLogic.Map;
import GameLogic.Ship;
import Geometry.Rotation;
import Geometry.Vector;
import Graphics.Screen;
import Physics.Body;
import Views.JLayeredPaneLayoutManager;

/**
 * The Main Menu of the game.
 * 
 * @author Jaren Chin-Hao Liu
 */

public class ButtonPanel extends JPanel {
	private MainMenu menu;
	public Client client;
	private JLayeredPane UILayers;
	public static JPanel menuPanel;

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
		Global.SCREEN_WIDTH = menu.getFrame().getWidth();
		Global.SCREEN_HEIGHT = menu.getFrame().getHeight();

		Screen screen = new Screen("", true, false);
		Map map = new Map(1000, 1000, 1000);
		Ship ship = new Ship("", "");
		//ship.setAngularVelocity(new Vector(0, 0, 0.05));
		//ship.setVelocity(new Vector(0, 0, 0));
		
		map.add(ship);
		System.out.println(Global.SCREEN_WIDTH + ", " + Global.SCREEN_WIDTH);
		Random r = new Random();
		for (int i = 0; i < 50; i++) {
			Asteroid a = new Asteroid(new Vector(r.nextDouble() * map.getDimensions().getX(),
					r.nextDouble() * map.getDimensions().getY(), r.nextDouble() * map.getDimensions().getZ()),
					new Rotation(r.nextDouble() * Math.PI * 2, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI * 2));
			map.add(a);

//			boolean overlaps = true;
//			while (overlaps) {
//				overlaps = false;
//
//				for (int bID : map.bodyIDs()) {
//					if (map.overlaps(a.getID(), bID) && bID != a.getID()) {
//						overlaps = true;
//							a.setPosition(new Vector(r.nextDouble() * map.getDimensions().getX(),
//									r.nextDouble() * map.getDimensions().getY(), r.nextDouble() * map.getDimensions().getZ()));
//							a.setOrientation(
//									new Rotation(r.nextDouble() * Math.PI * 2, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI * 2));
//						break;
//					}
//				}
//			}
			
//			System.out.println(a.getPosition());
		}
		screen.setBounds(rect);
		UILayers.add(screen, JLayeredPane.DEFAULT_LAYER);

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
		JLabel title = new JLabel(
				"<html>Space Flying 101<br><br>Welcome <font color='#66e0ff'>" + client.name + "</font></html>");
		title.setForeground(Color.WHITE);
		title.setOpaque(false);
		title.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		c.anchor = GridBagConstraints.NORTH;
		menuPanel.add(title, c);
		menuPanel.setBounds(rect);
		UILayers.add(menuPanel, JLayeredPane.PALETTE_LAYER);
		this.menuPanel = menuPanel;
		Timer timer = new Timer(30, e -> {
			ship.rotate(new Rotation(Vector.K.scale(-0.001)));
			ship.update();
			screen.setMap(map);
		});
		timer.setRepeats(true);
		timer.start();
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
			//UILayers.remove(menuPanel);
			UILayers.removeAll();
			PlayPanel ppanel = new PlayPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(ppanel);
			/*
			Rectangle rect = new Rectangle(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
			ppanel.setBounds(rect);
			UILayers.add(ppanel, JLayeredPane.PALETTE_LAYER);
			UILayers.repaint();
			UILayers.revalidate();
			*/
			Body.nextID = 0;

		});
		MyButton settings = new MyButton("Settings");
		settings.addActionListener(e -> {
			UILayers.removeAll();
			SettingsPanel spanel = new SettingsPanel(menu);
			AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
			menu.changeFrame(spanel);
			Body.nextID = 0;

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
