package GameLogic;

import java.io.Serializable;
import Geometry.*;
import Physics.*;

/**
 * A class of objects that describe weapons to be added to a ship.
 * @author Ivan Panchev
 * @author jivan
 */
public abstract class Weapon implements Serializable {
/// FIELDS
  
    private Ship        parent;
    private Bullet      bullet;
    private Resource    ammo;
    private int         shieldDamage;
    private int         shipDamage;
    private boolean     autoReload;
    private double      cooldown;
    private double      remainingCooldown = 0;
    
/// CONSTRUCTOR
    /**
     * Create a weapon with the given parameters.
     * @param parent the ship that the weapon is attached to.
     * @param bullet a reference body for this weapon's bullets.
     * @param maxAmmo the maximum number of bullets that the weapon can hold.
     * @param initAmmo the initial number of bullets the the weapon holds.
     *      If this value exceeds `maxAmmo` or is negative, then `initAmmo`
     *      will be set to `maxAmmo`.
     * @param shieldDamage the amount of damage that this weapon deals to a ship's shields.
     * @param shipDamage the amount of damage that this weapon deals to the ship
     *      directly when its shields are down.
     * @param autoReload `true` if this weapon should reload automatically.
     * @param cooldown the cooldown period for the weapon after each use, in milliseconds.
     */
    public Weapon(
        Ship    parent,
        Bullet  bullet,
        int     maxAmmo,
        int     initAmmo,
        int     shieldDamage,
        int     shipDamage,
        boolean autoReload,
        double  cooldown
    ) {
        this.parent         = parent;
        this.bullet         = bullet;
        this.ammo           = new Resource(maxAmmo, initAmmo);
        this.shieldDamage   = shieldDamage;
        this.shipDamage     = shipDamage;
        this.autoReload     = autoReload;
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
    
    public int getShieldDamage() {
        return shieldDamage;
    }
    
    public int getShipDamage() {
        return shipDamage;
    }
    
    public boolean autoReloads() {
        return autoReload;
    }
    
     public double getCooldownPeriod() {
        return cooldown;
    }
    
    public double getRemainingCooldown() {
        return remainingCooldown;
    }
    
    public boolean canFire () {
        return remainingCooldown == 0.0;
    }
    
// Evolution
    public void update() {
        remainingCooldown -= Global.REFRESH_PERIOD;
        if (remainingCooldown < 0) {
            remainingCooldown = 0;
        }
    }
    
// Actions
    public Bullet fire() throws Exception {
    	if(this.canFire()) {
    		Bullet bullet = newPositionedBullet();
    	    ammo.decrease();
    		remainingCooldown = cooldown;
    	}
    	return bullet;
    }
    
    private Bullet newPositionedBullet() {
        Bullet instance = null;
        try {
            instance = (Bullet)bullet.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Couldn't get new Bullet:");
            e.printStackTrace();
        }
        instance.setOriginBody(parent);
        return instance;
    }
    
    
}
