package Views;

import Graphics.Screen;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel implements KeyListener{

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

        WeaponView plasmaBlasterView = new WeaponView("Plasma Blaster", false);

        // default plasma blaster to be highlighted, remove at a later date!
        plasmaBlasterView.toggleHighlightWeapon();

        WeaponView laserBlasterView = new WeaponView("Laser Blaster", false);
        WeaponView torpedosView = new WeaponView("Torpedos", false);

        Container weaponPanel = new Container();
        weaponPanel.add(plasmaBlasterView);
        weaponPanel.add(laserBlasterView);
        weaponPanel.add(torpedosView);
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));

        Container UIpanel = new Container();
        UIpanel.add(weaponPanel);
        UIpanel.add(speedometerView);
        UIpanel.setLayout(new BoxLayout(UIpanel, BoxLayout.X_AXIS));

        this.add(UIpanel, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);
    }

    public void makeUI() {}

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        screen.keyTyped(keyEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        screen.keyPressed(keyEvent);

        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Weapon Fired");
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        screen.keyReleased(keyEvent);
    }
}
