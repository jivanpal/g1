package GameLogic;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * Abstract class which lays out the skeleton of a weapon
 */
public abstract class Weapon {
	
	public static final byte LOADING_TYPE_MANUAL = 1;
	public static final byte LOADING_TYPE_AUTOMATIC = 0;
	
	public static final byte TARGETING_TYPE_MANUAL = 1;
	public static final byte TARGETING_TYPE_AUTOMATIC = 0;
	
	private Resource ammo;
	private int damageToShield;
	private int damageToShip;
	private int countdown; 		//how many seconds the player should wait until being able to shoot again
	private byte loadingType;
	private byte targetingType; //whether the user has to manually target the weapon at the opponent or not 
	private boolean isFunctioning;
	
	public Weapon(Resource ammo, int damageToShield, int damageToShip, int countdown, byte loadingType,
			byte targetingType, boolean isFuntioning){
		this.ammo = ammo;
		this.damageToShield = damageToShield;
		this.damageToShip = damageToShip;
		this.countdown = countdown;
		this.loadingType = loadingType;
		this.targetingType = targetingType;
		this.isFunctioning = isFuntioning;
	}
	
	public int getAmmo(){
		return this.ammo.get();
	}
	
	public boolean getIsFunctioning(){
		return this.isFunctioning;
	}
	
	public int getDamageToShip(){
		return this.damageToShip;
	}
	
	public int getDamageToShield(){
		return this.damageToShield;
	}
	
	public byte getLoadingType(){
		return this.loadingType;
	}
	
	public byte getTargetingType(){
		return this.targetingType;
	}
	
	public abstract void fire(Vector position, Rotation orientation, Vector velocity, Vector angularVelocity);

}
