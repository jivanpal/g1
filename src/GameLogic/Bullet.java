package GameLogic;

import Geometry.*;
import Physics.*;

public class Bullet extends Body implements Cloneable {
/// DEFAULTS
    public static double DEFAULT_TIME_TO_LIVE = 3.0;
    
/// FIELDS
    private int shipDamage;
    private int shieldDamage;
    private double timeToLive;
    
/// CONSTRUCTORS
    
    /**
     * Construct a bullet instance.
     * @param shipDamage the amount of damage this instance deals directly to ships. 
     * @param shieldDamage the amount of damage this instance deals to a ship's shields.
     * @param timeToLive the amount of time this instance should live for, in seconds.
     * @param mass the mass of this instance.
     * @param radius the radius of this instance.
     * @param velocity the velocity of this instance.
     * @param angularVelocity the angular velocity of this instance.
     */
    public Bullet (
        int     shipDamage,
        int     shieldDamage,
        double  timeToLive,
        double  mass,
        double  radius,
        Vector  velocity,
        Vector  angularVelocity
    ) {
        super(mass, radius, Vector.ZERO, Rotation.NONE, velocity, angularVelocity);
        
        this.shipDamage = shipDamage;
        this.shieldDamage = shieldDamage;
        this.timeToLive = timeToLive;
    }
    
    public Bullet (int shipDamage, int shieldDamage, double timeToLive, Body body) {
        this(
            shipDamage,
            shieldDamage,
            timeToLive,
            body.getMass(),
            body.getRadius(),
            body.getVelocity(),
            body.getAngularVelocity()
        );
    }
    
    public Bullet (
        int     shipDamage,
        int     shieldDamage,
        double  mass,
        double  radius,
        Vector  velocity,
        Vector  angularVelocity
    ) {
        this(shipDamage, shieldDamage, DEFAULT_TIME_TO_LIVE, mass, radius, velocity, angularVelocity);
    }
    
    public Bullet (int shipDamage, int shieldDamage, Body body) {
        this(
            shipDamage,
            shieldDamage,
            DEFAULT_TIME_TO_LIVE,
            body.getMass(),
            body.getRadius(),
            body.getVelocity(),
            body.getAngularVelocity()
        );
    }
    
/// INSTANCE METHODS
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
// Getters
    
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
     * @return the time to live, in seconds.
     */
    public double timeToLive() {
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

