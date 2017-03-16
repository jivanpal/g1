package Views;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.GameOptions;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import Menus.MainMenu;
import Physics.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

/**
 * Created by James on 16/03/17.
 * Abstract class that both PilotView and EngineerView will extend from
 */
public abstract class AbstractPlayerView extends JPanel implements Observer {
    protected final String LOSE_GAME = "DEFEAT";
    protected final String WIN_GAME = "VICTORY";

    protected String playerNickname;
    protected GameClient gameClient;

    protected JFrame parentFrame;
    protected JLayeredPane UILayeredPane;

    protected Screen screen;
    protected GameChat chatWindow;
    protected JLabel fullScreenLabel;

    protected boolean gameActive = true;

    public AbstractPlayerView(String playerNickname, GameClient gameClient, JFrame parent) {
        super();

        this.playerNickname = playerNickname;
        this.gameClient = gameClient;
        this.parentFrame = parent;
        this.UILayeredPane = parent.getLayeredPane();
    }

    protected abstract void initialiseUI();

    /**
     * Checks if the player has won or lost. Updates the screen. Leaves the Subclasses free to Override and implement
     * any extra work that is relevant just to them.
     * @param observable The MapContainer
     * @param o Unused
     */
    public void update(Observable observable, Object o) {
        if(gameActive) {
            try {
                Map m = ((ClientNetworking.GameClient.MapContainer) observable).getMap();

                if (hasWonGame(m)) {
                    /**
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
            } catch (NullPointerException e) {
                System.err.println("Map seems to be null right now... let's just wait a little bit");
            }
        }
    }

    protected Ship findPlayerShip(Map m) {
        for (Body b : m.bodies()) {
            if (b instanceof Ship) {
                Ship s = (Ship) b;
                if (s.getEngineerName().equals(playerNickname) || s.getPilotName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        return null;
    }

    protected boolean hasWonGame(Map m) {
        boolean onlyShipLeft = true;

        for(Body b : m.bodies()) {
            if (b instanceof Ship) {
                Ship s = (Ship) b;
                if (!s.getPilotName().equals(playerNickname) && !s.getEngineerName().equals(playerNickname)) {
                    onlyShipLeft = false;
                    break;
                }
            }
        }

        return onlyShipLeft;
    }

    protected boolean hasLostGame(Map m) {
        boolean haveFoundMyShip = false;

        for(Body b : m.bodies()) {
            if(b instanceof Ship) {
                Ship s = (Ship) b;
                if(s.getPilotName().equals(playerNickname) || s.getEngineerName().equals(playerNickname)) {
                    haveFoundMyShip = true;
                    break;
                }
            }
        }

        return !haveFoundMyShip;
    }

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

    protected void swapToMainMenu() {
        MainMenu menu = new MainMenu(playerNickname);
        parentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        parentFrame.dispatchEvent(new WindowEvent(parentFrame, WindowEvent.WINDOW_CLOSING));
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.MENU_SCREEN_TUNE);
    }
}
