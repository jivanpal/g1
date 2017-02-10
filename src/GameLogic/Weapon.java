package GameLogic;

import Geometry.*;
import Physics.*;

/**
 * A class of objects that describe weapons to be added to a ship.
 */
public abstract class Weapon {
/// FIELDS
    private Ship        parent;
    private Bullet      bullet;
    private Resource    ammo;
    private int         shieldDamage;
    private int         shipDamage;
    private boolean     autoReload;
    private boolean     autoTarget;
    private double      cooldown;
    private double      remainingCooldown = 0;
    
/// CONSTRUCTOR
    /**
     * Create a weapon with the given parameters.
     * @param   parent          The ship that the weapon is attached to.
     * @param   referenceBullet A reference body for this weapon's bullets.
     * @param   maxAmmo         The maximum number of bullets that the weapon can hold.
     * @param   initAmmo        The initial number of bullets the the weapon holds. If this
     *              number exceeds `maxAmmo`, then `initAmmo` will be set to `maxAmmo`.
     * @param   shieldDamage    The amount of damage that this weapon deals to a ship's shields.
     * @param   shipDamage      The amount of damage that this weapon deals to the ship directly when its shields are down.
     * @param   autoReload      Whether this weapon reloads automatically.
     * @param   autoTarget      Whether this weapon automatically locks onto targets.
     * @param   cooldown        The cooldown period for the weapon after each use, in seconds.
     */
    public Weapon(
        Ship        parent,
        Bullet      bullet,
        int         maxAmmo,
        int         initAmmo,
        int         shieldDamage,
        int         shipDamage,
        boolean     autoReload,
        boolean     autoTarget,
        double      cooldown
    ) {
        this.parent         = parent;
        this.ammo           = new Resource(maxAmmo, initAmmo);
        this.shieldDamage   = shieldDamage;
        this.shipDamage     = shipDamage;
        this.autoReload     = autoReload;
        this.autoTarget     = autoTarget;
        this.cooldown       = cooldown;
    }
    
/// INSTANCE METHODS
    
// Getters
    
    public Ship getParent() {
        return parent;
    }
    
    public Bullet getReferenceBullet() {
        return bullet;
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
    
    public boolean autoTargets() {
        return autoTarget;
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
    
    private Bullet getBulletInstance() {
        Bullet instance = (Bullet)bullet.clone();
        instance.setReferenceBody(parent);
        return instance;
    }
    
// Evolution
    public void update() {
        remainingCooldown -= Global.REFRESH_PERIOD;
        if (remainingCooldown < 0) {
            remainingCooldown = 0;
        }
    }
    
// Actions
    public void fire() {
        parent.getMap().add(getBulletInstance());
        ammo.down();
    }
}
