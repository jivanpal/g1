package Views;

import Interfaces.ShipPresenter;

import javax.swing.*;

/**
 * Created by james on 08/02/17.
 */
public class SpeedometerView extends JPanel {
    private ShipPresenter presenter;

    private JLabel speedometerLabel;
    private JLabel currentSpeedLabel;

    public SpeedometerView() {
        super();

        this.speedometerLabel = new JLabel("Speed:");
        this.currentSpeedLabel = new JLabel("0");

        this.add(speedometerLabel);
        this.add(currentSpeedLabel);
    }

    public void makeUI() {

    }

    public void updateSpeedLevel(float newLevel) {
        this.currentSpeedLabel.setText(String.valueOf(newLevel));
    }

    public void setPresenter(ShipPresenter presenter) {
        this.presenter = presenter;
    }
}
