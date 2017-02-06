package GameLogic;

import java.util.ArrayList;

public class ShipModel {
	
	public static final byte LASER_BLASTER_INDEX = 0;
	public static final byte PLASMA_BLASTER_INDEX = 1;
	public static final byte TORPEDO_WEAPON_INDEX = 2;
	
	private static final int DEFAULT_SPEED = 0;
	private static final int DEFAULT_MAX_SPEED = 0;
	private static final int DEFAULT_SPEED_CHANGE = 0;
	
	private ArrayList<Weapon> weapons;
	private Engines engines;
	private Shields shields;
	private ShipHealth shipHealth;
	private Resource speed;
	
	public ShipModel(){
		//Initialising weapons
		TorpedoWeapon tp = new TorpedoWeapon();
		LaserBlaster lb = new LaserBlaster();
		PlasmaBlaster pb = new PlasmaBlaster();
		
		weapons = new ArrayList<>();
		weapons.add(lb);
		weapons.add(pb);
		weapons.add(tp);
		
		//Initialising other parts
		engines = new Engines();
		shields = new Shields();
		shipHealth = new ShipHealth();
		
		//initialising speed
		speed = new Resource(DEFAULT_SPEED, DEFAULT_MAX_SPEED, DEFAULT_SPEED_CHANGE);
		
		
	}
	
	public int getShipHealth(){
		return this.shipHealth.getHealth();
	}
	
	public int getShieldLevels(){
		return this.shields.getShieldsLevel();
	}
	
	public int getFuelLevel(){
		return this.engines.getFuel();
	}
	
	public int getAmmo(byte weaponIndex){
		return this.weapons.get(weaponIndex).getAmmo();
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
	
	public void increaseSpeed(){
		this.speed.increase();
	}
	
	public void decreaseSpeed(){
		this.speed.decrease();
	}
	
	public void customChangeSpeed(int change){
		this.speed.change(change);
	}

}
