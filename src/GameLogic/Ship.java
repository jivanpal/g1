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
    //   Maximum values for pitch velocity, roll velocity, and forward velocity.
    //   The ship is designed under the assumption that it cannot reverse,
    // therefore the reverse-velocity maximum is assumed to be zero.
    private static final double PITCH_VEL_MAX = 0.1;  // radians per second
    private static final double ROLL_VEL_MAX  = 0.3;  // radians per second
    private static final double FWD_VEL_MAX   = 20;   // meters per second
    
    // Smoothness of transition from rest to maximum for each axis.
    // Higher values result in smoother transitions.
    private static final int    PITCH_SMOOTHNESS    = 10;
    private static final int    ROLL_SMOOTHNESS     = 10;
    private static final int    THRUST_SMOOTHNESS   = 4;
    
    private String engineerName, pilotName;
    private Weapon laser, plasma, torpedo;
    private Resource engines, shields, health;
    
    /**
     * Create a ship belonging to a specified pilot and engineer. 
     * @param pilotName the nickname of this instance's pilot.
     * @param engineerName the nickname of this instance's engineer.
     */
    public Ship(String pilotName, String engineerName){
        // Set mass and radius
        super(100, 2);
        
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
    
    public String getPilotName() {
        return pilotName;
    }
    
    public String getEngineerName() {
        return engineerName;
    }
    
/// GETTERS
    
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
    
// Modified `update` method to affect all ship fields
    
    public void update() {
        super.update();
        
        laser.update();
        plasma.update();
        torpedo.update();
    }
}
