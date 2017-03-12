package Presenters;

import Views.EngineerView;
import Views.PilotView;
import com.sun.javafx.geom.Vec3d;
import com.sun.xml.internal.bind.v2.TODO;

import GameLogic.Resource;
import GameLogic.Ship;
import GameLogic.Weapon;

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
        return mModel.getResource(Resource.Type.HEALTH).get();
    }

    @Override
    public int getShields() {
        return mModel.getResource(Resource.Type.SHIELDS).get();
    }

    @Override
    public int getEngineFuel() {
        return mModel.getResource(Resource.Type.ENGINES).get();
    }

    @Override
    public int getAmmo(Weapon.Type type) {
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

    public void fire(Weapon.Type type) {
        // TODO
    }

    @Override
    public void updateFuel(int delta) {
        mModel.getResource(Resource.Type.ENGINES).alter(delta);
    }

    @Override
    public void updateShields(int delta) {
        mModel.getResource(Resource.Type.SHIELDS).alter(delta);
    }
}
