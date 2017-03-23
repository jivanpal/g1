package Views;

import GameLogic.GameOptions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by James on 08/02/17.
 * View which shows the current speed of the ship.
 */
public class SpeedometerView extends JPanel {
    private JLabel speedometerLabel;
    private JLabel currentSpeedLabel;

    /**
     * Creates a new SpeedometerView. Speed defaults to 0.
     */
    public SpeedometerView() {
        super();

        this.speedometerLabel = new JLabel("Speed:");
        this.speedometerLabel.setFont(GameOptions.REGULAR_TEXT_FONT);
        this.currentSpeedLabel = new JLabel("0");
        this.currentSpeedLabel.setFont(GameOptions.REGULAR_TEXT_FONT);
        this.speedometerLabel.setForeground(Color.white);
        this.currentSpeedLabel.setForeground(Color.white);

        this.add(speedometerLabel);
        this.add(currentSpeedLabel);
    }

    /**
     * Update the speed label to a new value.
     * @param newLevel The new value for the speed label.
     */
    public void updateSpeedLevel(double newLevel) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        this.currentSpeedLabel.setText(String.valueOf(formatter.format(newLevel)));
    }
}
