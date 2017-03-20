package Views;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Graphics.Screen;
import Menus.MainMenu;
import Physics.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by James on 16/03/17.
 * Abstract class that both PilotView and EngineerView will extend from
 */
public abstract class AbstractPlayerView extends JPanel implements Observer {
    protected final String LOSE_GAME = "DEFEAT";
    protected final String WIN_GAME = "VICTORY";

    protected final int DAMAGE_FLASH_TIME = 100; // ms

    protected String playerNickname;
    protected GameClient gameClient;

    protected JFrame parentFrame;
    protected JLayeredPane UILayeredPane;

    protected Screen screen;
    protected GameChat chatWindow;
    protected JLabel fullScreenLabel;
    protected RadarView radarView;

    protected Ship currentShip = null;  // The ship in this current tick
    protected Ship previousShip = null; // The Ship in the last tick

    protected boolean gameActive = true;

    public AbstractPlayerView(String playerNickname, GameClient gameClient, JFrame parent) {
        super();

        this.playerNickname = playerNickname;
        this.gameClient = gameClient;
        this.parentFrame = parent;
        this.UILayeredPane = parent.getLayeredPane();
    }

    // Initialse all the UI elements
    protected abstract void initialiseUI();

    // Flash elements of the UI a certain color for a very short amount of time to indicate that the Ship has been damaged
    protected abstract void flashUIDamaged();

    /**
     * Checks if the player has won or lost. Updates the screen. Leaves the Subclasses free to Override and implement
     * any extra work that is relevant just to them.
     * @param observable The MapContainer
     * @param o Unused
     */
    public void update(Observable observable, Object o) {
        if(gameActive) {
            try {
                // long time = System.currentTimeMillis();

                Map m = ((ClientNetworking.GameClient.MapContainer) observable).getMap();

                if (hasWonGame(m)) {
/*
                    *
                     * TODO: Victory is temporarilly off for testing. It's annoying to join the game as a single
                     * player and instantly win!
*/

//                    // Congrats!
//                    gameActive = false;
//
//                    AudioPlayer.playSoundEffect(AudioPlayer.VICTORY_EFFECT);
//                    displayFullScreenMessage(WIN_GAME, 5000, Color.green);
//
//                    Timer t = new Timer(5000, actionEvent -> {
//                        System.out.println("I'm going back to the main menu.");
//                        swapToMainMenu();
//                    });
//                    t.setRepeats(false);
//                    t.start();
                } else if (hasLostGame(m)) {
                    // Commiserations
                    gameActive = false;

                    AudioPlayer.playSoundEffect(AudioPlayer.FAILURE_EFFECT);
                    displayFullScreenMessage(LOSE_GAME, 5000, Color.RED);

                    Timer t = new Timer(5000, actionEvent -> {
                        System.out.println("I'm going back to the main menu.");
                        swapToMainMenu();
                    });
                }

                screen.setMap(m);

                Ship s = findPlayerShip(m);
                previousShip = currentShip;
                currentShip = s;


                if (currentShip.getWeapon(Weapon.Type.LASER).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.LASER).getAmmoLevel()) {
                    AudioPlayer.playSoundEffect(AudioPlayer.LASER_FIRE_EFFECT);
                }
                if (currentShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.PLASMA).getAmmoLevel()) {
                    AudioPlayer.playSoundEffect(AudioPlayer.PLASMA_FIRE_EFFECT);
                }
                if (currentShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel() < previousShip.getWeapon(Weapon.Type.TORPEDO).getAmmoLevel()) {
                    AudioPlayer.playSoundEffect(AudioPlayer.TORPEDO_FIRE_EFFECT);
                }
                if (currentShip.getResource(Resource.Type.SHIELDS).get() < previousShip.getResource(Resource.Type.SHIELDS).get()) {
                    AudioPlayer.playSoundEffect(AudioPlayer.SHIP_SHIELD_DECREASE_EFFECT);
                }
                if (currentShip.getResource(Resource.Type.HEALTH).get() < previousShip.getResource(Resource.Type.HEALTH).get()) {
                    AudioPlayer.playSoundEffect(AudioPlayer.SHIP_HEALTH_DECREASE_EFFECT);
                }

                // System.out.println("Time this frame: " + String.valueOf(System.currentTimeMillis() - time));
            } catch (NullPointerException e) {
                System.err.println("Map seems to be null right now... let's just wait a little bit");
            }
        }
    }

    /**
     * Finds the players ship from within the map
     * @param m The current map
     * @return The ship
     */
    protected Ship findPlayerShip(Map m) {
        try {
            return m.bodies().parallelStream()
                    .filter(b -> b instanceof Ship)
                    .map(Ship.class::cast)
                    .filter(s -> s.getEngineerName().equals(playerNickname) || s.getPilotName().equals(playerNickname))
                    .collect(Collectors.toList())
                    .get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns whether this Ship has won the game or not
     * @param m The current Map
     * @return True if we have won the game, otherwise false
     */
    protected boolean hasWonGame(Map m) {
         return m.bodies().parallelStream()
                .filter(b -> b instanceof Ship)
                .map(Ship.class::cast)
                .filter(s -> !s.getEngineerName().equals(playerNickname) && !s.getPilotName().equals(playerNickname))
                .collect(Collectors.toList())
                .size() == 0;
    }

    /**
     * Returns whether this Ship has lost the game or not
     * @param m The current map
     * @return True if we have lost the game, otherwise false
     */
    protected boolean hasLostGame(Map m) {
        return m.bodies().parallelStream()
                .filter(b -> b instanceof Ship)
                .map(Ship.class::cast)
                .filter(s -> s.getPilotName().equals(playerNickname) || s.getEngineerName().equals(playerNickname))
                .collect(Collectors.toList())
                .size() == 0;
    }

    /**
     * Displays a message in the middle of the screen
     * @param message The message to display
     * @param time How long to display the message for
     * @param textColor The color of the text
     */
    protected void displayFullScreenMessage(String message, int time, Color textColor) {
        fullScreenLabel = new JLabel(message);
        fullScreenLabel.setFont(GameOptions.FULLSCREEN_BOLD_TEXT_FONT);
        fullScreenLabel.setForeground(textColor);

        Graphics g = parentFrame.getGraphics();
        FontMetrics metrics = g.getFontMetrics(GameOptions.FULLSCREEN_BOLD_TEXT_FONT);
        int textWidth = metrics.stringWidth(message);
        int textHeight = metrics.getHeight();

        fullScreenLabel.setPreferredSize(new Dimension(textWidth + 2, textHeight + 2));
        fullScreenLabel.setBounds((parentFrame.getWidth() / 2) - ((textWidth + 2) / 2),
                (parentFrame.getHeight() / 2) - ((textHeight + 2) / 2),
                textWidth + 2,
                textHeight + 2);

        UILayeredPane.add(fullScreenLabel, JLayeredPane.MODAL_LAYER);

        Timer timer = new Timer(time, actionEvent -> {
            fullScreenLabel.setVisible(false);
            UILayeredPane.remove(fullScreenLabel);
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Swap control back to the main menu of the game
     */
    protected void swapToMainMenu() {
        MainMenu menu = new MainMenu(playerNickname);
        parentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        parentFrame.dispatchEvent(new WindowEvent(parentFrame, WindowEvent.WINDOW_CLOSING));
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.MENU_SCREEN_TUNE);
    }

    /**
     * Creates the RadarView
     */
    protected void initialiseRadar() {
        this.radarView = new RadarView(playerNickname, gameClient.getMap());

        // Create a mouse listener to check when the user has clicked the RadarView
        this.radarView.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                radarView.setVisible(false);

                // If currently small, make large. If currently large, make small.
                if (!radarView.isLargeView()) {
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
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }
}
