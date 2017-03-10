package GameLogic;

import Geometry.*;
import Physics.*;

/**
 * Class which represents the ship object in the game
 * @author Ivan Panchev
 *
 */
public class Ship extends Body{
	private static final double PITCH_SPEED  = 0.05;    // radians per second
    private static final double ROLL_SPEED   = 0.1;    // radians per second
    private static final double THRUST_SPEED = 5;    // meters  per second
	
	public static final byte LASER_BLASTER_INDEX = 0;
	public static final byte PLASMA_BLASTER_INDEX = 1;
	public static final byte TORPEDO_WEAPON_INDEX = 2;

	private String engineerName;
	private String pilotName;
	private Weapon torpedoWeapon;
	private Weapon laserBlaster;
	private Weapon plasmaBlaster;
	private Engines engines;
	private Shields shields;
	private ShipHealth shipHealth;
	
	/**
	 * Creates a new ship with a specific pilotName
	 * @param pilotName The pilot name of the specific ship
	 */
	public Ship(String pilotName, String engineerName){
		// Set mass and radius
		super(100, 2);
		
		//Initialising weapons
		torpedoWeapon = new TorpedoWeapon();
		laserBlaster = new LaserBlaster();
		plasmaBlaster = new PlasmaBlaster();
		
		//Initialising other parts
		engines = new Engines();
		shields = new Shields();
		shipHealth = new ShipHealth();
		
		//assigning the pilot name to the ship as a way of identifying the ship 
		this.pilotName = pilotName;
		this.engineerName = engineerName;
	}	
	
	public void updateWeaponsCooldown(){
		this.laserBlaster.update();
		this.torpedoWeapon.update();
		this.plasmaBlaster.update();
	}
	
	public String getPilotName(){
		return this.pilotName;
	}

	public String getEngineerName() {
		return engineerName;
	}
	
	//getters and setters
	public int getShipHealth(){
		return this.shipHealth.getHealth();
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
	
	public Bullet fire(int weaponIndex) throws Exception {
		switch(weaponIndex){
		case LASER_BLASTER_INDEX:
			return laserBlaster.fire(this);
		case PLASMA_BLASTER_INDEX:
			return plasmaBlaster.fire(this);
		case TORPEDO_WEAPON_INDEX:
			return torpedoWeapon.fire(this);
		default:
		    throw new IllegalArgumentException("You didn't specify a weapon index!");
		}
	}
	
	public void increaseWeaponAmmoByIndex(int index){
		if(index == LASER_BLASTER_INDEX){
			laserBlaster.increaseAmmo();
		} else if(index == TORPEDO_WEAPON_INDEX){
			torpedoWeapon.increaseAmmo();
		} else if(index == PLASMA_BLASTER_INDEX){
			plasmaBlaster.increaseAmmo();
		}
	}
	
	public int getWeaponMaxAmmoByIndex(int index){
		if(index == LASER_BLASTER_INDEX){
			return laserBlaster.getMaxAmmo();
		} else if(index == TORPEDO_WEAPON_INDEX){
			return torpedoWeapon.getMaxAmmo();
		} else if(index == PLASMA_BLASTER_INDEX){
			return plasmaBlaster.getMaxAmmo();
		}

        // Should never reach this
		return -1;
	}
	
// Movement methods
	
	private static final Rotation  PITCH_UP    = new Rotation(Vector.I.scale(PITCH_SPEED * Global.REFRESH_PERIOD));
	private static final Rotation  PITCH_DOWN  = new Rotation(Vector.I.negate().scale(PITCH_SPEED * Global.REFRESH_PERIOD));
	private static final Rotation  ROLL_RIGHT  = new Rotation(Vector.J.scale(ROLL_SPEED * Global.REFRESH_PERIOD));
	private static final Rotation  ROLL_LEFT   = new Rotation(Vector.J.negate().scale(ROLL_SPEED * Global.REFRESH_PERIOD));
	private static final Vector    THRUST_FWD  = Vector.J.scale(THRUST_SPEED);
	private static final Vector    THRUST_REV  = Vector.J.negate().scale(THRUST_SPEED);
	
	public void pitchUp() {
	    rotate(PITCH_UP);
	}
	
	public void pitchDown() {
	    rotate(PITCH_DOWN);
	}
	
	public void rollRight() {
        rotate(ROLL_RIGHT);
    }
	
	public void rollLeft() {
	    rotate(ROLL_LEFT);
	}
	
	public void thrustForward() {
	    alterVelocityLocally(THRUST_FWD);
	}
	
	public void thrustReverse() {
	    alterVelocityLocally(THRUST_REV);
	}
}
