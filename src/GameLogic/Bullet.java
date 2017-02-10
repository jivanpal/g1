package GameLogic;

import Geometry.*;
import Physics.*;

public class Bullet extends Body implements Cloneable {
/// FIELDS
    private int shipDamage;
    private int shieldDamage; 	
    
/// CONSTRUCTORS
    
    public Bullet (
        int     shipDamage,
        int     shieldDamage,
        double  mass,
        double  radius,
        Vector  velocity,
        Vector  angularVelocity
    ) {
        super(mass, radius, Vector.ZERO, Rotation.NONE, velocity, angularVelocity);
        
        this.shipDamage = shipDamage;
        this.shieldDamage = shieldDamage;
    }
    
    public Bullet (int shipDamage, int shieldDamage, Body body) {
        this(
            shipDamage,
            shieldDamage,
            body.getMass(),
            body.getRadius(),
            body.getVelocity(),
            body.getAngularVelocity()
        );
    }
    
/// INSTANCE METHODS
    
    public Object clone() {
        return super.clone();
    }
    
// Getters
    
    public int getShipDamage() {
        return shipDamage;
    }
    
    public int getShieldDamage() {
        return shieldDamage;
    }
}

