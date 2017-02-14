package Views;

import javax.swing.*;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by James on 08/02/17.
 */
public class WeaponView extends JPanel {
    private JLabel weaponNameLabel;
    private JProgressBar weaponAmmoLevel;

    private boolean showAmmoLevel = false;
    private boolean highlighted = false;

    public WeaponView(String weaponName) {
        weaponNameLabel = new JLabel(weaponName);

        this.add(weaponNameLabel);
    }

    public WeaponView(String weaponName, boolean showAmmoLevel) {
        this(weaponName);

        this.showAmmoLevel = showAmmoLevel;

        if(showAmmoLevel) {
            weaponAmmoLevel = new JProgressBar();
            this.add(weaponAmmoLevel);
        }
    }

    public void updateWeaponAmmoLevel(int ammoLevel) {
        if(showAmmoLevel) {
            weaponAmmoLevel.setValue(ammoLevel);
        }
    }

    public void toggleHighlightWeapon() {
        this.highlighted = !highlighted;

        if(highlighted) {
            weaponNameLabel.setForeground(Color.red);
        }
    }

}