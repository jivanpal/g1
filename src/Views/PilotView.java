package Views;

import ClientNetworking.GameClient.GameClientReceiver;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import UI.ClientShipObservable;

import javax.swing.*;
import javax.swing.border.Border;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import GameLogic.GameOptions;
import sun.swing.UIClientPropertyKey;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel implements KeyListener, Observer {
    private Screen screen;
    private SpeedometerView speedometerView;
    private WeaponView plasmaBlasterView;
    private WeaponView laserBlasterView;
    private WeaponView torpedosView;
    private InstructionsView instructionsView;

    private GameClient gameClient;
    private String playerNickname;

    private boolean UIinitialised = false;

    // private JLayeredPane UIContainer;

    /**
     * Creates a new PilotView.
     *
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public PilotView(String playerNickname, GameClient gameClient) {
        super();

        // Allow the server to fully initialise before we go and try get values from it.
        // TODO: Show a loading screen? Do this more elegantly?
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        addKeyListener(this);
        setFocusable(true);

        try {
            initialiseUI();
            UIinitialised = true;
        } catch (Exception e) {
            // Oh well.
        }

        // starting the in-game sounds
        /*try {
            AudioPlayer.stopMusic();
			AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
		} catch (Exception e) {
			// TODO: Fix
			// In game sound failed to load? Hopefully the game will no longer hang. This fix
			// doesn't appear to work. Never starting the sound allows me to load though? - James
			AudioPlayer.stopMusic();
			AudioPlayer.stopSoundEffect();
			e.printStackTrace();

			getParent().revalidate();
			getParent().repaint();
		}*/
    }

    /**
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    public void initialiseUI() throws Exception {
        Ship s = findPlayerShip();

        if(s == null) {
            throw new Exception("Ship is null");
        }

        initialiseWeapons(s);
        initialiseInstructions();
        intialiseSpeedometer();
        initialiseScreen();

        // Add mouse listener which swaps the cursor between being the default and a crosshair.
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                final int x = mouseEvent.getX();
                final int y = mouseEvent.getY();

                final Rectangle screenBounds = screen.getBounds();
                if (screenBounds != null && screenBounds.contains(x, y)) {
                    getParent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        addAllComponents();
        System.out.println("Done initialising the UI. I am the Pilot");
    }

    /**
     * Add all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        this.setLayout(new BorderLayout());
        Container weaponPanel = new Container();
        weaponPanel.add(plasmaBlasterView);
        weaponPanel.add(laserBlasterView);
        weaponPanel.add(torpedosView);
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));

        Container UIpanel = new Container();
        UIpanel.add(weaponPanel);
        UIpanel.add(speedometerView);
        UIpanel.add(instructionsView);
        UIpanel.setLayout(new BoxLayout(UIpanel, BoxLayout.X_AXIS));

        this.add(screen, BorderLayout.CENTER);
        this.add(UIpanel, BorderLayout.SOUTH);
        this.add(new JButton("Test"), BorderLayout.NORTH);
        System.out.println("Done adding all components");
        this.revalidate();
        this.repaint();
    }

    /**
     * Initialise the Screen for the UI
     */
    private void initialiseScreen() {
        this.screen = new Screen(playerNickname, true);
        screen.setPreferredSize(new Dimension(1000, 800));
    }

    /**
     * Initialises the speedometer to its initial values.
     */
    private void intialiseSpeedometer() {
        speedometerView = new SpeedometerView();
    }

    /**
     * Initialises the InstructionsView with all of the relevant instructions for this ship.
     */
    private void initialiseInstructions() {
        instructionsView = new InstructionsView();

        for (int i = 0; i < gameClient.keySequence.length; i++) {
            String instruction = String.valueOf(gameClient.keySequence[i]);
            instructionsView.addInstruction(instruction);
        }

    }

    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseWeapons(Ship s) {
        if (s != null) {
            plasmaBlasterView = new WeaponView("Plasma Blaster", false);
            laserBlasterView = new WeaponView("Laser Blaster", false);
            torpedosView = new WeaponView("Torpedos", false);
        } else {
            System.out.println("Ship is null? Oh dear oh dear");
        }
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

                if (s.getPilotName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        throw new Exception("ERROR: Couldn't find the players ship!");
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON)) {
            gameClient.send("fireWeapon1");
        } else if (keyEvent.getKeyCode() == GameOptions
                .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON)) {
            gameClient.send("fireWeapon2");
        } else if (keyEvent.getKeyCode() == GameOptions
                .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON)) {
            gameClient.send("fireWeapon3");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ACCELERATE_BUTTON)) {
            gameClient.send("accelerate");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_DECELERATE_BUTTON)) {
            gameClient.send("decelerate");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_DOWN_BUTTON)) {
            gameClient.send("pitchDown");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_UP_BUTTON)) {
            gameClient.send("pitchUp");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON)) {
            gameClient.send("rollLeft");
        } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)) {
            gameClient.send("rollRight");
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void update(Observable observable, Object o) {

        if (!UIinitialised) {
            try {
                initialiseUI();
                UIinitialised = true;
            } catch (Exception e) {
                // Oh well.
                System.out.println("Update Error: " + e.getMessage());
                System.out.println("Localised message: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } else {
            Map m = gameClient.getMap();
            screen.setMap(m);
        }
    }
}
