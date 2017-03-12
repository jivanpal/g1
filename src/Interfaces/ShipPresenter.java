package Interfaces;

import com.sun.javafx.geom.Vec3d;
import java.util.ArrayList;
import GameLogic.Weapon;

/**
 * Created by james on 01/02/17.
 */
public interface ShipPresenter {
    int getHealth();
    int getShields();
    int getEngineFuel();
    int getAmmo(Weapon.Type type);
    int getSpeed();

    ArrayList<Vec3d> getOtherShipPositions();   // temporary, may want to change the return type
    ArrayList<Vec3d> getAsteroidPositions();    // for these two

    void fire(Weapon.Type type);

    void updateFuel(int delta);                 // delta = +/- change from the current value
    void updateShields(int delta);
}
