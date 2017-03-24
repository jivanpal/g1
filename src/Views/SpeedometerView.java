package Views;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GameLogic.GameOptions;

/**
 * Created by James on 08/02/17.
 * View which shows the current speed of the ship.
 * @author James Brown
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
