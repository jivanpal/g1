package GameLogic;

import Geometry.*;
import Physics.*;

public class Bullet extends Body implements Cloneable {
/// FIELDS
    private final Weapon.Type type;
    private int shipDamage;
    private int shieldDamage;
    private double timeToLive;
    
/// CONSTRUCTOR
    
    /**
     * Construct a bullet instance.
     * @param mass the mass of this instance.
     * @param radius the radius of this instance.
     * @param velocity the velocity of this instance.
     * @param angularVelocity the angular velocity of this instance.
     * @param type the type of weapon that this bullet originated from.
     * @param shipDamage the amount of damage this instance deals directly to ships. 
     * @param shieldDamage the amount of damage this instance deals to a ship's shields.
     * @param timeToLive the amount of time this instance should live for, in seconds.
     */
    public Bullet (
        double  mass,
        double  radius,
        Vector  velocity,
        Vector  angularVelocity,
        Weapon.Type type,
        int     shipDamage,
        int     shieldDamage,
        double  timeToLive
    ) {
        super(mass, radius, Vector.ZERO, Rotation.NONE, velocity, angularVelocity);
        
        this.type           = type;
        this.timeToLive     = timeToLive;
        this.shipDamage     = shipDamage;
        this.shieldDamage   = shieldDamage;
    }
    
/// INSTANCE METHODS
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
// Getters
    
    /**
     * Get the type of weapon that this instance originated from.
     */
    public Weapon.Type type() {
        return type;
    }
    
    /**
     * Get the amount of damage that this instance will deal
     * directly to a ship when that ship's shields are down.
     */
    public int getShipDamage() {
        return shipDamage;
    }
    
    /**
     * Get the amount of damage that this instance will
     * deal to a ship's shields when they are up.
     */
    public int getShieldDamage() {
        return shieldDamage;
    }
    
    /**
     * Get the time remaining before this instance should be
     * removed from the map.
     * @return the remaining amount of time that this instance
     *      should remain on the map, in seconds.
     */
    public double getTimeToLive() {
        return timeToLive;
    }
    
/// Refined UPDATE method
    
    public void update() {
        super.update();
        
        timeToLive -= Global.REFRESH_PERIOD;
        
        if (timeToLive < 0.0) {
            this.destroy();
        }
    }
}

