package GameLogic;

import Physics.Body;
/**
 * Class which represents the ship object in the game
 * @author Ivan Panchev
 *
 */
public class Ship extends Body{
	
	public static final byte LASER_BLASTER_INDEX = 0;
	public static final byte PLASMA_BLASTER_INDEX = 1;
	public static final byte TORPEDO_WEAPON_INDEX = 2;
	
	private static final int DEFAULT_HEALTH = 0;
	private static final int DEFAULT_MAX_HEALTH = 0;
	
	private String pilotName;
	
	private Weapon torpedoWeapon;
	private Weapon laserBlaster;
	private Weapon plasmaBlaster;
	private Engines engines;
	private Shields shields;
	private Resource shipHealth;
	
	public Ship(String pilotName){
		//Initialising weapons
		torpedoWeapon = new TorpedoWeapon();
		laserBlaster = new LaserBlaster();
		plasmaBlaster = new PlasmaBlaster();
		
		//Initialising other parts
		engines = new Engines();
		shields = new Shields();
		shipHealth = new Resource(DEFAULT_MAX_HEALTH, DEFAULT_HEALTH);
		
		//assigning the pilot name to the ship as a way of identifying the ship 
		this.pilotName = pilotName;
	
	}
	
	public String getPilotName(){
		return this.pilotName;
	}
	
	//getters and setters
	public int getShipHealth(){
		return this.shipHealth.get();
	}
	
	public int getShieldLevels(){
		return this.shields.getShieldsLevel();
	}
	
	public int getFuelLevel(){
		return this.engines.getFuel();
	}
	
	public int getPlasmaBlasterAmmo(){
		return this.plasmaBlaster.getAmmoLevel();
	}
	
	public int getTorpedoWeaponAmmo(){
		return this.torpedoWeapon.getAmmoLevel();
	}
	
	public int getLaserBlasterAmmo(){
		return this.laserBlaster.getAmmoLevel();
	}
	
	public void increaseFuel(){
		this.engines.increaseFuel();
	}
	
	public void decreaseFuel(){
		this.engines.decreaseFuel();
	}
	
	public void customChangeFuel(int change){
		this.engines.customChangeFuel(change);
	}
	
	public void increseShieldsLevel(){
		this.shields.increaseShieldsLevel();
	}
	
	public void decreaseShieldsLevel(){
		this.shields.decreaseShieldsLevel();
	}
	
	public void customChangeShieldsLevel(int change){
		this.shields.cusomChangeShieldsLevel(change);
	}
	
	public void fire(int weaponIndex) {
	    // TODO Implement this method
	}
}
