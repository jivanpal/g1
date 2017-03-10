package GameLogic;

import Geometry.*;
import Physics.*;

/**
 * Class which represents the ship object in the game
 * @author Ivan Panchev
 * @author jivan
 */
public class Ship extends Body{
    // Constants to denote weapon types
    public static final byte LASER_BLASTER_INDEX    = 0;
    public static final byte PLASMA_BLASTER_INDEX = 1;
    public static final byte TORPEDO_WEAPON_INDEX = 2;
    
    // Maximum values for pitch velocity, roll velocity, and forward velocity.
    // The ship is designed under the assumption that it cannot reverse,
    //      therefore the reverse-velocity maximum is assumed to be zero.
    private static final double PITCH_VEL_MAX = 0.1;  // radians per second
    private static final double ROLL_VEL_MAX  = 0.3;  // radians per second
    private static final double FWD_VEL_MAX   = 20;   // meters per second
    
    // Smoothness of transition from rest to maximum for each axis
    // Higher values result in smoother transitions
    private static final int    PITCH_SMOOTHNESS    = 10;
    private static final int    ROLL_SMOOTHNESS     = 10;
    private static final int    THRUST_SMOOTHNESS   = 4;
    
    private String engineerName;
    private String pilotName;
    private Weapon torpedo;
    private Weapon laser;
    private Weapon plasma;
    private Engines engines;
    private Shields shields;
    private Health health;
    
    /**
     * Create a ship belonging to a specified pilot and engineer. 
     * @param pilotName the nickname of this instance's pilot.
     * @param engineerName the nickname of this instance's engineer.
     */
    public Ship(String pilotName, String engineerName){
        // Set mass and radius
        super(100, 2);
        
        // Initialising ship weapons
        torpedo = new TorpedoWeapon();
        laser   = new LaserBlaster();
        plasma  = new PlasmaBlaster();
        
        // Initialise ship resources
        engines = new Engines();
        shields = new Shields();
        health  = new Health();
        
        // Record the names of the pilot and engineer 
        this.pilotName = pilotName;
        this.engineerName = engineerName;
    }	
    
    public String getPilotName(){
        return pilotName;
    }
    
    public String getEngineerName() {
        return engineerName;
    }
    
    // Getters
    
    public int getHealth(){
        return health.getHealth();
    }
    
    public int getShieldLevels(){
        return shields.getShieldsLevel();
    }
    
    public int getFuelLevel(){
        return engines.getFuel();
    }
    
    public int getPlasmaBlasterAmmo(){
        return plasma.getAmmoLevel();
    }
    
    public int getTorpedoWeaponAmmo(){
        return torpedo.getAmmoLevel();
    }
    
    public int getLaserBlasterAmmo(){
        return laser.getAmmoLevel();
    }
    
    // Setters
    
    public void increaseFuel(){
        engines.increaseFuel();
    }
    
    public void decreaseFuel(){
        engines.decreaseFuel();
    }
    
    public void customChangeFuel(int change){
        engines.customChangeFuel(change);
    }
    
    public void increseShieldsLevel(){
        shields.increaseShieldsLevel();
    }
    
    public void decreaseShieldsLevel(){
        shields.decreaseShieldsLevel();
    }
    
    public void customChangeShieldsLevel(int change){
        shields.cusomChangeShieldsLevel(change);
    }
    
    // Firing weapons
    
    public Bullet fire(int weaponIndex) throws Exception {
        switch(weaponIndex) {
        case LASER_BLASTER_INDEX:
            return laser.fire(this);
        case PLASMA_BLASTER_INDEX:
            return plasma.fire(this);
        case TORPEDO_WEAPON_INDEX:
            return torpedo.fire(this);
        default:
            throw new IllegalArgumentException("You didn't specify a weapon index!");
        }
    }
    
    public void increaseWeaponAmmoByIndex(int weaponIndex) {
        switch(weaponIndex) {
        case LASER_BLASTER_INDEX:
            laser.increaseAmmo();
        case PLASMA_BLASTER_INDEX:
            torpedo.increaseAmmo();
        case TORPEDO_WEAPON_INDEX:
            plasma.increaseAmmo();
        default:
            throw new IllegalArgumentException("You didn't specify a weapon index!");
        }
    }
    
    public int getWeaponMaxAmmoByIndex(int weaponIndex){
        switch(weaponIndex) {
        case LASER_BLASTER_INDEX:
            return laser.getMaxAmmo();
        case PLASMA_BLASTER_INDEX:
            return plasma.getMaxAmmo();
        case TORPEDO_WEAPON_INDEX:
            return torpedo.getMaxAmmo();
        default:
            throw new IllegalArgumentException("You didn't specify a weapon index!");
        }
    }
    
// Movement methods
    
    private static final double PITCH_ACC   = PITCH_VEL_MAX / PITCH_SMOOTHNESS;
    private static final double ROLL_ACC    = ROLL_VEL_MAX  / ROLL_SMOOTHNESS;
    private static final double THRUST_ACC  = FWD_VEL_MAX   / THRUST_SMOOTHNESS;
    
    private static final Rotation   PITCH_UP    = new Rotation(Vector.I.scale(PITCH_ACC * Global.REFRESH_PERIOD));
    private static final Rotation   PITCH_DOWN  = new Rotation(Vector.I.negate().scale(PITCH_ACC * Global.REFRESH_PERIOD));
    private static final Rotation   ROLL_RIGHT  = new Rotation(Vector.J.scale(ROLL_ACC * Global.REFRESH_PERIOD));
    private static final Rotation   ROLL_LEFT   = new Rotation(Vector.J.negate().scale(ROLL_ACC * Global.REFRESH_PERIOD));
    private static final Vector     THRUST_FWD  = Vector.J.scale(THRUST_ACC);
    private static final Vector     THRUST_REV  = Vector.J.negate().scale(THRUST_ACC);
    
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
