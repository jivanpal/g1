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

/**
 * Created by James on 16/03/17.
 * Abstract class that both PilotView and EngineerView will extend from
 */
public abstract class AbstractPlayerView extends JPanel implements Observer {
    protected String playerNickname;
    protected GameClient gameClient;

    protected JFrame parentFrame;
    protected JLayeredPane UILayeredPane;

    protected Screen screen;
    protected GameChat chatWindow;
    protected JLabel fullScreenLabel;

    public AbstractPlayerView(String playerNickname, GameClient gameClient, JFrame parent) {
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
                if (s.getEngineerName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        return null;
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
