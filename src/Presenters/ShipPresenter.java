package Presenters;

import Views.EngineerView;
import Views.PilotView;
import com.sun.javafx.geom.Vec3d;
import com.sun.xml.internal.bind.v2.TODO;

import GameLogic.Ship;

import java.util.ArrayList;

/**
 * Created by james on 01/02/17.
 */
public class ShipPresenter implements Interfaces.ShipPresenter {

    private Ship mModel;
    private EngineerView mEngineerView;
    private PilotView mPilotView;

    public ShipPresenter() {

    }

    public void setModel(Ship model) {
        this.mModel = model;
    }

    @Override
    public int getHealth() {
        return mModel.getShipHealth();
    }

    @Override
    public int getShields() {
        return mModel.getShieldLevels();
    }

    @Override
    public int getEngineFuel() {
        return mModel.getFuelLevel();
    }

    @Override
    public int getAmmo(byte weaponIndex) {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 10;
    }

    @Override
    public ArrayList<Vec3d> getOtherShipPositions() {
        // TODO
        return null;
    }

    @Override
    public ArrayList<Vec3d> getAsteroidPositions() {
        // TODO
        return null;
    }

    @Override
    public void fireWeapon(byte weaponIndex) {
        // TODO
    }

    @Override
    public void updateFuel(int delta) {
        mModel.customChangeFuel(delta);
    }

    @Override
    public void updateShields(int delta) {
        mModel.customChangeShieldsLevel(delta);
    }
}
