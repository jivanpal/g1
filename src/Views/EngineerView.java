package Views;

import javax.swing.*;
import javax.swing.border.Border;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.Ship;
import Graphics.Screen;
import UI.ClientShipObservable;

import GameLogic.Map;

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel implements KeyListener, Observer {

    private final ShipState state = ShipState.NONE;
    private final KeySequenceManager keyManager;

    private final WeaponView plasmaBlasterView;
    private final WeaponView laserBlasterView;
    private final WeaponView torpedosView;

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

        /*UILayeredPane = new JLayeredPane();
        JLayeredPaneLayoutManager layeredLayoutManager = new JLayeredPaneLayoutManager();
        UILayeredPane.setLayout(layeredLayoutManager);

        UIBaseLayer = new JPanel();
        UIBaseLayer.setLayout(new BorderLayout());*/

        this.setLayout(new BorderLayout());
        screen = new Screen(playerNickname, false);
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));
        // UIBaseLayer.add(screen, BorderLayout.CENTER);

        Container UIPanel = new Container();
        UIPanel.setLayout(new BoxLayout(UIPanel, BoxLayout.X_AXIS));

        resourcesView = new ResourcesView();
        resourcesView.setSize(new Dimension(1000, 200));
        resourcesView.setMaximumSize(new Dimension(1000, 200));
        resourcesView.setMinimumSize(new Dimension(1000, 200));
        resourcesView.setPreferredSize(new Dimension(1000, 200));

        plasmaBlasterView = new WeaponView("Plasma Blaster", true);

        // default plasma blaster to be highlighted, remove at a later date!
        plasmaBlasterView.setHighlightWeapon(true);

        laserBlasterView = new WeaponView("Laser Blaster", true);
        torpedosView = new WeaponView("Torpedos", true);

        Container weaponPanel = new Container();
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));
        weaponPanel.add(plasmaBlasterView);
        weaponPanel.add(laserBlasterView);
        weaponPanel.add(torpedosView);

        UIPanel.add(resourcesView);
        UIPanel.add(weaponPanel);

        // UIBaseLayer.add(UIPanel, BorderLayout.SOUTH);

        // starting the in-game sounds
        /*AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);*/

        keyManager = new KeySequenceManager(this);

        /*UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);
        layeredLayoutManager.setBounds(UIBaseLayer, new Rectangle(1000, 1000, 1000, 1000));
        this.add(UILayeredPane);*/

        this.add(screen, BorderLayout.CENTER);
        this.add(UIPanel, BorderLayout.SOUTH);
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
        keyManager.keyPressed(keyEvent);
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
