package Views;

import Graphics.Screen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel {

    Screen screen;

    public PilotView() {
        this.setLayout(new BorderLayout());

        screen = new Screen();
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));
        this.add(screen, BorderLayout.CENTER);

        SpeedometerView speedometerView = new SpeedometerView();
        this.add(speedometerView, BorderLayout.SOUTH);
    }

    public void makeUI() {}
}
