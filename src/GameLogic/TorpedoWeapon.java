package GameLogic;

import Geometry.Rotation;
import Geometry.Vector;
import Physics.Entity;

/**
 * Class which represents the Torpedo weapon
 */
public class TorpedoWeapon extends Weapon {
	
	//default values for the specific weapon
	private static final int DEFAULT_AMMO = 0;
	private static final int DEFAULT_MAX_AMMO = 0;
	private static final int DEFAULT_AMMO_CHANGE = 0;
	private static final int DEFAULT_DAMAGE_TO_SHIP = 0;
	private static final int DEFAULT_DAMAGE_TO_SHIELD = 0;
	private static final int DEFAULT_COUNTDOWN = 0;
	private static final boolean DEFAULT_IS_FUNCTIONING = true;
	private static final double DEFAULT_TORPEDO_WEAPON_BULLET_MASS = 0; 
	
	public TorpedoWeapon(){
		ammo = new Resource(DEFAULT_AMMO, DEFAULT_MAX_AMMO, DEFAULT_AMMO_CHANGE);
		damageToShield = DEFAULT_DAMAGE_TO_SHIELD;
		damageToShip = DEFAULT_DAMAGE_TO_SHIP;
		countdown = DEFAULT_COUNTDOWN; 		//how many seconds the player should wait until being able to shoot again
		loadingType = Weapon.LOADING_TYPE_MANUAL;
		targetingType = Weapon.TARGETING_TYPE_AUTOMATIC;
		isFunctioning = DEFAULT_IS_FUNCTIONING;
	}

	public void fire(Vector position, Rotation orientation, Vector velocity, Vector angularVelocity) {
		Entity bullet = new Bullet(DEFAULT_TORPEDO_WEAPON_BULLET_MASS, position, orientation, velocity,
				angularVelocity, DEFAULT_DAMAGE_TO_SHIP, DEFAULT_DAMAGE_TO_SHIELD);
	}
}
