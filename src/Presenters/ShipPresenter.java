package Presenters;

import Views.EngineerView;
import Views.PilotView;
import com.sun.javafx.geom.Vec3d;
import com.sun.xml.internal.bind.v2.TODO;

import GameLogic.ShipModel;

import java.util.ArrayList;

/**
 * Created by james on 01/02/17.
 */
public class ShipPresenter implements Interfaces.ShipPresenter {

    private ShipModel mModel;
    private EngineerView mEngineerView;
    private PilotView mPilotView;

    public ShipPresenter() {

    }

    public void setModel(ShipModel model) {
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
        return mModel.getAmmo(weaponIndex);
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
        mModel.customChnageFuel(delta);
    }

    @Override
    public void updateShields(int delta) {
        mModel.customChangeShieldsLevel(delta);
    }
}
