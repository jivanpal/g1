package GameLogic;

import Geometry.Vector;
import Physics.Body;
/**
 * Class which represents the ship object in the game
 * @author Ivan Panchev
 *
 */
public class Ship extends Body{
	private final int ENGINE_FORCE = 30; // newtons
	
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
	
// Engine-affecting, user-friendly movement methods
	
	final int PUSH_INTENSITY = 5;
	final double FRAMES_TO_WAIT = 2;
	
	public void pitchUp() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
	        _pitchUp();
	    }
	    sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _pitchDown();
        }
	    decreaseFuel();
	}
	
	public void pitchDown() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _pitchDown();
        }
	    sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _pitchUp();
        }
	    decreaseFuel();
	}
	
	public void rollLeft() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _rollLeft();
        }
	    sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _rollRight();
        }
        decreaseFuel();
	}
	
	public void rollRight() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _rollRight();
        }
        sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _rollLeft();
        }
        decreaseFuel();
	}
	
	public void thrustForward() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _thrustForward();
        }
	    sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _thrustReverse();
        }
        decreaseFuel();
	}
	
	public void thrustReverse() {
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _thrustReverse();
        }
	    sleep();
	    for (int i = 0; i < PUSH_INTENSITY; i++) {
            _thrustForward();
        }
        decreaseFuel();
	}
	
// Old movement methods
	
	private void _pitchUp() {
	    System.err.println("PitchUp!");
	    this.exertForce(Vector.K.scale(ENGINE_FORCE), Vector.J.scale(this.getRadius()));
	}
	
	private void _pitchDown() {
	    System.err.println("PitchDown!");
	    this.exertForce(Vector.K.negate().scale(ENGINE_FORCE), Vector.J.scale(this.getRadius()));
	}
	
	private void _rollLeft() {
	    System.err.println("RollLeft!");
	    this.exertForce(Vector.K.scale(ENGINE_FORCE/2), Vector.I.scale(this.getRadius()));
	    this.exertForce(Vector.K.negate().scale(ENGINE_FORCE/2), Vector.I.negate().scale(this.getRadius()));
	}
	
	private void _rollRight() {
	    System.err.println("RollRight!");
	    this.exertForce(Vector.K.scale(ENGINE_FORCE/2), Vector.I.negate().scale(this.getRadius()));
        this.exertForce(Vector.K.negate().scale(ENGINE_FORCE/2), Vector.I.scale(this.getRadius()));
	}
	private void _thrustForward() {
	    System.err.println("ThrustForward!");
	    this.exertForce(Vector.J.scale(ENGINE_FORCE), Vector.J.negate().scale(this.getRadius()));
	}
	
	private void _thrustReverse() {
	    System.err.println("ThrustReverse!");
	    this.exertForce(Vector.J.negate().scale(ENGINE_FORCE), Vector.J.scale(this.getRadius()));
	}
	
// Nice sleep method
	
	private void sleep() {
	    try {
	        Thread.sleep((long)(Global.REFRESH_PERIOD * FRAMES_TO_WAIT));
	    } catch (InterruptedException e) {
	        System.err.println("Couldn't sleep between exertion of forces when controlling ship\n"+e.getMessage());
	        e.printStackTrace();
	    }
	}
}
