package AI;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Geometry.*;
import Physics.*;

/**
 * A class of bots that have a relatively simple implementation of targeting. A bot of
 * this class has an associated target body which it will try to destroy by finding the
 * shortest distance path from it to that body and attempting to manoeuvre in that direction
 * If the distance and angle between the bot's firing line and the target are in a reasonable
 * range, the bot will fire repeatedly in an attempt to destroy the target.
 * @author jivan
 */
public class TargetingBot extends AbstractBot {
/// FIELDS
    private Ship ship;
    private Body target;
    
/// CONSTANTS
    
    /**
     * The angle within which to attempt to fire at the target, in radians.
     */
    // The argument of Math.toRadians is, of course, given in degrees here.
    private final double IN_RANGE_ANGLE = Math.toRadians(40);
    
    /**
     * The distance within which to attempt to fire at the target, in meters.
     */
    private final double IN_RANGE_DISTANCE = MapContainer.MAP_SIZE * 0.4;
    
/// CONSTRUCTOR
    
    /**
     * Given a ship's ID, wrap this class's behaviour around it if it exists on the manager's map. 
     * @param shipID the ID of the ship to wrap this class's behaviour around.
     */
    public TargetingBot(Map map, int shipID, int targetID) {
        super(map, shipID);
        if (getBody() instanceof Ship) {
            ship = (Ship)getBody();
        } else {
            throw new IllegalArgumentException("TargetingBot instantiation: FAILED: the body with ID "+shipID+" is not a Ship.");
        }
        if (!setTarget(targetID)) {
            throw new IllegalArgumentException("TargetingBot instantiation: FAILED: cannot set target; no body with ID "+targetID+" exists on map.");
        }
    }
    
/// INSTANCE METHODS
    
// Setters
    
    /**
     * Set this bot's target by specifying the target's ID.
     * @param targetID the ID of the body to set as this instance's target. If
     *      no body with this ID exists on the map, the target is unchanged.
     * @return `true` if and only if the target was set to the one specified.
     */
    public boolean setTarget(int targetID) {
        Body b;
        boolean targetSet;
        if (targetSet = ((b = getMap().get(targetID)) != null)) {
            target = b;
        }
        return targetSet;
    }
    
// Evolution
    
    public void update() {
        // Get the direction vector towards the target in the local basis.
        Vector pathToTarget = ship.getBasis().localiseDirection(getMap().shortestPath(ship.getID(), target.getID()));
        
        // If the target is in range, fire.
        if (
                Vector.J.angleWith(pathToTarget) < IN_RANGE_ANGLE
            &&  pathToTarget.length() < IN_RANGE_DISTANCE
        ) {
            ship.fire(Weapon.Type.LASER);
        }
        
        // Pilot the ship accordingly.
        
        if (pathToTarget.getX() > 0) {
            ship.rotateRight();
        } else if (pathToTarget.getX() < 0) {
            ship.rotateLeft();
        }
        
        if (pathToTarget.getZ() > 0) {
            ship.pitchUp();
        } else if (pathToTarget.getZ() < 0) {
            ship.pitchDown();
        }
        
        // Get ETA until target lies in the bot's x-z plane, in seconds.
        double timeToTarget = pathToTarget.getY() / ship.getVelocity().getY();
        
        if (pathToTarget.length() < 100) {
            if (timeToTarget < 1) {
                ship.thrustReverse();
            } else if (timeToTarget > 3) {
                ship.thrustForward();
            }
        }
        if (timeToTarget < 4) {
            ship.thrustReverse();
        } else if (timeToTarget > 5) {
            ship.thrustForward();
        }
    }
}
