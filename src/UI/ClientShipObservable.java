package UI;

import java.util.Observable;

/**
 * Created by james on 15/02/17.
 */
public class ClientShipObservable extends Observable {
    private int shipHealth = 0;
    private int shipShields = 0;
    private int shipFuel = 0;
    private int shipSpeed = 0;

    private int plasmaAmmo = 0;
    private int laserAmmo = 0;
    private int torpedoAmmo = 0;

    public ClientShipObservable() {
        super();
    }

    public int getShipHealth() {
        return shipHealth;
    }

    public void setShipHealth(int shipHealth) {
        this.shipHealth = shipHealth;
        hasChanged();
        notifyObservers();
    }

    public int getShipFuel() {
        return shipFuel;
    }

    public void setShipFuel(int shipFuel) {
        this.shipFuel = shipFuel;
        hasChanged();
        notifyObservers();
    }

    public int getShipShields() {
        return shipShields;
    }

    public void setShipShields(int shipShields) {
        this.shipShields = shipShields;
        hasChanged();
        notifyObservers();
    }

    public int getShipSpeed() {
        return shipSpeed;
    }

    public void setShipSpeed(int shipSpeed) {
        this.shipSpeed = shipSpeed;
        hasChanged();
        notifyObservers();
    }

    public int getTorpedoAmmo() {
        return torpedoAmmo;
    }

    public void setTorpedoAmmo(int torpedoAmmo) {
        this.torpedoAmmo = torpedoAmmo;
        hasChanged();
        notifyObservers();
    }

    public int getLaserAmmo() {
        return laserAmmo;
    }

    public void setLaserAmmo(int laserAmmo) {
        this.laserAmmo = laserAmmo;
        hasChanged();
        notifyObservers();
    }

    public int getPlasmaAmmo() {
        return plasmaAmmo;
    }

    public void setPlasmaAmmo(int plasmaAmmo) {
        this.plasmaAmmo = plasmaAmmo;
        hasChanged();
        notifyObservers();
    }
}
