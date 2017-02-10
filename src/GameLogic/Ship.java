package GameLogic;

import Physics.Body;

public class Ship extends Body{
	
	public static final byte LASER_BLASTER_INDEX = 0;
	public static final byte PLASMA_BLASTER_INDEX = 1;
	public static final byte TORPEDO_WEAPON_INDEX = 2;
	
	private static final int DEFAULT_HEALTH = 0;
	private static final int DEFAULT_MAX_HEALTH = 0;
	private static final int DEFAULT_HEALTH_CHANGE = 0;
	
	private Weapon torpedoWeapon;
	private Weapon laserBlaster;
	private Weapon plasmaBlaster;
	private Engines engines;
	private Shields shields;
	private Resource shipHealth;
	
	public Ship(){
		//Initialising weapons
		torpedoWeapon = new TorpedoWeapon(this);
		laserBlaster = new LaserBlaster(this);
		plasmaBlaster = new PlasmaBlaster(this);
		
		//Initialising other parts
		engines = new Engines();
		shields = new Shields();
		shipHealth = new Resource(DEFAULT_MAX_HEALTH, DEFAULT_HEALTH);
	
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
	
	public void customChnageFuel(int change){
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
}
