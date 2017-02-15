package Views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by James on 08/02/17.
 * View to show information about one single weapon (name, ammo bar, highlighted).
 * By default the Weapon is unhighlighted and shows no ammo bar.
 */
public class WeaponView extends JPanel {
    private Color HIGHLIGHT_COLOUR = Color.red;
    private Color DEFAULT_COLOUR = Color.black;

    private JLabel weaponNameLabel;
    private JProgressBar weaponAmmoLevel;

    private boolean showAmmoLevel = false;
    private boolean highlighted = false;

    /**
     * Creates a WeaponView with the given label name. Ammo bar does not exist.
     * @param weaponName The name of the weapon
     */
    public WeaponView(String weaponName) {
        weaponNameLabel = new JLabel(weaponName);

        this.add(weaponNameLabel);
    }

    /**
     * Creates a WeaponView with the given label name and shows the ammo bar if appropriate.
     * @param weaponName The name of the weapon
     * @param showAmmoLevel Whether to create and show the ammo bar
     */
    public WeaponView(String weaponName, boolean showAmmoLevel) {
        this(weaponName);

        this.showAmmoLevel = showAmmoLevel;

        if(showAmmoLevel) {
            weaponAmmoLevel = new JProgressBar();
            this.add(weaponAmmoLevel);
        }
    }

    /**
     * Updates the value of the ammo bar to the given value
     * @param ammoLevel The new ammo level
     */
    public void updateWeaponAmmoLevel(int ammoLevel) {
        if(showAmmoLevel) {
            weaponAmmoLevel.setValue(ammoLevel);
        }
    }

    /**
     * Set whether the Weapon is highlighted or not
     * @param newHighlight New value for whether the weapon is highlighted or not
     */
    public void setHighlightWeapon(boolean newHighlight) {
        this.highlighted = newHighlight;

        if(highlighted) {
            weaponNameLabel.setForeground(HIGHLIGHT_COLOUR);
        } else {
            weaponNameLabel.setForeground(DEFAULT_COLOUR);
        }
    }
}