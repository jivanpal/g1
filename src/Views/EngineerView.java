package Views; // from the 6

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
public class EngineerView extends JPanel implements KeySequenceResponder, Observer {
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
    private final int SHIELD_MAX_NUM = 5;
    private int fuelSequenceNum = 6;
    private final int FUEL_MAX_NUM = 12;
    private int laserSequenceNum = 13;
    private final int LASER_MAX_NUM = 19;
    private int plasmaSequenceNum = 20;
    private final int PLASMA_MAX_NUM = 26;
    private int torpedoSequenceNum = 27;
    private final int TORPEDO_MAX_NUM = 33;

    private final int ALLOWED_DEFAULT = 30;
    private int shieldAllowedNum = ALLOWED_DEFAULT; // Amount of key sequence passes we are allowed at this difficulty of sequence
    private int fuelAllowedNum = ALLOWED_DEFAULT;
    private int laserAllowedNum = ALLOWED_DEFAULT;
    private int plasmaAllowedNum = ALLOWED_DEFAULT;
    private int torpedoAllowedNum = ALLOWED_DEFAULT;

    private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;
    public JFrame parentFrame;
    private GameChat chatWindow;

    private Ship previousShip = null;
    private Ship currentShip = null;
    
    public ArrayList<JButton> replenishButtons;

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
                System.out.println("Key pressed");
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

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse clicked outside of chat");
                    parentFrame.requestFocusInWindow();
                } else {
                    System.out.println("Mouse clicked inside chat");
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse pressed outside of chat");
                    parentFrame.requestFocusInWindow();
                } else {
                    System.out.println("Mouse pressed inside chat");
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse released outside of chat");
                    parentFrame.requestFocusInWindow();
                } else {
                    System.out.println("Mouse released inside chat");
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
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    private void initialiseUI() {
        if(UIinitialised) {
            UILayeredPane.removeAll();
            UIBaseLayer.removeAll();
        }

        currentShip = findPlayerShip();
        while (currentShip == null) {
            System.out.println("ship is null");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentShip = findPlayerShip();
        }

        replenishButtons = new ArrayList<JButton>();
        
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
        System.out.println("Done initialising the UI. I am the Engineer");

        parentFrame.requestFocusInWindow();
        parentFrame.setFocusable(true);
        // parentFrame.setUndecorated(true);
        // parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        update(null, null);
        screen.setMap(gameClient.getMap());
        screen.revalidate();
        screen.repaint();

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

        resourcesView.setOpaque(false);
        UIPanel.add(resourcesView, uiPanelConstraints);

        uiPanelConstraints.weightx = 1;
        uiPanelConstraints.gridx = 1;
        
        //uiPanelConstraints.anchor = GridBagConstraints.EAST;
        UIPanel.add(weaponPanel, uiPanelConstraints);

        // TODO: Make this work.
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

        radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4, parentFrame.getHeight() / 4);
        radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));

        UILayeredPane.add(radarView, JLayeredPane.MODAL_LAYER);

        chatWindow.setBounds(0,
                parentFrame.getHeight() - ((int) UIPanel.getPreferredSize().getHeight() + (parentFrame.getHeight() / 6)),
                parentFrame.getWidth() / 6,
                parentFrame.getHeight() / 6);
        chatWindow.setPreferredSize(new Dimension(parentFrame.getWidth() / 6, parentFrame.getHeight() / 6));
        UILayeredPane.add(chatWindow, JLayeredPane.PALETTE_LAYER);
    }

    private void initialiseChatWindow(GameClient gameClient, String nickname) {
        this.chatWindow = new GameChat(this, gameClient, nickname);
        this.chatWindow.setFocusable(false);
    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     * @param s This players Ship object
     */
    private void initialiseWeapons(Ship s) {
        String plasmaReplenishNumber = parseNumber(keySequences.get(plasmaSequenceNum));
        String laserReplenishNumber = parseNumber(keySequences.get(laserSequenceNum));
        String torpedoReplenishNumber = parseNumber(keySequences.get(torpedoSequenceNum));

        plasmaBlasterView = new WeaponView("Plasma Blaster", true, plasmaReplenishNumber, replenishButtons);

        // default plasma blaster to be highlighted, remove at a later date!
        // plasmaBlasterView.setHighlightWeapon(true);

        laserBlasterView = new WeaponView("Laser Blaster", true, laserReplenishNumber, replenishButtons);
        torpedosView = new WeaponView("Torpedos", true, torpedoReplenishNumber, replenishButtons);

        laserBlasterView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.LASER).getAmmoMaximum());
        plasmaBlasterView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.PLASMA).getAmmoMaximum());
        torpedosView.setMaxiumumAmmo(s.getWeapon(Weapon.Type.TORPEDO).getAmmoMaximum());

        laserBlasterView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.LASER).getAmmoLevel());
        plasmaBlasterView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.PLASMA).getAmmoLevel());
        torpedosView.updateWeaponAmmoLevel(s.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel());

        laserBlasterView.setReplenishAmmo(this, ShipState.LASER_REPLENISH);
        plasmaBlasterView.setReplenishAmmo(this, ShipState.PLASMA_REPLENISH);
        torpedosView.setReplenishAmmo(this, ShipState.TORPEDO_REPLENISH);
    }

    /**
     * Given a Ship, this will initialise the resource progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseResources(Ship s) {
        System.out.println("Shield sequence: " + keySequences.get(shieldSequenceNum));
        System.out.println("Fuel sequence: " + keySequences.get(fuelSequenceNum));

        String shieldSequenceNumber = parseNumber(keySequences.get(shieldSequenceNum));
        String fuelSequenceNumber = parseNumber(keySequences.get(fuelSequenceNum));

        resourcesView = new ResourcesView(this, shieldSequenceNumber, fuelSequenceNumber, replenishButtons);
        
        resourcesView.updateResourceLevels(ResourcesView.ENGINE,     s.getResource(Resource.Type.ENGINES).get());
        resourcesView.updateResourceLevels(ResourcesView.SHIELDS,    s.getResource(Resource.Type.SHIELDS).get());
        resourcesView.updateResourceLevels(ResourcesView.HULL,       s.getResource(Resource.Type.HEALTH).get());
        
        resourcesView.setMaximumResourceLevel(ResourcesView.ENGINE,  s.getResource(Resource.Type.ENGINES).getMax());
        resourcesView.setMaximumResourceLevel(ResourcesView.SHIELDS, s.getResource(Resource.Type.SHIELDS).getMax());
        resourcesView.setMaximumResourceLevel(ResourcesView.HULL,    s.getResource(Resource.Type.HEALTH).getMax());
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
                radarView.setVisible(false);
                if(!radarView.isLargeView()) {
                    radarView.setLargeView(true);
                    radarView.setBounds(50, 50, getWidth() - 100, getHeight() - 100);
                    radarView.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
                } else {
                    radarView.setLargeView(false);
                    radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4, parentFrame.getHeight() / 4);
                    radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));
                }
                radarView.setVisible(true);

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

        for (Body b : m.bodies()) {
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

            for (int i = 0; i < m.size(); i++) {
                if (m.get(i) instanceof Ship) {
                    Ship s = (Ship) m.get(i);

                    if (s.getEngineerName().equals(playerNickname)) {
                        previousShip = currentShip;
                        currentShip = s;

                        if (currentShip.getWeapon(Weapon.Type.LASER).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.LASER).getAmmoLevel()) {
                            AudioPlayer.playSoundEffect(AudioPlayer.LASER_FIRE_EFFECT);
                        }
                        laserBlasterView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.LASER).getAmmoLevel());

                        plasmaBlasterView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel());
                        if (currentShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel()) {
                            AudioPlayer.playSoundEffect(AudioPlayer.PLASMA_FIRE_EFFECT);
                        }

                        torpedosView.updateWeaponAmmoLevel(currentShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel());
                        if (currentShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel()) {
                            AudioPlayer.playSoundEffect(AudioPlayer.TORPEDO_FIRE_EFFECT);
                        }

                        resourcesView.updateResourceLevels(ResourcesView.ENGINE,  currentShip.getResource(Resource.Type.ENGINES).get());

                        if(currentShip.getResource(Resource.Type.SHIELDS).get() < previousShip.getResource(Resource.Type.SHIELDS).get()) {
                            AudioPlayer.playSoundEffect(AudioPlayer.SHIELD_DECREASE_EFFECT);
                        }
                        resourcesView.updateResourceLevels(ResourcesView.SHIELDS, currentShip.getResource(Resource.Type.SHIELDS).get());

                        if(currentShip.getResource(Resource.Type.HEALTH).get() < previousShip.getResource(Resource.Type.HEALTH).get()) {
                            AudioPlayer.playSoundEffect(AudioPlayer.SHIP_HEALTH_DECREASE_EFFECT);
                        }
                        resourcesView.updateResourceLevels(ResourcesView.HULL,    currentShip.getResource(Resource.Type.HEALTH).get());
                    }
                }
            }
        }
    }

    /**
     * Called by the KeyManager when a key sequence has been completed in it's entirety. Depending on the current state
     * of the ship, we tell the server what the user has done.
     */
    public void keySequencePassed() {
        switch (state) {
            case NONE:
                break;
            case SHIELD_REPLENISH:
                System.out.println("Sending shieldReplenish");
                shieldAllowedNum--;
                if(shieldAllowedNum <= 0 && (shieldSequenceNum < SHIELD_MAX_NUM)) {
                    keyManager.deactivate();
                    shieldAllowedNum = ALLOWED_DEFAULT;
                    shieldSequenceNum += 1;
                    this.state = ShipState.NONE;
                }
                resourcesView.updateRefreshNumber(ResourcesView.SHIELDS, parseNumber(keySequences.get(shieldSequenceNum)));

                gameClient.send("shieldReplenish");
                break;
            case FUEL_REPLENISH:
                System.out.println("Sending fuelReplenish");

                fuelAllowedNum--;
                if(fuelAllowedNum <= 0 && (fuelSequenceNum < FUEL_MAX_NUM)) {
                    keyManager.deactivate();
                    fuelAllowedNum = ALLOWED_DEFAULT;
                    fuelSequenceNum += 1;
                    this.state = ShipState.NONE;
                }
                resourcesView.updateRefreshNumber(ResourcesView.ENGINE, parseNumber(keySequences.get(fuelSequenceNum)));

                gameClient.send("fuelReplenish");
                break;
            case LASER_REPLENISH:
                System.out.println("Sending laserReplenish");

                laserAllowedNum--;
                if(laserAllowedNum <= 0 && (laserSequenceNum < LASER_MAX_NUM)) {
                    keyManager.deactivate();
                    laserAllowedNum = ALLOWED_DEFAULT;
                    laserSequenceNum += 1;
                    this.state = ShipState.NONE;
                }
                laserBlasterView.setReplenishAmmoNumber(parseNumber(keySequences.get(laserSequenceNum)));

                gameClient.send("laserReplenish");
                break;
            case TORPEDO_REPLENISH:
                System.out.println("Sending torpedoReplenish");

                torpedoAllowedNum--;
                if(torpedoAllowedNum <= 0 && (torpedoSequenceNum < TORPEDO_MAX_NUM)) {
                    keyManager.deactivate();
                    torpedoAllowedNum = ALLOWED_DEFAULT;
                    torpedoSequenceNum += 1;
                    this.state = ShipState.NONE;
                }
                torpedosView.setReplenishAmmoNumber(parseNumber(keySequences.get(torpedoSequenceNum)));

                gameClient.send("torpedoReplenish");
                break;
            case PLASMA_REPLENISH:
                System.out.println("Sending plasmaReplenish");

                plasmaAllowedNum--;
                if(plasmaAllowedNum <= 0 && (plasmaSequenceNum < PLASMA_MAX_NUM)) {
                    keyManager.deactivate();
                    plasmaAllowedNum = ALLOWED_DEFAULT;
                    plasmaSequenceNum += 1;
                    this.state = ShipState.NONE;
                }
                plasmaBlasterView.setReplenishAmmoNumber(parseNumber(keySequences.get(plasmaSequenceNum)));

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
                keyManager.deactivate();
                changeButton("none");
                break;
            case SHIELD_REPLENISH:
                System.out.println("Starting a shield sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                changeButton("Shields");
                break;
            case FUEL_REPLENISH:
                System.out.println("Starting a fuel sequence");
                sequence = parseSequence(keySequences.get(fuelSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                changeButton("Engines");
                break;
            case LASER_REPLENISH:
                System.out.println("Starting a laser sequence");
                sequence = parseSequence(keySequences.get(laserSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, true);
                changeButton("Laser Blaster");
                break;
            case TORPEDO_REPLENISH:
                System.out.println("Starting a torpedo sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, false);
                changeButton("Torpedos");
                break;
            case PLASMA_REPLENISH:
                System.out.println("Starting a plasma sequence");
                sequence = parseSequence(keySequences.get(shieldSequenceNum));
                keyManager.initialiseKeySequenceManager(sequence, false);
                changeButton("Plasma Blaster");
                break;
        }
    }
    
    private void changeButton(String state) {
    	for(JButton b : replenishButtons) {
    		if (state.equals(b.getName())) {
    			b.setBackground(Color.decode("#ff3333"));
    		} else {
    			b.setBackground(Color.decode("#cccccc"));
    		}
    	}
    }

    private String parseNumber(String sequenceWithNum) {
        return sequenceWithNum.split(":")[0];
    }

    private String parseSequence(String sequenceWithNum) {
        return sequenceWithNum.split(":")[1];
    }

    public ShipState getState() { return this.state; }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
