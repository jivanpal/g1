package game.logic;

import Temp.Resource;

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
	
	public int getAmmo(){
		return this.ammo.get();
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
	
	public void setSupply(Resource supply){
		this.ammo = supply;
	}
	
	public void setDamageToShip(int damage){
		this.damageToShip = damage;
	}
	
	public void setDamageToShield(int damage){
		this.damageToShield = damage;
	}

}
