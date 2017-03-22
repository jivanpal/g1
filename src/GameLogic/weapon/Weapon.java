package GameLogic.weapon;

import java.io.Serializable;

import GameLogic.Global;
import GameLogic.Resource;
import GameLogic.Ship;
import Geometry.*;
import Physics.*;

/**
 * A class of objects that describe weapons to be added to a ship.
 * @author Ivan Panchev
 * @author jivan
 */
public class Weapon implements Serializable {
/// CONSTANTS
    public static final double SPAWN_SEPARATION = 0.01; // meters
    
/// FIELDS
    private Ship        parent;
    private Bullet      bullet;
    private Resource    ammo;
    private double      cooldown;
    private double      remainingCooldown;
    
/// CONSTRUCTOR
    /**
     * Create a weapon with the given parameters.
     * @param parent the ship that the weapon is attached to.
     * @param bullet a reference body for this weapon's bullets.
     * @param maxAmmo the maximum number of bullets that the weapon can hold.
     * @param initAmmo the initial number of bullets the the weapon holds.
     *      If this value exceeds `maxAmmo` or is negative, then `initAmmo`
     *      will be set to `maxAmmo`.
     * @param cooldown the cooldown period for the weapon after each use, in seconds.
     */
    public Weapon(
        Ship    parent,
        Bullet  bullet,
        int     maxAmmo,
        int     initAmmo,
        double  cooldown
    ) {
        this.parent         = parent;
        this.bullet         = bullet;
        this.ammo           = new Resource(maxAmmo, initAmmo);
        this.cooldown       = cooldown;
    }
    
/// INSTANCE METHODS
    
// Getters
    
    public int getShipID() {
        return parent.getID();
    }
    
    public void increaseAmmo() {
    	ammo.increase();
    }
    
    public Resource getAmmoResource() {
        return ammo;
    }
    
    public int getAmmoMaximum() {
        return this.ammo.getMax();
    }
    
    public int getAmmoLevel() {
        return ammo.get();
    }
    
    public double getCooldownPeriod() {
        return cooldown;
    }
    
    public double getRemainingCooldown() {
        return remainingCooldown;
    }
    
    public boolean canFire () {
        return remainingCooldown == 0.0 && getAmmoLevel() > 0;
    }
    
// Evolution
    public void update() {
        remainingCooldown -= Global.REFRESH_PERIOD;
        if (remainingCooldown < 0) {
            remainingCooldown = 0;
        }
    }
    
// Actions
    
    /**
     * Fire this weapon, getting the Body instance that the firing of this
     * weapon at that instant would generate. 
     * @return an instance of this weapon's bullet type, positioned appropriately
     *      in relation to its parent Ship. If the weapon cannot be fired, `null`
     *      is returned instead.
     */
    public Bullet fire() {
    	if(this.canFire()) {
    		Bullet bullet = newPositionedBullet();
    	    ammo.decrease();
    		remainingCooldown = cooldown;
    		return bullet;
    	} else {
    	    return null;
    	}
    }
    
    /**
     * Clone the reference bullet and put it in position relative to the ship.
     */
    private Bullet newPositionedBullet() {
        Bullet instance = null;
        try {
            instance = (Bullet)bullet.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Couldn't clone reference bullet instance when firing weapon:");
            e.printStackTrace();
        }
        
        if (instance == null) {
            System.err.println("Cloning and casting of the reference bullet gave `null` for some reason.");
        } else {
            instance.setOriginBody(parent);
            instance.setPosition(instance.getPosition().plus(parent.getFrontVector().scale(parent.getRadius() + instance.getRadius() + SPAWN_SEPARATION)));
        }
        
        return instance;
    }
    
/// PREDEFINED WEAPON TYPES
    public enum Type {
        LASER, PLASMA, TORPEDO;
    }
}
