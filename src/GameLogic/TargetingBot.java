package GameLogic;

import Physics.*;
import Geometry.*;

/**
 * A class of bots that have a relatively simple implementation of targeting. A bot of
 * this class has an associated target body which it will try to destroy by finding the
 * shortest distance path from it to that body and attempting to manoeuvre in that direction
 * If the distance and angle between the bot's firing line and the target are in a reasonable
 * range, the bot will fire repeatedly in an attempt to destroy the target.
 * @author jivan
 */
public class TargetingBot extends Bot {
/// FIELDS
    private Body target;
    
/// CONSTANTS
    
    /**
     * The angle within which to attempt to fire at the target, in radians.
     * The argument to Math.toRadians here is, of course, given in degrees.
     */
    private final double IN_RANGE_ANGLE = Math.toRadians(20);
    
    /**
     * The distance within which to attempt to fire at the target, in meters.
     */
    private final double IN_RANGE_DISTANCE = 100;
    
/// CONSTRUCTORS
    
    /**
     * Given a body and the game map that it is on, turn that body into a bot.
     * @param   map     The game map that the body is on.
     * @param   ship    The ship to turn into a bot.
     */
    public TargetingBot(Map map, Ship ship) {
        super(map, ship);
    }
    
    /**
     * Given a map and the index of a body on that map, turn that body into a bot.
     * @param   map             The game map that the body is on.
     * @param   shipMapIndex    The map index of the ship to turn into a bot.
     */
    public TargetingBot(Map map, int shipMapIndex) {
        super(map, shipMapIndex);
        if ( ! (getBotBody() instanceof Ship) ) {
            throw new IllegalArgumentException("The specified body is not a ship.");
        }
    }
    
/// INSTANCE METHODS
    
    public void setTarget(Body target) {
        if (getMap().contains(target)) {
            this.target = target;
        } else {
            throw new IllegalArgumentException("The specified target body does not exist on this bot's map.");
        }
    }
    
// Targeting
    
// Evolution
    
    public void update() {
        Ship bot = (Ship) getBotBody();
        
        // Get the direction vector towards the target in the global basis.
        Vector pathToTarget = getMap().shortestPath(bot, target);
        
        // If the target is in range, fire.
        if (
                bot.getForwardVector().angleWith(pathToTarget) < IN_RANGE_ANGLE
            &&  pathToTarget.length() < IN_RANGE_DISTANCE
        ) {
            bot.fire(Ship.LASER_BLASTER_INDEX);
        }
        
        // Get some useful vectors for heuristics, described in the bot's local basis.
        Vector desiredDirection = bot.getOrientation().inverse().apply(pathToTarget);
        
    // Pilot the bot-ship accordingly.
        
        if (desiredDirection.getX() > 0) {
            bot.rollRight();
        } else if (desiredDirection.getX() < 0) {
            bot.rollLeft();
        }
        
        if (desiredDirection.getZ() > 0) {
            bot.pitchUp();
        } else if (desiredDirection.getZ() < 0) {
            bot.pitchDown();
        }
        
        // Get ETA until target lies in the bot's x-z plane, in seconds.
        double timeToTarget = desiredDirection.getY() / bot.getVelocity().getY();
        
        if (timeToTarget > 5) {
            bot.thrustReverse();
        } else if (timeToTarget < 4) {
            bot.thrustForward();
        }
    }
}
