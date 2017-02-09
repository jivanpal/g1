package GameLogic;

import Geometry.*;
import Physics.*;

public class Bullet extends Body {
	
	private double damageToShip;
	private double damageToShield; 
	
	public Bullet (
        int      damageToShip,
        int      damageToShield,
        double   mass,
        double   radius,
        Vector   position,
        Rotation orientation,
        Vector   velocity,
        Vector   angularVelocity
    ) {
        super(mass, radius, position, orientation, velocity, angularVelocity);
        
        this.damageToShield = damageToShield;
        this.damageToShip = damageToShip;
    }
	
	
}
