package Views;

import Interfaces.ShipPresenter;

import javax.swing.*;

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
        this.currentSpeedLabel = new JLabel("0");

        this.add(speedometerLabel);
        this.add(currentSpeedLabel);
    }

    /**
     * Update the speed label to a new value.
     * @param newLevel The new value for the speed label.
     */
    public void updateSpeedLevel(float newLevel) {
        this.currentSpeedLabel.setText(String.valueOf(newLevel));
    }
}
