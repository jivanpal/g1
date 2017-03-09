package Views; // from the 6

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Graphics.Screen;
import Physics.Body;

/**
 * Created by James on 01/02/17.
 * This View contains the entire UI for the Engineer once they are in game.
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
    private RadarView radarView;

    private GameClient gameClient;
    private String playerNickname;

    private ArrayList<String> keySequences;
    private int shieldSequenceNum = 0;
    private int fuelSequenceNum = 6;
    private int laserSequenceNum = 13;
    private int plasmaSequenceNum = 20;
    private int torpedoSequenceNum = 27;

    private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;
    private JFrame parentFrame;

    /**
     * Creates a new EngineerView
     * Creates a new EngineerView.
     *
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public EngineerView(String playerNickname, GameClient gameClient, JFrame parent) {
        super();

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        keyManager = new KeySequenceManager(this);

        addKeyListener(this);
        setFocusable(true);

        this.parentFrame = parent;
        UILayeredPane = parent.getLayeredPane();
        UIBaseLayer = new JPanel();

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
                    System.out.println("Stopping this sequence");
                    state = ShipState.NONE;
                    keyManager.deactivate();
                } else {
                    if (keyManager.isActive()) {
                        // If we're half way through a sequence, pass the key off to the key manager.
                        keyManager.keyPressed(keyEvent);
                    } else {
                        // We're not in a sequence, see if this key was a keybind to start a new one.
                        String sequence = "";
                        switch (keyEvent.getKeyChar()) {
                            case 'l':
                                System.out.println("Starting a laser sequence");
                                state = ShipState.LASER_REPLENISH;
                                sequence = parseSequence(keySequences.get(laserSequenceNum));
                                keyManager.initialiseKeySequenceManager(sequence, true);
                                break;
                            case 't':
                                System.out.println("Starting a torpedo sequence");
                                state = ShipState.TORPEDO_REPLENISH;
                                sequence = parseSequence(keySequences.get(torpedoSequenceNum));
                                keyManager.initialiseKeySequenceManager(sequence, false);
                                break;
                            case 'p':
                                System.out.println("Starting a plasma sequence");
                                state = ShipState.PLASMA_REPLENISH;
                                sequence = parseSequence(keySequences.get(plasmaSequenceNum));
                                keyManager.initialiseKeySequenceManager(sequence, false);
                                break;
                            case 's':
                                System.out.println("Starting a shield sequence");
                                state = ShipState.SHIELD_REPLENISH;
                                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                                keyManager.initialiseKeySequenceManager(sequence, true);
                                break;
                            case 'f':
                                System.out.println("Starting a fuel sequence");
                                state = ShipState.FUEL_REPLENISH;
                                sequence = parseSequence(keySequences.get(fuelSequenceNum));
                                keyManager.initialiseKeySequenceManager(sequence, true);
                                break;
                        }
                    }
                }
            }
        });

        initialiseUI();

        // starting the in-game sounds
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
    }

    /**
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    private void initialiseUI() {
        if(UIinitialised) {
            UILayeredPane.removeAll();
            UIBaseLayer.removeAll();
        }

        Ship s = findPlayerShip();
        while (s == null) {
            System.out.println("ship is null");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s = findPlayerShip();
        }

        initialiseWeapons(s);
        initialiseResources(s);
        initialiseScreen();
        initialiseRadar();
        addAllComponents();

        this.revalidate();
        this.repaint();
        UIBaseLayer.revalidate();
        UIBaseLayer.repaint();
        UILayeredPane.revalidate();
        UILayeredPane.repaint();

        this.addKeyListener(this);

        this.UIinitialised = true;
        System.out.println("Done initialising the UI. I am the Engineer");
    }

    /**
     * Adds all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        this.setLayout(new BorderLayout());

        JPanel weaponPanel = new JPanel();
        weaponPanel.setLayout(new GridBagLayout());
        GridBagConstraints weaponConstraints = new GridBagConstraints();
        weaponConstraints.weightx = 0;
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

        JPanel UIPanel = new JPanel();
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
        UIPanel.add(resourcesView, uiPanelConstraints);

        uiPanelConstraints.gridx = 1;
        
        //uiPanelConstraints.anchor = GridBagConstraints.EAST;
        UIPanel.add(weaponPanel, uiPanelConstraints);

        // TODO: Make this work.
        /*UIPanel.setOpaque(true);
        UIPanel.setForeground(ViewConstants.UI_BACKGROUND_COLOR);
        UIPanel.setBackground(ViewConstants.UI_BACKGROUND_COLOR);
        UIPanel.paintComponents(getGraphics());*/

        UIBaseLayer.setLayout(new BorderLayout());
        UIBaseLayer.add(screen, BorderLayout.CENTER);
        UIBaseLayer.add(UIPanel, BorderLayout.SOUTH);
        UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());
        JLayeredPaneLayoutManager layoutManager = new JLayeredPaneLayoutManager();

        UILayeredPane.setLayout(layoutManager);
        UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);

        radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4, parentFrame.getHeight() / 4);
        radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));

        UILayeredPane.add(radarView, JLayeredPane.PALETTE_LAYER);
    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     * @param s This players Ship object
     */
    private void initialiseWeapons(Ship s) {
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

        laserBlasterView.setReplenishAmmo(this, ShipState.LASER_REPLENISH);
        plasmaBlasterView.setReplenishAmmo(this, ShipState.PLASMA_REPLENISH);
        torpedosView.setReplenishAmmo(this, ShipState.TORPEDO_REPLENISH);
    }

    /**
     * Given a Ship, this will initialse the resource progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseResources(Ship s) {
        System.out.println("Shield sequence: " + keySequences.get(shieldSequenceNum));
        System.out.println("Fuel sequence: " + keySequences.get(fuelSequenceNum));

        String shieldSequenceNumber = parseNumber(keySequences.get(shieldSequenceNum));
        String fuelSequenceNumber = parseNumber(keySequences.get(fuelSequenceNum));

        resourcesView = new ResourcesView(this, shieldSequenceNumber, fuelSequenceNumber);
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
        screen.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - (this.getHeight() / 5)));
        Global.SCREEN_WIDTH = this.getWidth();
        Global.SCREEN_HEIGHT = this.getHeight() - (this.getHeight() / 5);
    }

    private void initialiseRadar() {
        this.radarView = new RadarView(playerNickname, gameClient.getMap());
        this.radarView.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) { }

            @Override
            public void mousePressed(MouseEvent mouseEvent) { }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(!radarView.isLargeView()) {
                    radarView.setLargeView(true);
                    radarView.setBounds(50, 50, getWidth() - 100, getHeight() - 100);
                    radarView.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
                } else {
                    radarView.setLargeView(false);
                    radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4, parentFrame.getHeight() / 4);
                    radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));
                }

                radarView.revalidate();
                radarView.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) { }

            @Override
            public void mouseExited(MouseEvent mouseEvent) { }
        });
    }

    /**
     * Finds the players Ship within all of the objects in the Map
     * @return The players Ship object
     */
    private Ship findPlayerShip() {
        Map m = gameClient.getMap();

        for (Body b : m) {
            if (b instanceof Ship) {
                Ship s = (Ship) b;

                if (s.getEngineerName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (!UIinitialised) {
            try {
                keySequences = gameClient.keySequence.getAllKeys();
            } catch (Exception e) {
                // Should never get here
            }

            initialiseUI();
            UIinitialised = true;
        } else {
            Map m = gameClient.getMap();
            screen.setMap(m);
            radarView.updateMap(m);

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

    void setState(ShipState newState) {
        this.state = newState;

        String sequence = "";

        switch (state) {
            case NONE:
                break;
            case SHIELD_REPLENISH:
                System.out.println("Starting a shield sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                break;
            case FUEL_REPLENISH:
                System.out.println("Starting a fuel sequence");
                sequence = parseSequence(keySequences.get(fuelSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                break;
            case LASER_REPLENISH:
                System.out.println("Starting a laser sequence");
                sequence = parseSequence(keySequences.get(laserSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                break;
            case TORPEDO_REPLENISH:
                System.out.println("Starting a torpedo sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, false);
                break;
            case PLASMA_REPLENISH:
                System.out.println("Starting a plasma sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, false);
                break;
        }
    }

    private String parseNumber(String sequenceWithNum) {
        return sequenceWithNum.split(":")[0];
    }

    private String parseSequence(String sequenceWithNum) {
        return sequenceWithNum.split(":")[1];
    }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
