package GameLogic;

import Geometry.*;
import Physics.*;
import GameLogic.weapon.*;
import GameLogic.resource.*;

/**
 * Class which represents the ship object in the game
 * @author Ivan Panchev
 * @author jivan
 */
public class Ship extends Body{


/// CONSTANTS
	
	private static final double MASS 	= 100;
    private static final double RADIUS 	= 8;
    
    public static final int DAMAGE_TO_OTHER_SHIPS = 40;
	
	// Decay rates and velocity bounds
    private static final double NONPLANAR_SPIN_DECAY        = 0.95; // radians per radian
    private static final double NONPLANAR_SPIN_MIN          = 0.02; // radians per sec
    private static final double ORTHOGONAL_VELOCITY_DECAY   = 0.95; // ratio
    private static final double ORTHOGONAL_VELOCITY_MIN     = 0.05; // meters per meter 
    
    //   Maximum values for pitch velocity, roll velocity, and forward velocity.
    //   The ship is designed under the assumption that it cannot reverse,
    // therefore the reverse-velocity maximum is assumed to be zero.
    private static final double PITCH_VEL_MAX   = 1.0;  // radians per second
    private static final double YAW_VEL_MAX	    = 1.0;  // radians per second
    private static final double ROLL_VEL_MAX    = 4.0;	// radians per second 
    private static final double FWD_VEL_MAX     = 200.0;// meters per second
    
    // Smoothness of transition from rest to maximum for each axis.
    // Higher values result in smoother transitions.
    private static final int    PITCH_SMOOTHNESS    = 10;
    private static final int    YAW_SMOOTHNESS     	= 10;
    private static final int	ROLL_SMOOTHNESS		= 10;
    private static final int    THRUST_SMOOTHNESS   = 100;
    
    // Roll / yaw flag
    private static final boolean ROLLING = false;
    
/// FIELDS
    private String engineerName, pilotName;
    private Weapon laser, plasma, torpedo;
    private Resource engines, shields, health;
    private int collisionAllowedTick = 0;
    
/// CONSTRUCTOR
    
    /**
     * Create a ship belonging to a specified pilot and engineer. 
     * @param pilotName the nickname of this instance's pilot.
     * @param engineerName the nickname of this instance's engineer.
     */
    public Ship(String pilotName, String engineerName){
        // Set mass and radius
        super(MASS, RADIUS);
        
        // Initialising ship weapons
        laser   = new Laser(this);
        plasma  = new Plasma(this);
        torpedo = new Torpedo(this);
        
        // Initialise ship resources
        engines = new GameLogic.resource.Engines();
        shields = new GameLogic.resource.Shields();
        health  = new Health();
        
        // Record the names of the pilot and engineer 
        this.pilotName = pilotName;
        this.engineerName = engineerName;
    }	
    
/// GETTERS
    
    public String getPilotName() {
        return pilotName;
    }
    
    public String getEngineerName() {
        return engineerName;
    }
    
    public boolean isAllowedToTakeDamageOnCollision() {
    	return collisionAllowedTick == 0;
    }
    
    public void collided() {
    	collisionAllowedTick -= GameLogic.Global.REFRESH_RATE;
    	if (collisionAllowedTick < 0) {
    	    collisionAllowedTick = 0;
    	}
    }
    
    public Weapon getWeapon(Weapon.Type type) {
        switch (type) {
        case LASER:
            return laser;
        case PLASMA:
            return plasma;
        case TORPEDO:
            return torpedo;
        default:
            throw new RuntimeException("Apparently, you asked for a weapon that doesn't exist, which is impossible. WOOPS!");
        }
    }
    
    public Resource getResource(Resource.Type type) {
        switch (type) {
        case ENGINES:
            return engines;
        case SHIELDS:
            return shields;
        case HEALTH:
            return health;
        default:
            throw new RuntimeException("Apparently, you asked for a resource that doesn't exist, which is impossible. WOOPS!");
        }
    }
    
    /**
     * Makes the ship take appropriate damage to either its shields of health
     * @param shipDamage The damage the ships health (hull)
     * @param shieldDamage The damage to the ships shields
     */
    public void takeDamage(int shipDamage, int shieldDamage) {
    	if(getResource(Resource.Type.SHIELDS).get() > 0) {
    		getResource(Resource.Type.SHIELDS).alter(-shieldDamage);
    	} else {
    		getResource(Resource.Type.HEALTH).alter(-shipDamage);
    	}
    }
    
    /**
     * Determine whether this instance's health is zero or below.
     */
    public boolean hasNoHealth() {
        return health.get() <= 0;
    }
    
/// FIRING
    
    public Bullet fire(Weapon.Type type) {
        switch(type) {
        case LASER:
            return laser.fire();
        case PLASMA:
            return plasma.fire();
        case TORPEDO:
            return torpedo.fire();
        default:
            throw new RuntimeException("Apparently, you asked to fire a weapon that doesn't exist, which is impossible. WOOPS!");
        }
    }
    
/// SHIP MOVEMENT
    
    private static final double PITCH_ACC   = PITCH_VEL_MAX	/ PITCH_SMOOTHNESS;
    private static final double YAW_ACC   	= YAW_VEL_MAX   / YAW_SMOOTHNESS;
    private static final double ROLL_ACC	= ROLL_VEL_MAX	/ ROLL_SMOOTHNESS;
    private static final double THRUST_ACC  = FWD_VEL_MAX   / THRUST_SMOOTHNESS;
    
    private static final Rotation   PITCH_UP    = new Rotation(Vector.I.scale(  PITCH_ACC * Global.REFRESH_PERIOD));
    private static final Rotation   PITCH_DOWN  = new Rotation(Vector.I.scale(- PITCH_ACC * Global.REFRESH_PERIOD));
    private static final Rotation   YAW_RIGHT   = new Rotation(Vector.K.scale(- YAW_ACC   * Global.REFRESH_PERIOD));
    private static final Rotation   YAW_LEFT 	= new Rotation(Vector.K.scale(  YAW_ACC   * Global.REFRESH_PERIOD));
    private static final Rotation 	ROLL_RIGHT	= new Rotation(Vector.J.scale(  ROLL_ACC  * Global.REFRESH_PERIOD));
    private static final Rotation	ROLL_LEFT	= new Rotation(Vector.J.scale(- ROLL_ACC  * Global.REFRESH_PERIOD));
    private static final Vector     THRUST_FWD  = Vector.J          .scale(THRUST_ACC);
    private static final Vector     THRUST_REV  = Vector.J.negate() .scale(THRUST_ACC);
    
    private static final Rotation ROTATE_RIGHT	= ROLLING ? ROLL_RIGHT 	: YAW_RIGHT;
    private static final Rotation ROTATE_LEFT	= ROLLING ? ROLL_LEFT	: YAW_LEFT; 
    
    public void pitchUp() {
        if(engines.get() > 0) {
            rotate(PITCH_UP);
            Basis b = getBasis();
            setVelocity(b.globaliseDirection(PITCH_UP.apply(getBasis().localiseDirection(getVelocity()))));
        }
    }
    
    public void pitchDown() {
        if(engines.get() > 0) {
            rotate(PITCH_DOWN);
            Basis b = getBasis();
            setVelocity(b.globaliseDirection(PITCH_DOWN.apply(getBasis().localiseDirection(getVelocity()))));
        }
    }
    
    public void rotateRight() {
        if(engines.get() > 0) {
            rotate(ROTATE_RIGHT);
            Basis b = getBasis();
            setVelocity(b.globaliseDirection(ROTATE_RIGHT.apply(getBasis().localiseDirection(getVelocity()))));
        }
    }
    
    public void rotateLeft() {
        if(engines.get() > 0) {
            rotate(ROTATE_LEFT);
            Basis b = getBasis();
            setVelocity(b.globaliseDirection(ROTATE_LEFT.apply(getBasis().localiseDirection(getVelocity()))));
        }
    }
    
    public void thrustForward() {
        if(engines.get() > 0    &&   getBasis().localiseDirection(getVelocity()).getY() < +FWD_VEL_MAX) {
            alterVelocityLocally(THRUST_FWD);
            engines.decrease(); // TODO: Might want to change this to a better value
        }
    }
    
    public void thrustReverse() {
        if(engines.get() > 0    &&    getBasis().localiseDirection(getVelocity()).getY() > -FWD_VEL_MAX) {
            alterVelocityLocally(THRUST_REV);
            engines.decrease();
        }
    }
    
/// UPDATE METHOD
    
    public void update() {
        // Update physical parameters
        super.update();
        
        if(collisionAllowedTick > 0) {
        	collisionAllowedTick -= Global.REFRESH_PERIOD;
        }
        
        // Update status of weapons
        laser.update();
        plasma.update();
        torpedo.update();
        
    // Now decay and bound velocities
        Vector w            = getAngularVelocity();
        Vector planarSpin   = w.proj(getUpVector());
        Vector nonplanarSpin= w.minus(planarSpin);
        setAngularVelocity(planarSpin.plus(nonplanarSpin.length() < NONPLANAR_SPIN_MIN ? Vector.ZERO : nonplanarSpin.scale(NONPLANAR_SPIN_DECAY)));
        
        Vector v            = getVelocity();
        Vector forwardVel   = v.proj(getFrontVector());
        Vector orthogVel    = v.minus(forwardVel);
        setVelocity(forwardVel.plus(orthogVel.length() < ORTHOGONAL_VELOCITY_MIN ? Vector.ZERO : orthogVel.scale(ORTHOGONAL_VELOCITY_DECAY)));
    }
}
