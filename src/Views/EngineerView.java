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

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel implements KeyListener, Observer {

    private final ShipState state = ShipState.NONE;
    private final KeySequenceManager keyManager;

    private WeaponView plasmaBlasterView;
    private WeaponView laserBlasterView;
    private WeaponView torpedosView;

    private Screen screen;
    private ResourcesView resourcesView;
    private GameClient gameClient;
    private String playerNickname;

    /*private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;*/

    public EngineerView(String playerNickname, GameClient gameClient) {
        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        keyManager = new KeySequenceManager(this);

        initialiseUI();

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
        System.out.println("Done adding all components");
    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     * @param s This players Ship object
     */
    private void initaliseWeapons(Ship s) {
        plasmaBlasterView = new WeaponView("Plasma Blaster", true);

        // default plasma blaster to be highlighted, remove at a later date!
        plasmaBlasterView.setHighlightWeapon(true);

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
     * @return The players Ship object
     * @throws Exception No ship could be found, in theory this should never be called! Hopefully...
     */
    private Ship findPlayerShip() throws Exception {
        Map m = gameClient.getMap();

        for(int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++) {
            if(m.get(i) instanceof Ship) {
                Ship s = (Ship) m.get(i);

                if(s.getEngineerName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        throw new Exception("ERROR: Couldn't find the players ship!");
    }

    @Override
    public void update(Observable observable, Object o) {
        Map m = gameClient.getMap();
        screen.setMap(m);

        for(int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++) {
            if(m.get(i) instanceof Ship) {
                Ship s = (Ship) m.get(i);

                if(s.getEngineerName().equals(playerNickname)) {
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
        if(keyManager.isActive()) {
            keyManager.keyPressed(keyEvent);
        }
    }

    public void keySequencePassed() {
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

    public void keySequenceFailed() {
    }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
