package Views;

import ClientNetworking.GameClient.GameClient;
import GameLogic.GameOptions;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import Physics.Body;

import javax.swing.*;
import java.awt.*;
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

    public AbstractPlayerView(String playerNickname, GameClient gameClient, JFrame parent) {
        super();

        this.playerNickname = playerNickname;
        this.gameClient = gameClient;
        this.parentFrame = parent;
        this.UILayeredPane = parent.getLayeredPane();
    }

    protected abstract void initialiseUI();

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

        return haveFoundMyShip;
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
}
