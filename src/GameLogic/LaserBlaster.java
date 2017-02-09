package GameLogic;

import Geometry.Rotation;
import Geometry.Vector;
import Physics.Body;

/**
 * Class which represents the laser blaster
 */
public class LaserBlaster extends Weapon {
	
	//default values for the specific weapon
	private static final int DEFAULT_AMMO = 0;
	private static final int DEFAULT_MAX_AMMO = 0;
	private static final int DEFAULT_AMMO_CHANGE = 0;
	private static final int DEFAULT_DAMAGE_TO_SHIP = 0;
	private static final int DEFAULT_DAMAGE_TO_SHIELD = 0;
	private static final int DEFAULT_COUNTDOWN = 0;
	private static final boolean DEFAULT_IS_FUNCTIONING = true;
	private static final double DEFAULT_LASER_BLASTER_BULLET_MASS = 0;
	
	public LaserBlaster(){
		super(new Resource(DEFAULT_AMMO, DEFAULT_MAX_AMMO, DEFAULT_AMMO_CHANGE), DEFAULT_DAMAGE_TO_SHIELD, DEFAULT_DAMAGE_TO_SHIP,
				DEFAULT_COUNTDOWN, Weapon.LOADING_TYPE_MANUAL, Weapon.TARGETING_TYPE_AUTOMATIC, DEFAULT_IS_FUNCTIONING);
	}
	
	public void fire(Vector position, Rotation orientation, Vector velocity, Vector angularVelocity) {
		Body bullet = new Bullet(DEFAULT_LASER_BLASTER_BULLET_MASS, position, orientation, velocity,
				angularVelocity, DEFAULT_DAMAGE_TO_SHIP, DEFAULT_DAMAGE_TO_SHIELD);
	}
}
