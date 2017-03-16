package GameLogic;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

import Geometry.Vector;
import Physics.Body;

public class BotManager implements Serializable {
    private Map map;
    private ConcurrentSkipListMap<Integer,AbstractBot> bots;
    private int nextID = 0;
    
    /**
     * Create a `BotManager` instance to manage bots for a specified map.
     * @param map the map to manage bots for.
     */
    public BotManager(Map map) {
        this.map = map;
        this.bots = new ConcurrentSkipListMap<>();
    }
    
    /**
     * Get a collection of all the eneabled bots on the map managed by this instance.
     * Used to iterate over all bots when updating the game state.
     * @return a collection of all enabled bots on the map managed by this instance.
     */
    public Collection<AbstractBot> bots() {
        return bots.values();
    }
    
/// ABSTRACT INNER CLASS
    
    /**
     * A class that defines methods to wrap automated in-game behaviour (AI)
     * around an instance of `Body`.
     * @author jivan
     */
    public abstract class AbstractBot implements Serializable {
    /// FIELDS
        private int ID;
        private Body body;
        private boolean enabled = false;
        
    /// CONSTRUCTORS
        
        /**
         * Given a map and the index of a body on that map, turn that body into a bot.
         * @param bodyID the ID of the body on the specified map to define as a bot.
         */
        public AbstractBot(int bodyID) {
            body = map.get(bodyID);
            if (body == null) {
                System.err.println("Bot instantiation: body with ID "+bodyID+"doesn't exist on the specified map.");
                new RuntimeException().printStackTrace();
            } else {
                ID = nextID++;
            }
        }
        
    /// INSTANCE METHODS
        
    // Getters
        
        /**
         * Get the ID of this bot.
         */
        public int getID() {
            return ID;
        }
        
        /**
         * Get the body that this instance was constructed from.
         */
        public Body getBody() {
            return body;
        }
        
        /**
         * Get the map that this bot resides on.
         */
        public Map getMap() {
            return map;
        }
        
        /**
         * Determine whether this bot is enabled / actively
         * behaving as it is defined, or not.
         * @return `true` if enabled, `false` if not.
         */
        public boolean isEnabled() {
            return enabled;
        }
        
    // Setters
        
        /**
         * Set the status of whether this bot is enabled / actively
         * behaving as it is defined. 
         * @param status `true` to enable this instance, `false` to disable it.
         */
        public void setEnabled(boolean status) {
            if (enabled != status) {
                if (status) {
                    bots.put(this.getID(), this);
                } else {
                    bots.remove(this.getID());
                }
            }
        }
        
    // Evolution
        
        /**
         * This method is where the behaviour for the bot is defined. The body of
         * this method should describe how the bot's state changes with each tick
         * of the in-game clock, as this method is called with every new frame
         * that is generated.
         */
        public abstract void update();
    }
    
/// TARGETING BOT IMPLEMENTATION
    
    /**
     * A class of bots that have a relatively simple implementation of targeting. A bot of
     * this class has an associated target body which it will try to destroy by finding the
     * shortest distance path from it to that body and attempting to manoeuvre in that direction
     * If the distance and angle between the bot's firing line and the target are in a reasonable
     * range, the bot will fire repeatedly in an attempt to destroy the target.
     * @author jivan
     */
    public class TargetingBot extends BotManager.AbstractBot {
    /// FIELDS
        private Ship ship;
        private Body target;
        
    /// CONSTANTS
        
        /**
         * The angle within which to attempt to fire at the target, in radians.
         */
        // The argument of Math.toRadians is, of course, given in degrees here.
        private final double IN_RANGE_ANGLE = Math.toRadians(20);
        
        /**
         * The distance within which to attempt to fire at the target, in meters.
         */
        private final double IN_RANGE_DISTANCE = 100;
        
    /// CONSTRUCTORS
        
        /**
         * Given a ship's ID, wrap this class's behaviour around it if it exists on the manager's map. 
         * @param shipID the ID of the ship to wrap this class's behaviour around.
         */
        public TargetingBot(int shipID) {
            super(shipID);
            if (getBody() instanceof Ship) {
                ship = (Ship)getBody();
            } else {
                throw new IllegalArgumentException("TargetingBot instantiation: FAILED: the body with the specified ID is not a Ship.");
            }
        }
        
    /// INSTANCE METHODS
        
    // Setters
        
        /**
         * Set this bot's target by specifying the target's ID.
         * @param targetID the ID of the body to set as this instance's target.
         */
        public void setTarget(int targetID) {
            target = map.get(targetID);
        }
        
    // Evolution
        
        public void update() {
            // Get the direction vector towards the target in the global basis.
            Vector pathToTarget = map.shortestPath(ship.getID(), target.getID());
            
            // If the target is in range, fire.
            if (
                    ship.getFrontVector().angleWith(pathToTarget) < IN_RANGE_ANGLE
                &&  pathToTarget.length() < IN_RANGE_DISTANCE
            ) {
                ship.fire(Weapon.Type.LASER);
            }
            
            // Get some useful vectors for heuristics, described in the bot's local basis.
            Vector desiredDirection = ship.getBasis().localiseDirection(pathToTarget);
            
        // Pilot the ship accordingly.
            
            if (desiredDirection.getX() > 0) {
                ship.rotateRight();
            } else if (desiredDirection.getX() < 0) {
                ship.rotateLeft();
            }
            
            if (desiredDirection.getZ() > 0) {
                ship.pitchUp();
            } else if (desiredDirection.getZ() < 0) {
                ship.pitchDown();
            }
            
            // Get ETA until target lies in the bot's x-z plane, in seconds.
            double timeToTarget = desiredDirection.getY() / ship.getVelocity().getY();
            
            if (timeToTarget > 5) {
                ship.thrustReverse();
            } else if (timeToTarget < 4) {
                ship.thrustForward();
            }
        }
    }
}
