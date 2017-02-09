package GameLogic;

import Geometry.Rotation;
import Geometry.Vector;
import Physics.Body;

public class Bullet extends Body {
	
	private double damageToShip;
	private double damageToShield; 
	
	public Bullet (double mass, Vector position, Rotation orientation,
	        Vector velocity, Vector angularVelocity, int damageToShip, int damageToShield) {
			super(mass,position,orientation,velocity,angularVelocity);
			this.damageToShield = damageToShield;
			this.damageToShip = damageToShip;
	        
	    }
}
