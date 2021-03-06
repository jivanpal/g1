package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import GameLogic.Global;
import GameLogic.Map;
import GameLogic.Resource;
import GameLogic.Ship;
import GameLogic.Weapon;
import Graphics.Screen;

/**
 * Created by James on 01/02/17. This View contains the entire UI for the
 * Engineer once they are in game.
 * @author James Brown
 */
public class EngineerView extends AbstractPlayerView implements KeySequenceResponder, Observer {
	private static final String SOFT_FAILED_SEQUENCE = "FAILED SEQUENCE";
	private static final String HARD_FAILED_SEQUENCE = "FAILURE - SEQUENCE STOPPED";
	private static final String NEW_SEQUENCE = "NEW SEQUENCE";
	private static final Color SEQUENCE_FAILED_COLOR = Color.red;
	private static final Color SEQUENCE_NEW_COLOR = Color.blue;
	private static final int SEQUENCE_FULL_SCREEN_MESSAGE_TIME = 750;

	private boolean UIinitialised = false;

	private ShipState state = ShipState.NONE;
	private final KeySequenceManager keyManager;

	private WeaponView plasmaBlasterView;
	private WeaponView laserBlasterView;
	private WeaponView torpedosView;
	private ResourcesView resourcesView;

	private ArrayList<String> keySequences;
	private int shieldSequenceNum = 0;
	private final int SHIELD_MAX_NUM = 5;
	private int fuelSequenceNum = 6;
	private final int FUEL_MAX_NUM = 11;
	private int laserSequenceNum = 12;
	private final int LASER_MAX_NUM = 17;
	private int plasmaSequenceNum = 18;
	private final int PLASMA_MAX_NUM = 23;
	private int torpedoSequenceNum = 24;
	private final int TORPEDO_MAX_NUM = 29;

	// Amount of key sequence passes we are allowed at this difficulty of sequence
	private final int ALLOWED_DEFAULT = 30;
	private int shieldAllowedNum = ALLOWED_DEFAULT;
	private int fuelAllowedNum = ALLOWED_DEFAULT;
	private int laserAllowedNum = ALLOWED_DEFAULT;
	private int plasmaAllowedNum = ALLOWED_DEFAULT;
	private int torpedoAllowedNum = ALLOWED_DEFAULT;

	private JPanel UIBaseLayer;
	private JPanel UIPanel;

	public ArrayList<JButton> replenishButtons;

	/**
	 * Creates a new EngineerView Creates a new EngineerView.
	 * @param playerNickname The nickname of the player controlling this view.
	 * @param gameClient The GameClient handling network connections for this player.
	 */
	public EngineerView(String playerNickname, GameClient gameClient, JFrame parent) {
		super(playerNickname, gameClient, parent);

		gameClient.addObserver(this);

		keyManager = new KeySequenceManager(this);

		setFocusable(true);

		UIBaseLayer = new JPanel();

		// Create a listener in the parent - checks for whenever the window is
		// resized so the UI can be redrawn.
		parent.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {
				initialiseUI();
			}

			@Override
			public void componentMoved(ComponentEvent componentEvent) {
			}

			@Override
			public void componentShown(ComponentEvent componentEvent) {
				initialiseUI();
			}

			@Override
			public void componentHidden(ComponentEvent componentEvent) {
				initialiseUI();
			}
		});

		// Create a listener in the parent - handles key presses by the user.
		parent.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {

			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {

			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
					// User wishes to escape out of this sequence.
					state = ShipState.NONE;
					changeButton("none");
					keyManager.deactivate();
				} else {
					if (keyManager.isActive()) {
						// If we're half way through a sequence, pass the key
						// off to the key manager.
						keyManager.keyPressed(keyEvent);
					} else {
						// We're not in a sequence, see if this key was a
						// keybind to start a new one.
						String sequence = "";
						switch (keyEvent.getKeyChar()) {
						case 'l':
							state = ShipState.LASER_REPLENISH;
							sequence = Utils.Utils.parseSequence(keySequences.get(laserSequenceNum));
							keyManager.initialiseKeySequenceManager(sequence, true);
							break;
						case 't':
							state = ShipState.TORPEDO_REPLENISH;
							sequence = Utils.Utils.parseSequence(keySequences.get(torpedoSequenceNum));
							keyManager.initialiseKeySequenceManager(sequence, false);
							break;
						case 'p':
							state = ShipState.PLASMA_REPLENISH;
							sequence = Utils.Utils.parseSequence(keySequences.get(plasmaSequenceNum));
							keyManager.initialiseKeySequenceManager(sequence, false);
							break;
						case 's':
							state = ShipState.SHIELD_REPLENISH;
							sequence = Utils.Utils.parseSequence(keySequences.get(shieldSequenceNum));
							keyManager.initialiseKeySequenceManager(sequence, true);
							break;
						case 'f':
							state = ShipState.FUEL_REPLENISH;
							sequence = Utils.Utils.parseSequence(keySequences.get(fuelSequenceNum));
							keyManager.initialiseKeySequenceManager(sequence, true);
							break;
						}
					}
				}
			}
		});

		// Create a listener - handles mouse clicks and passing control between
		// the chat window and the game.
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				// If we click anywhere other than the chat window, send focus
				// back to the game.
				if (!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
					parentFrame.requestFocusInWindow();
				}
			}

			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				// If we click anywhere other than the chat window, send focus
				// back to the game.
				if (!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
					parentFrame.requestFocusInWindow();
				}
			}

			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				// If we click anywhere other than the chat window, send focus
				// back to the game.
				if (!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
					parentFrame.requestFocusInWindow();
				}
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent) {

			}

			@Override
			public void mouseExited(MouseEvent mouseEvent) {

			}
		});

		initialiseUI();

		// starting the in-game sounds
		AudioPlayer.stopMusic();
		AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
	}

	/**
	 * Creates all the elements of the UI and positions them on the screen. Sets
	 * all default values of the UI elements.
	 */
	public void initialiseUI() {
		if (UIinitialised) {
			UILayeredPane.removeAll();
			UIBaseLayer.removeAll();
		}

		// Loop until we've successfully found our teams ship
		currentShip = findPlayerShip(gameClient.getMap());
		while (currentShip == null) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentShip = findPlayerShip(gameClient.getMap());
		}

		replenishButtons = new ArrayList<JButton>();

		keySequences = gameClient.keySequence.getAllKeys();

		initialiseWeapons(currentShip);
		initialiseResources(currentShip);
		initialiseScreen();
		initialiseRadar();
		initialiseChatWindow(gameClient, playerNickname);
		addAllComponents();

		this.revalidate();
		this.repaint();
		UIBaseLayer.revalidate();
		UIBaseLayer.repaint();
		UILayeredPane.revalidate();
		UILayeredPane.repaint();

		this.UIinitialised = true;

		parentFrame.requestFocusInWindow();
		parentFrame.setFocusable(true);

		update(null, null);
		screen.setMap(gameClient.getMap());
		screen.revalidate();
		screen.repaint();
	}

	/**
	 * Flashes some UI elements a color for a very brief period of time to
	 * indicate that the ship has been damaged
	 */
	@Override
	protected void flashUIDamaged(Color c) {
		// Set the background of the UI to be the damaged color
		UIPanel.setBackground(c);

		// Wait DAMAGE_FLASH_TIME amount of milliseconds, and then set the components background color back to the default
		Timer t = new Timer(DAMAGE_FLASH_TIME, e -> {
			UIPanel.setBackground(ViewConstants.UI_BACKGROUND_COLOR);
		});
		t.setRepeats(false);
		t.start();
	}

	/**
	 * Adds all of the UI components to the JPanel.
	 */
	private void addAllComponents() {
		this.setLayout(new BorderLayout());

		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridBagLayout());
		weaponPanel.setOpaque(false);
		GridBagConstraints weaponConstraints = new GridBagConstraints();
		weaponConstraints.weightx = 0.5;
		weaponConstraints.weighty = 0.5;
		weaponConstraints.gridwidth = 0;
		weaponConstraints.fill = GridBagConstraints.HORIZONTAL;
		weaponConstraints.ipady = 10;
		weaponConstraints.anchor = GridBagConstraints.NORTH;
		weaponPanel.add(plasmaBlasterView, weaponConstraints);
		weaponConstraints.anchor = GridBagConstraints.CENTER;
		weaponPanel.add(laserBlasterView, weaponConstraints);
		weaponConstraints.anchor = GridBagConstraints.SOUTH;
		weaponPanel.add(torpedosView, weaponConstraints);

		UIPanel = new JPanel();
		UIPanel.setLayout(new GridBagLayout());
		GridBagConstraints uiPanelConstraints = new GridBagConstraints();
		uiPanelConstraints.weightx = 0.5;
		uiPanelConstraints.weighty = 0.5;
		uiPanelConstraints.gridwidth = 1;
		uiPanelConstraints.gridheight = 0;
		uiPanelConstraints.gridx = 0;
		uiPanelConstraints.gridy = 0;
		uiPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		uiPanelConstraints.insets = new Insets(10, 20, 30, 20);
		// uiPanelConstraints.anchor = GridBagConstraints.WEST;

		resourcesView.setOpaque(false);
		UIPanel.add(resourcesView, uiPanelConstraints);

		// uiPanelConstraints.weightx = 1;
		uiPanelConstraints.gridx = 1;

		// uiPanelConstraints.anchor = GridBagConstraints.EAST;
		UIPanel.add(weaponPanel, uiPanelConstraints);

		UIPanel.setOpaque(true);
		UIPanel.setForeground(ViewConstants.UI_BACKGROUND_COLOR);
		UIPanel.setBackground(ViewConstants.UI_BACKGROUND_COLOR);
		UIPanel.paintComponents(getGraphics());

		UIBaseLayer.setLayout(new BorderLayout());
		UIBaseLayer.add(screen, BorderLayout.CENTER);
		UIBaseLayer.add(UIPanel, BorderLayout.SOUTH);
		UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
		JLayeredPaneLayoutManager layoutManager = new JLayeredPaneLayoutManager();

		UILayeredPane.setLayout(layoutManager);
		UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);

		radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4,
				parentFrame.getHeight() / 4);
		radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));

		UILayeredPane.add(radarView, JLayeredPane.MODAL_LAYER);

		chatWindow.setBounds(0,
				parentFrame.getHeight()
						- ((int) UIPanel.getPreferredSize().getHeight() + (parentFrame.getHeight() / 6)),
				parentFrame.getWidth() / 6, parentFrame.getHeight() / 6);
		chatWindow.setPreferredSize(new Dimension(parentFrame.getWidth() / 6, parentFrame.getHeight() / 6));
		UILayeredPane.add(chatWindow, JLayeredPane.PALETTE_LAYER);
	}

	/**
	 * Given a Ship, this will initialise the weapon progress bars to their
	 * initial values and set their maximum values
	 * @param s This players Ship object
	 */
	private void initialiseWeapons(Ship s) {
		// Get all the manual replenish numbers to display them to the user
		String plasmaReplenishNumber = Utils.Utils.parseNumber(keySequences.get(plasmaSequenceNum));
		String laserReplenishNumber = Utils.Utils.parseNumber(keySequences.get(laserSequenceNum));
		String torpedoReplenishNumber = Utils.Utils.parseNumber(keySequences.get(torpedoSequenceNum));

		// Create the views
		plasmaBlasterView = new WeaponView("Plasma Blaster", true, plasmaReplenishNumber, replenishButtons);
		laserBlasterView = new WeaponView("Laser Blaster", true, laserReplenishNumber, replenishButtons);
		torpedosView = new WeaponView("Torpedos", true, torpedoReplenishNumber, replenishButtons);

		// Set the various attributes of the view.
		laserBlasterView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.LASER).getAmmoMaximum());
		plasmaBlasterView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.PLASMA).getAmmoMaximum());
		torpedosView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.TORPEDO).getAmmoMaximum());
		laserBlasterView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.LASER).getAmmoLevel());
		plasmaBlasterView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.PLASMA).getAmmoLevel());
		torpedosView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel());

		// Tell the view what to do when it been clicked
		laserBlasterView.setReplenishAmmo(this, ShipState.LASER_REPLENISH);
		plasmaBlasterView.setReplenishAmmo(this, ShipState.PLASMA_REPLENISH);
		torpedosView.setReplenishAmmo(this, ShipState.TORPEDO_REPLENISH);
	}

	/**
	 * Given a Ship, this will initialise the resource progress bars to their
	 * initial values and set their maximum values
	 * @param s This players Ship object
	 */
	private void initialiseResources(Ship s) {

		// Get the manual sequence numbers to display them to the user
		String shieldSequenceNumber = Utils.Utils.parseNumber(keySequences.get(shieldSequenceNum));
		String fuelSequenceNumber = Utils.Utils.parseNumber(keySequences.get(fuelSequenceNum));

		resourcesView = new ResourcesView(this, shieldSequenceNumber, fuelSequenceNumber, replenishButtons);

		resourcesView.updateResourceLevels(ResourcesView.ENGINE, s.getResource(Resource.Type.ENGINES).get());
		resourcesView.updateResourceLevels(ResourcesView.SHIELDS, s.getResource(Resource.Type.SHIELDS).get());
		resourcesView.updateResourceLevels(ResourcesView.HULL, s.getResource(Resource.Type.HEALTH).get());

		resourcesView.setMaximumResourceLevel(ResourcesView.ENGINE, s.getResource(Resource.Type.ENGINES).getMax());
		resourcesView.setMaximumResourceLevel(ResourcesView.SHIELDS, s.getResource(Resource.Type.SHIELDS).getMax());
		resourcesView.setMaximumResourceLevel(ResourcesView.HULL, s.getResource(Resource.Type.HEALTH).getMax());
	}

	/**
	 * Initialise the Screen for the UI
	 */
	private void initialiseScreen() {
		this.screen = new Screen(playerNickname, false, true);
		screen.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - (this.getHeight() / 5)));
		Global.SCREEN_WIDTH = parentFrame.getWidth();
		Global.SCREEN_HEIGHT = parentFrame.getHeight() - (parentFrame.getHeight() / 5);
	}
	
	/**
	 * Updates all elements of the view when a new map is recieved.
	 */
	@Override
	public void update(Observable observable, Object o) {
		super.update(observable, o);

		if (gameActive) {
			// Try and update the UI. May throw an exception if you receive a map update before the UI has been fully
			// initialised. We catch this exception and just do nothing.
			try {
				Map m = gameClient.getMap();
				radarView.updateMap(m);

				laserBlasterView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.LASER).getAmmoLevel());
				plasmaBlasterView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel());
				torpedosView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel());
				resourcesView.updateResourceLevels(ResourcesView.ENGINE,
						currentShip.getResource(Resource.Type.ENGINES).get());
				resourcesView.updateResourceLevels(ResourcesView.SHIELDS,
						currentShip.getResource(Resource.Type.SHIELDS).get());
				resourcesView.updateResourceLevels(ResourcesView.HULL, currentShip.getResource(Resource.Type.HEALTH).get());
			} catch (Exception e) {
				// UI probably not initialised yet, give up.
				System.err.println("Caught exception in UPDATE");
			}
		}
	}

	/**
	 * Called by the KeyManager when a key has been correctly entered by the
	 * user. Plays a sound effect of a key press.
	 */
	public void correctKeyPress() {
		// Play audio feedback to the user
		AudioPlayer.playSoundEffect(AudioPlayer.KEY_PRESS_EFFECT);
	}

	/**
	 * Called by the KeyManager when a key sequence has been completed in it's
	 * entirety. Depending on the current state of the ship, we tell the server
	 * what the user has done.
	 */
	public void keySequencePassed() {
		switch (state) {
		case NONE:
			break;
		case SHIELD_REPLENISH:
			shieldAllowedNum--;
			if (shieldAllowedNum <= 0 && (shieldSequenceNum <= SHIELD_MAX_NUM)) {
				keyManager.deactivate();
				shieldAllowedNum = ALLOWED_DEFAULT;
				shieldSequenceNum += 1;
				this.state = ShipState.NONE;
				changeButton("none");
				displayFullScreenMessage(NEW_SEQUENCE,
						SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
						SEQUENCE_NEW_COLOR);
			}
			resourcesView.updateRefreshNumber(ResourcesView.SHIELDS, Utils.Utils.parseNumber(keySequences.get(shieldSequenceNum)));

			gameClient.send("shieldReplenish");
			break;
		case FUEL_REPLENISH:

			fuelAllowedNum--;
			if (fuelAllowedNum <= 0 && (fuelSequenceNum <= FUEL_MAX_NUM)) {
				keyManager.deactivate();
				fuelAllowedNum = ALLOWED_DEFAULT;
				fuelSequenceNum += 1;
				this.state = ShipState.NONE;
				changeButton("none");
				displayFullScreenMessage(NEW_SEQUENCE,
						SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
						SEQUENCE_NEW_COLOR);
			}
			resourcesView.updateRefreshNumber(ResourcesView.ENGINE, Utils.Utils.parseNumber(keySequences.get(fuelSequenceNum)));

			gameClient.send("fuelReplenish");
			break;
		case LASER_REPLENISH:
			laserAllowedNum--;
			if (laserAllowedNum <= 0 && (laserSequenceNum <= LASER_MAX_NUM)) {
				keyManager.deactivate();
				laserAllowedNum = ALLOWED_DEFAULT;
				laserSequenceNum += 1;
				this.state = ShipState.NONE;
				changeButton("none");
				displayFullScreenMessage(NEW_SEQUENCE,
						SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
						SEQUENCE_NEW_COLOR);
			}
			laserBlasterView.setReplenishAmmoNumber(Utils.Utils.parseNumber(keySequences.get(laserSequenceNum)));

			gameClient.send("laserReplenish");
			break;
		case TORPEDO_REPLENISH:

			torpedoAllowedNum--;
			if (torpedoAllowedNum <= 0 && (torpedoSequenceNum <= TORPEDO_MAX_NUM)) {
				keyManager.deactivate();
				torpedoAllowedNum = ALLOWED_DEFAULT;
				torpedoSequenceNum += 1;
				this.state = ShipState.NONE;
				changeButton("none");

				displayFullScreenMessage(NEW_SEQUENCE,
						SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
						SEQUENCE_NEW_COLOR);
			}
			torpedosView.setReplenishAmmoNumber(Utils.Utils.parseNumber(keySequences.get(torpedoSequenceNum)));

			gameClient.send("torpedoReplenish");
			break;
		case PLASMA_REPLENISH:

			plasmaAllowedNum--;
			if (plasmaAllowedNum <= 0 && (plasmaSequenceNum <= PLASMA_MAX_NUM)) {
				keyManager.deactivate();
				plasmaAllowedNum = ALLOWED_DEFAULT;
				plasmaSequenceNum += 1;
				this.state = ShipState.NONE;
				changeButton("none");

				displayFullScreenMessage(NEW_SEQUENCE,
						SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
						SEQUENCE_NEW_COLOR);
			}
			plasmaBlasterView.setReplenishAmmoNumber(Utils.Utils.parseNumber(keySequences.get(plasmaSequenceNum)));

			gameClient.send("plasmaReplenish");
			break;
		}
	}

	/**
	 * Called by the KeyManager when a key sequence has been failed but
	 * automatically restarted.
	 */
	public void keySequenceSoftFailure() {
		AudioPlayer.playSoundEffect(AudioPlayer.KEY_SEQUENCE_FAILED);
		displayFullScreenMessage(SOFT_FAILED_SEQUENCE,
				SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
				SEQUENCE_FAILED_COLOR);
	}

	/**
	 * Called by the KeyManager when a key sequence has been failed and /not/
	 * automatically restarted. Depending on the current state of the ship, we
	 * may wish to show a message to the user.
	 */
	public void keySequenceHardFailure() {
		AudioPlayer.playSoundEffect(AudioPlayer.KEY_SEQUENCE_FAILED);
		displayFullScreenMessage(HARD_FAILED_SEQUENCE,
				SEQUENCE_FULL_SCREEN_MESSAGE_TIME,
				SEQUENCE_FAILED_COLOR);
		this.state = ShipState.NONE;
		changeButton("none");
	}

	/**
	 * Sets the current state of the ship
	 * @param newState The new state of the ship
	 */
	void setState(ShipState newState) {
		this.state = newState;

		String sequence = "";

		switch (state) {
		case NONE:
			keyManager.deactivate();
			changeButton("none");
			break;
		case SHIELD_REPLENISH:
			sequence = Utils.Utils.parseSequence(keySequences.get(shieldSequenceNum));
			keyManager.initialiseKeySequenceManager(sequence, true);
			changeButton("Shields");
			break;
		case FUEL_REPLENISH:
			sequence = Utils.Utils.parseSequence(keySequences.get(fuelSequenceNum));
			keyManager.initialiseKeySequenceManager(sequence, true);
			changeButton("Engines");
			break;
		case LASER_REPLENISH:
			sequence = Utils.Utils.parseSequence(keySequences.get(laserSequenceNum));
			keyManager.initialiseKeySequenceManager(sequence, true);
			changeButton("Laser Blaster");
			break;
		case TORPEDO_REPLENISH:
			sequence = Utils.Utils.parseSequence(keySequences.get(torpedoSequenceNum));
			keyManager.initialiseKeySequenceManager(sequence, false);
			changeButton("Torpedos");
			break;
		case PLASMA_REPLENISH:
			sequence = Utils.Utils.parseSequence(keySequences.get(plasmaSequenceNum));
			keyManager.initialiseKeySequenceManager(sequence, true);
			changeButton("Plasma Blaster");
			break;
		}
	}

	/**
	 * Sets the correct button to be highlighted based on the current state of the ship
	 * @param state The current state of the ship
	 */
	private void changeButton(String state) {
		for (JButton b : replenishButtons) {
			if (state.equals(b.getName())) {
				b.setBackground(Color.decode("#ff3333"));
			} else {
				b.setBackground(Color.decode("#cccccc"));
			}
		}
	}

	/**
	 * Returns the current state of the Ship.
	 * @return Returns the current state of the Ship.
	 */
	ShipState getState() {
		return this.state;
	}
}

/**
 * An enumeration to hold all of the possible states of the ships
 * @author James Brown
 */
enum ShipState {
	NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
