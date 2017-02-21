package Views;

import javax.swing.*;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import Graphics.Screen;
import UI.ClientShipObservable;

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

    private JLayeredPane UIContainer;

    public EngineerView(String playerNickname, GameClient gameClient) {

        UIContainer.setLayout(new LayoutManager() {
            private Map<Component, Rectangle> bounds = new LinkedHashMap<Component, Rectangle>();

            @Override
            public void addLayoutComponent(String s, Component component) {
                bounds.put(component, new Rectangle(component.getPreferredSize()));
            }

            @Override
            public void removeLayoutComponent(Component component) {
                bounds.remove(component);
            }

            @Override
            public Dimension preferredLayoutSize(Container container) {
                Rectangle rect = new Rectangle();
                for (Rectangle r : bounds.values()) {
                    rect = rect.union(r);
                }

                return rect.getSize();
            }

            @Override
            public Dimension minimumLayoutSize(Container container) {
                return preferredLayoutSize(container);
            }

            @Override
            public void layoutContainer(Container container) {
                for(Map.Entry<Component, Rectangle> entry : bounds.entrySet()) {
                    entry.getKey().setBounds(entry.getValue());
                }
            }

            public void setBounds(Component c, Rectangle bounds) {
                this.bounds.put(c, bounds);
            }
        });

        this.setLayout(new BorderLayout());
        this.gameClient = gameClient;
        gameClient.addObserver(this);
        screen = new Screen(playerNickname, false);
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));
        this.add(screen, BorderLayout.CENTER);

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

        this.add(UIPanel, BorderLayout.SOUTH);

        // starting the in-game sounds
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);

        keyManager = new KeySequenceManager(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof ClientShipObservable) {
            ClientShipObservable shipObservable = (ClientShipObservable) observable;

            resourcesView.updateResourceLevels(ResourcesView.SHIELDS, shipObservable.getShipShields());
            resourcesView.updateResourceLevels(ResourcesView.HULL, shipObservable.getShipHealth());
            resourcesView.updateResourceLevels(ResourcesView.ENGINE, shipObservable.getShipFuel());

            laserBlasterView.updateWeaponAmmoLevel(shipObservable.getLaserAmmo());
            plasmaBlasterView.updateWeaponAmmoLevel(shipObservable.getPlasmaAmmo());
            torpedosView.updateWeaponAmmoLevel(shipObservable.getTorpedoAmmo());
        } else
            screen.setMap(gameClient.getMap());
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
                break;
            case FUEL_REPLENISH:
                break;
            case LASER_REPLENISH:
                break;
            case TORPEDO_REPLENISH:
                break;
            case PLASMA_REPLENISH:
                break;
        }
    }

    public void keySequenceFailed() {
    }
}

enum ShipState {
    NONE, SHIELD_REPLENISH, FUEL_REPLENISH, LASER_REPLENISH, TORPEDO_REPLENISH, PLASMA_REPLENISH
}
