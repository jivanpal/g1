package Views;

import javax.swing.*;
import javax.swing.border.Border;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Graphics.Screen;
import UI.ClientShipObservable;
import javafx.scene.input.KeyCode;

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel implements KeyListener, Observer {
    private boolean UIinitialised = false;
    private ShipState state = ShipState.NONE;
    private final KeySequenceManager keyManager;

    private WeaponView plasmaBlasterView;
    private WeaponView laserBlasterView;
    private WeaponView torpedosView;

    private Screen screen;
    private ResourcesView resourcesView;
    private GameClient gameClient;
    private String playerNickname;

    private char[][] keySequences;

    /*private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;*/

    public EngineerView(String playerNickname, GameClient gameClient) {
        super();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        keyManager = new KeySequenceManager(this);

        addKeyListener(this);
        setFocusable(true);

        // initialiseUI();

        // starting the in-game sounds
        /*AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);*/


        /*UILayeredPane = new JLayeredPane();
        JLayeredPaneLayoutManager layeredLayoutManager = new JLayeredPaneLayoutManager();
        UILayeredPane.setLayout(layeredLayoutManager);

        UIBaseLayer = new JPanel();
        UIBaseLayer.setLayout(new BorderLayout());*/


        // UIBaseLayer.add(screen, BorderLayout.CENTER);


        // UIBaseLayer.add(UIPanel, BorderLayout.SOUTH);

        /*UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);
        layeredLayoutManager.setBounds(UIBaseLayer, new Rectangle(1000, 1000, 1000, 1000));
        this.add(UILayeredPane);*/
    }

    public void initialiseUI() {
        try {
            Ship s = findPlayerShip();

            initaliseWeapons(s);
            initialiseResources(s);
            initialiseScreen();
            addAllComponents();

        } catch (Exception e) {
            System.out.println("Unable to find the Ship");
            e.printStackTrace();
        }
    }

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

        this.add(screen, BorderLayout.CENTER);
        this.add(UIPanel, BorderLayout.SOUTH);
        System.out.println("Done creating the UI. I am the Engineer");

        this.revalidate();
        this.repaint();
    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initaliseWeapons(Ship s) {
        plasmaBlasterView = new WeaponView("Plasma Blaster", true);

        // default plasma blaster to be highlighted, remove at a later date!
        // plasmaBlasterView.setHighlightWeapon(true);

        laserBlasterView = new WeaponView("Laser Blaster", true);
        torpedosView = new WeaponView("Torpedos", true);
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
        resourcesView = new ResourcesView();
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
        screen = new Screen(playerNickname, false);
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
        if(!UIinitialised) {
            keySequences = gameClient.keySequence;
            initialiseUI();
            UIinitialised = true;
        }

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

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println("Recieved a keypress");
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            state = ShipState.NONE;
            keyManager.deactiveate();
        } else {
            if (keyManager.isActive()) {
                System.out.println("Passing " + keyEvent.getKeyChar() + " to the sequence manager");
                keyManager.keyPressed(keyEvent);
            } else {
                switch (keyEvent.getKeyChar()) {
                    case 'l':
                        System.out.println("Starting a laser sequence");
                        this.state = ShipState.LASER_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[0]), false);
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
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[3]), false);
                        break;
                    case 'f':
                        System.out.println("Starting a fuel sequence");
                        this.state = ShipState.FUEL_REPLENISH;
                        keyManager.initialiseKeySequenceManager(String.valueOf(keySequences[4]), false);
                        break;

                }
            }
        }
    }

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
                System.out.println("Adding more lasers");
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

    public void keySequenceFailed() {
        System.out.println("Bad key!!!!!");

        this.state = ShipState.NONE;
    }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
