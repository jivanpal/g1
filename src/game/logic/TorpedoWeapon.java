package game.logic;

import Temp.Resource;

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
	
	public TorpedoWeapon(){
		ammo = new Resource(DEFAULT_AMMO, DEFAULT_MAX_AMMO, DEFAULT_AMMO_CHANGE);
		damageToShield = DEFAULT_DAMAGE_TO_SHIELD;
		damageToShip = DEFAULT_DAMAGE_TO_SHIP;
		countdown = DEFAULT_COUNTDOWN; 		//how many seconds the player should wait until being able to shoot again
		loadingType = Weapon.LOADING_TYPE_MANUAL;
		targetingType = Weapon.TARGETING_TYPE_AUTOMATIC;
		isFunctioning = DEFAULT_IS_FUNCTIONING;
	}
}
