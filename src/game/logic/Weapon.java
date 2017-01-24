package game.logic;
/**
 * Abstract class which lays out the skeleton of a weapon
 */
public abstract class Weapon {
	
	public static final byte LOADING_TYPE_MANUAL = 1;
	public static final byte LOADING_TYPE_AUTOMATIC = 0;
	
	public static final byte TARGETING_TYPE_MANUAL = 1;
	public static final byte TARGETING_TYPE_AUTOMATIC = 0;
	
	int supply;
	int damageToShield;
	int damageToShip;
	int countdown; 		//how many seconds the player should wait until being able to shoot again
	byte loadingType;
	byte targetingType; //whether the user has to manually target the weapon at the opponent or not 
	
	public int getSupply(){
		return this.supply;
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
	
	public void setSupply(int supply){
		this.supply = supply;
	}
	
	public void setDamageToShip(int damage){
		this.damageToShip = damage;
	}
	
	public void setDamageToShield(int damage){
		this.damageToShield = damage;
	}

}
