package Views;

import Graphics.Screen;
import UI.ClientShipObservable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel implements KeyListener, Observer{

    private final Screen screen;
    private final SpeedometerView speedometerView;
    private final WeaponView plasmaBlasterView;
    private final WeaponView laserBlasterView;
    private final WeaponView torpedosView;

    private final InstructionsView instructionsView;

    public PilotView(String playerNickname) {
        this.setLayout(new BorderLayout());

        screen = new Screen(playerNickname, true);
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                final int x = mouseEvent.getX();
                final int y = mouseEvent.getY();

                final Rectangle screenBounds = screen.getBounds();
                if(screenBounds != null && screenBounds.contains(x, y)) {
                    getParent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        this.add(screen, BorderLayout.CENTER);

        speedometerView = new SpeedometerView();

        plasmaBlasterView = new WeaponView("Plasma Blaster", false);

        // default plasma blaster to be highlighted, remove at a later date!
        plasmaBlasterView.setHighlightWeapon(true);

        laserBlasterView = new WeaponView("Laser Blaster", false);
        torpedosView = new WeaponView("Torpedos", false);

        instructionsView = new InstructionsView();
        instructionsView.addInstruction("test instruction 1");
        instructionsView.addInstruction("test instruction 2");
        instructionsView.addInstruction("test instruction 3");

        Container weaponPanel = new Container();
        weaponPanel.add(plasmaBlasterView);
        weaponPanel.add(laserBlasterView);
        weaponPanel.add(torpedosView);
        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));

        Container UIpanel = new Container();
        UIpanel.add(weaponPanel);
        UIpanel.add(speedometerView);
        UIpanel.add(instructionsView);
        UIpanel.setLayout(new BoxLayout(UIpanel, BoxLayout.X_AXIS));

        this.add(UIpanel, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Weapon Fired. Tell the server.");
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void update(Observable observable, Object o) {
        ClientShipObservable shipObservable = (ClientShipObservable) observable;

        speedometerView.updateSpeedLevel(shipObservable.getShipSpeed());
        laserBlasterView.updateWeaponAmmoLevel(shipObservable.getLaserAmmo());
        plasmaBlasterView.updateWeaponAmmoLevel(shipObservable.getPlasmaAmmo());
        torpedosView.updateWeaponAmmoLevel(shipObservable.getTorpedoAmmo());
    }
}
