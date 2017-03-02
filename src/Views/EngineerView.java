package Views;

import javax.swing.*;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import Graphics.Screen;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Graphics.Screen;

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel implements KeyListener, KeySequenceResponder, Observer {
    private boolean UIinitialised = false;

    private ShipState state = ShipState.NONE;
    private final KeySequenceManager keyManager;

    private Screen screen;
    private WeaponView plasmaBlasterView;
    private WeaponView laserBlasterView;
    private WeaponView torpedosView;
    private ResourcesView resourcesView;

    private GameClient gameClient;
    private String playerNickname;

    private char[][] keySequences;

    private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;
    private JFrame parentFrame;

    /**
     * Creates a new EngineerView
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public EngineerView(String playerNickname, GameClient gameClient, JFrame parent) {
        super();

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        keyManager = new KeySequenceManager(this);

        this.parentFrame = parent;
        UILayeredPane = parent.getLayeredPane();
        UIBaseLayer = new JPanel();

        parent.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
                UIBaseLayer.revalidate();
                UIBaseLayer.repaint();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {
                UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
                UIBaseLayer.revalidate();
                UIBaseLayer.repaint();
            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
                UIBaseLayer.revalidate();
                UIBaseLayer.repaint();
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
                UIBaseLayer.revalidate();
                UIBaseLayer.repaint();
            }
        });

        initialiseUI();

        addKeyListener(this);
        setFocusable(true);

        // starting the in-game sounds
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
    }

    /**
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    public void initialiseUI() {
        try {
            Ship s = findPlayerShip();

            initialiseWeapons(s);
            initialiseResources(s);
            initialiseScreen();
            addAllComponents();
            System.out.println("Done initialising the UI. I am the Engineer");
            UIinitialised = true;

        } catch (Exception e) {
            System.out.println("Unable to find the Ship");
        }
    }

    /**
     * Adds all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        this.setLayout(new BorderLayout());

        Container UIPanel = new Container();
        UIPanel.setLayout(new BoxLayout(UIPanel, BoxLayout.X_AXIS));

        Container weaponPanel = new Container();
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));
        weaponPanel.add(plasmaBlasterView);
        weaponPanel.add(laserBlasterView);
        weaponPanel.add(torpedosView);

        UIPanel.add(resourcesView);
        UIPanel.add(weaponPanel);

        UIBaseLayer.setLayout(new BorderLayout());
        UIBaseLayer.add(screen, BorderLayout.CENTER);
        UIBaseLayer.add(UIPanel, BorderLayout.SOUTH);
        UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
        JLayeredPaneLayoutManager layoutManager = new JLayeredPaneLayoutManager();

        UILayeredPane.setLayout(layoutManager);
        UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);

        this.revalidate();
        this.repaint();
    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseWeapons(Ship s) {
        plasmaBlasterView = new WeaponView("Plasma Blaster", true);
        plasmaBlasterView.setReplenishAmmo(this, ShipState.PLASMA_REPLENISH);

        // default plasma blaster to be highlighted, remove at a later date!
        // plasmaBlasterView.setHighlightWeapon(true);

        laserBlasterView = new WeaponView("Laser Blaster", true);
        laserBlasterView.setReplenishAmmo(this, ShipState.LASER_REPLENISH);

        torpedosView = new WeaponView("Torpedos", true);
        torpedosView.setReplenishAmmo(this, ShipState.TORPEDO_REPLENISH);

        System.out.println("Created all weapons");

        laserBlasterView.setMaxiumumAmmo(s.getWeaponMaxAmmoByIndex(Ship.LASER_BLASTER_INDEX));
        plasmaBlasterView.setMaxiumumAmmo(s.getWeaponMaxAmmoByIndex(Ship.LASER_BLASTER_INDEX));
        torpedosView.setMaxiumumAmmo(s.getWeaponMaxAmmoByIndex(Ship.TORPEDO_WEAPON_INDEX));
        laserBlasterView.updateWeaponAmmoLevel(s.getLaserBlasterAmmo());
        plasmaBlasterView.updateWeaponAmmoLevel(s.getPlasmaBlasterAmmo());
        torpedosView.updateWeaponAmmoLevel(s.getTorpedoWeaponAmmo());
    }

    /**
     * Given a Ship, this will initialse the resource progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseResources(Ship s) {
        resourcesView = new ResourcesView(this);
        resourcesView.updateResourceLevels(ResourcesView.ENGINE, s.getFuelLevel());
        resourcesView.updateResourceLevels(ResourcesView.SHIELDS, s.getShieldLevels());
        resourcesView.updateResourceLevels(ResourcesView.HULL, s.getShipHealth());
        resourcesView.setMaximumResourceLevel(ResourcesView.ENGINE, Engines.DEFAULT_FUEL_MAX_LEVEL);
        resourcesView.setMaximumResourceLevel(ResourcesView.HULL, ShipHealth.DEFAULT_MAX_SHIP_HEALTH_LEVEL);
        resourcesView.setMaximumResourceLevel(ResourcesView.SHIELDS, Shields.DEFAULT_MAX_SHIELDS_LEVEL);
    }

    /**
     * Initialise the Screen for the UI
     */
    private void initialiseScreen() {
        this.screen = new Screen(playerNickname, false);
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));
    }

    /**
     * Finds the players Ship within all of the objects in the Map
     *
     * @return The players Ship object
     * @throws Exception No ship could be found, in theory this should never be called! Hopefully...
     */
    private Ship findPlayerShip() throws Exception {
        Map m = gameClient.getMap();

        for (int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++) {
            if (m.get(i) instanceof Ship) {
                Ship s = (Ship) m.get(i);

                if (s.getEngineerName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        throw new Exception("ERROR: Couldn't find the players ship!");
    }

    @Override
    public void update(Observable observable, Object o) {
        if (!UIinitialised) {
            // TODO: Swap the over to the proper Manual view. This is just a temporary solution.
            try {
                keySequences = gameClient.keySequence.getSequencesByLength(2);
            } catch (Exception e) {
                // Should never get here
            }
            initialiseUI();
            UIinitialised = true;
        } else {
            Map m = gameClient.getMap();
            screen.setMap(m);

            for (int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++) {
                if (m.get(i) instanceof Ship) {
                    Ship s = (Ship) m.get(i);

                    if (s.getEngineerName().equals(playerNickname)) {
                        laserBlasterView.updateWeaponAmmoLevel(s.getLaserBlasterAmmo());
                        plasmaBlasterView.updateWeaponAmmoLevel(s.getPlasmaBlasterAmmo());
                        torpedosView.updateWeaponAmmoLevel(s.getTorpedoWeaponAmmo());
                        resourcesView.updateResourceLevels(ResourcesView.ENGINE, s.getFuelLevel());
                        resourcesView.updateResourceLevels(ResourcesView.SHIELDS, s.getShieldLevels());
                        resourcesView.updateResourceLevels(ResourcesView.HULL, s.getShipHealth());
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println("Key was pressed");
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // User wishes to escape out of this sequence.
            System.out.println("Stopping this sequence");
            state = ShipState.NONE;
            keyManager.deactivate();
        } else {
            if (keyManager.isActive()) {
                // If we're half way through a sequence, pass the key off to the key manager.
                keyManager.keyPressed(keyEvent);
            } else {
                // We're not in a sequence, see if this key was a keybind to start a new one.
                switch (keyEvent.getKeyChar()) {
                    case 'l':
                        System.out.println("Starting a laser sequence");
                        this.state = ShipState.LASER_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[0]), true);
                        break;
                    case 't':
                        System.out.println("Starting a torpedo sequence");
                        this.state = ShipState.TORPEDO_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[1]), false);
                        break;
                    case 'p':
                        System.out.println("Starting a plasma sequence");
                        this.state = ShipState.PLASMA_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[2]), false);
                        break;
                    case 's':
                        System.out.println("Startng a shield sequence");
                        this.state = ShipState.SHIELD_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[3]), true);
                        break;
                    case 'f':
                        System.out.println("Starting a fuel sequence");
                        this.state = ShipState.FUEL_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[4]), true);
                        break;

                }
            }
        }
    }

    /**
     * Called by the KeyManager when a key sequence has been completed in it's entirety. Depending on the current state
     * of the ship, we tell the server what the user has done.
     */
    public void keySequencePassed() {
        System.out.println("Passed a sequence");
        switch (state) {
            case NONE:
                break;
            case SHIELD_REPLENISH:
                gameClient.send("shieldReplenish");
                break;
            case FUEL_REPLENISH:
                gameClient.send("fuelReplenish");
                break;
            case LASER_REPLENISH:
                gameClient.send("laserReplenish");
                break;
            case TORPEDO_REPLENISH:
                gameClient.send("torpedoReplenish");
                break;
            case PLASMA_REPLENISH:
                gameClient.send("plasmaReplenish");
                break;
        }
    }

    /**
     * Called by the KeyManager when a key sequence has been failed but automatically restarted.
     */
    public void keySequenceSoftFailure() {
        System.out.println("Soft failure of sequence");
    }

    /**
     * Called by the KeyManager when a key sequence has been failed and /not/ automatically restarted. Depending on the
     * current state of the ship, we may wish to show a message to the user.
     */
    public void keySequenceHardFailure() {
        System.out.println("Hard failure of sequence");
        this.state = ShipState.NONE;
    }

    public void setState(ShipState newState) {
        this.state = newState;

        switch (state) {
            case NONE:
                break;
            case SHIELD_REPLENISH:
                System.out.println("Startng a shield sequence");
                keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[3]), true);
                break;
            case FUEL_REPLENISH:
                System.out.println("Starting a fuel sequence");
                keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[4]), true);
                break;
            case LASER_REPLENISH:
                System.out.println("Starting a laser sequence");
                keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[0]), true);
                break;
            case TORPEDO_REPLENISH:
                System.out.println("Starting a torpedo sequence");
                keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[1]), false);
                break;
            case PLASMA_REPLENISH:
                System.out.println("Starting a plasma sequence");
                keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[2]), false);
                break;
        }
    }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
