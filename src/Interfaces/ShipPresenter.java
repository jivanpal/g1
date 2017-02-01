package Interfaces;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

/**
 * Created by james on 01/02/17.
 */
public interface ShipPresenter {
    int getHealth();
    int getShields();
    int getEngineFuel();
    int getAmmo(byte weaponIndex);

    ArrayList<Vec3d> getOtherShipPositions();   // temporary, may want to change the return type
    ArrayList<Vec3d> getAsteroidPositions();    // for these two

    void fireWeapon(byte weaponIndex);

    void updateFuel(int delta);                 // delta = +/- change from the current value
    void updateShields(int delta);
}
