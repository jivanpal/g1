package GameLogic;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

import GameLogic.*;
import AI.*;
import Geometry.*;
import Physics.Body;

/**
 * A class to describe an instance of the game map, which is
 * a looping map, akin to those in games like Pac-Man, but in
 * 3D rather than simply 2D.
 * @author jivan
 */
public class Map implements Serializable {
/// FIELDS
    private Vector dimensions;
    private ConcurrentSkipListMap<Integer,Body> bodies      = new ConcurrentSkipListMap<Integer,Body>();
    private ConcurrentSkipListMap<Integer,AbstractBot> bots = new ConcurrentSkipListMap<Integer,AbstractBot>();
    public int nextBotID = 0;
    
/// CONSTRUCTORS
    
    /**
     * Construct a map with the given dimensions.
     * @param dimensions a vector whose components describe the map's
     *      dimensions with respect to each axis.
     */
    public Map(Vector dimensions) {
        this.dimensions = dimensions;
    }
    
    /**
     * Construct a map with the given dimensions
     * @param x the map's width / size in the x-dimension.
     * @param y the map's length / size in the y-dimension.
     * @param z the map's height / size in the z-dimension.
     */
    public Map(int x, int y, int z) {
        this(new Vector(x,y,z));
    }
    
/// INSTANCE METHODS
    
// Getters
    
    /**
     * Get the dimensions of the map.
     */
    public Vector getDimensions() {
        return dimensions;
    }
    
// Body management
    
    /**
     * Add a new body to the map.
     * @param newBody the body to add to the map. If a body with the same ID already exists
     *      on the map, this one won't be added.
     * @return `true` if the body was added, else `false`.
     */
    public boolean add(Body newBody) {
        if (newBody == null || this.contains(newBody.getID())) {
            return false;
        } else {
            bodies.put(newBody.getID(), newBody);
            return true;
        }
    }
    
    /**
     * Remove a body from the map.
     * @param bodyID the ID of the body to remove from the map.
     * @return `true` if a body with that ID was on the map, and has subsequently been
     *      removed due to invoking this command, else `false` because no body with the
     *      specified ID exists on the map. 
     */
    public boolean remove(int bodyID) {
        return bodies.remove(bodyID) != null;
    }
    

    
    /**
     * Get the body on this map that has the given ID.
     * @param bodyID the ID of the body to get.
     * @return the body with that ID, or `null` if no body with that ID exists on this map.
     */
    public Body get(int bodyID) {
        return bodies.get(bodyID);
    }
    
    /**
     * Get a collection of all the IDs of bodies on the map.
     * Useful to iterate over all bodies on the map.
     */
    public Collection<Integer> bodyIDs() {
        return bodies.keySet();
    }
    
    /**
     * Get a collection of all the bodies on the map.
     * Useful to iterate over all bodies on the map.
     */
    public Collection<Body> bodies() {
        return bodies.values();
    }
    
    /**
     * Get the number of bodies on the map.
     */
    public int size() {
        return bodies.size();
    }
    
    /**
     * Determine whether this map contains the body with a given ID.
     * @param bodyID the ID of the body to check the presence of.
     * @return whether a body with that ID is present.
     */
    public boolean contains(int bodyID) {
        return bodies.containsKey(bodyID);
    }
    
// Bot management
    
    /**
     * Add a new bot to the map. 
     * @param newBot the bot to add. If a bot with the same ID as this one already exists
     *      on the map, this one won't be added.
     * @return the ID of the bot that was added, or -1 if the bot object is invalid or
     *      conflicts with an already existing bot's ID.
     */
    public int addBot(AbstractBot newBot) {
        if (newBot == null || this.containsBot(newBot.getID())) {
            return -1;
        } else {
            bots.put(newBot.getID(), newBot);
            return newBot.getID();
        }
    }
    
    /**
     * Remove a bot with a given ID from the map.
     * @param botID the ID of the bot to remove.
     * @return `true` if a bot with that ID was on the map, and has subsequently been
     *      removed due to invoking this command, else `false` because no bot with the
     *      specified ID exists on the map.
     */
    public boolean removeBot(int botID) {
        return bots.remove(botID) != null;
    }
    
    /**
     * Get the bot with a given ID.
     * @param botID the ID of the bot to get.
     * @return the bot with that ID.
     */
    public AbstractBot getBot(int botID) {
        return bots.get(botID);
    }
    
    /**
     * Get a collection of the IDs of all bots that are on the map.
     */
    public Collection<Integer> botIDs() {
        return bots.keySet();
    }
    
    /**
     * Get a collection of all the bot instances that are on the map.
     * @return
     */
    public Collection<AbstractBot> bots() {
        return bots.values();
    }
    
    /**
     * Get the number of bots that are on the map.
     */
    public int numberOfBots() {
        return bots.size();
    }
    
    /**
     * 
     * @param botID
     * @return
     */
    public boolean containsBot(int botID) {
        return bots.containsKey(botID);
    }
    
// Other methods
    
    /**
     * Given a position vector with any components, convert it to the
     * smallest vector with positive components that represents the same
     * position on the map.
     * 
     * For example, if the map has dimensions (2, 5, 7), then applying this method
     * to the position vector (5, 6, 13) returns the vector (1, 1, 6), and applying
     * it to (-4, 2, -24) returns (0, 2, 3).
     * 
     * @param position the position vector to be normalised.
     * @return the normalised position vector; the position vector, put within the bounds of the map.
     */
    public Vector normalisePosition(Vector position) {
        return new Vector(
            position.getX() % dimensions.getX() + (position.getX() % dimensions.getX() < 0 ? dimensions.getX() : 0),
            position.getY() % dimensions.getY() + (position.getY() % dimensions.getY() < 0 ? dimensions.getY() : 0),
            position.getZ() % dimensions.getZ() + (position.getZ() % dimensions.getZ() < 0 ? dimensions.getZ() : 0)
        );
    }
    
    /**
     * Get the direction vector representing the shortest path between
     * two bodies on this map.
     * @param aID the ID of the origin body.
     * @param bID the ID of the destination body.
     * @return the shortest vector from <i>a</i> to <i>b</i>. Note that
     *      this method is anti-commutative.
     */
    public Vector shortestPath(int aID, int bID) {
        Body a, b;
        if ( (a = get(aID)) != null    &&    (b = get(bID)) != null ) {
            Vector lineWithinBounds = b.getPosition().minus(a.getPosition());
            return new Vector(
                lineWithinBounds.getX() > dimensions.getX() ?
                    dimensions.getX() - lineWithinBounds.getX():
                    lineWithinBounds.getX(),
                lineWithinBounds.getY() > dimensions.getY() ?
                    dimensions.getY() - lineWithinBounds.getY():
                    lineWithinBounds.getY(),
                lineWithinBounds.getZ() > dimensions.getZ() ?
                    dimensions.getZ() - lineWithinBounds.getZ():
                    lineWithinBounds.getZ()
            );
        } else {
            throw new IllegalArgumentException("shortestPath: body with ID "+(a==null ? aID : bID)+" doesn't exist on map.");
        }
    }
    
    /**
     * Return the shortest possible path between two bodies in the next tick, as if
     * positions truly changed continuously rather than discretely.
     * @param aID the ID of the first body.
     * @param bID the ID of the second body.
     * @return the shortest distance between the two bodies in the next tick, in seconds.
     *      Note that this method is commutative.
     */
    public Vector shortestPathThisTick(int aID, int bID) {
        Body a, b;
        if ( (a = get(aID)) != null    &&    (b = get(bID)) != null ) {
        // Insane linear algebra stuff, optimised.
            // Get endpoints of line segments, with a0 implicitly at origin,
            //  i.e. as if `Vector a0 = Vector.ZERO`.
            Vector a1 = a.getVelocity().scale(Global.REFRESH_PERIOD);
            Vector b0 = shortestPath(aID,bID);
            Vector b1 = b0.plus(b.getVelocity().scale(Global.REFRESH_PERIOD));
            
            // Compute frequently used values
            Vector a0b0 = b0.negate();  // a0 - b0
            Vector a0a1 = a1.negate();  // a0 - a1
            Vector b0b1 = b0.minus(b1); // b0 - b1
            
            double a0a1DOTb0b1 = a0a1.dot(b0b1);
            double a0b0DOTb0b1 = a0b0.dot(b0b1);
            double b0b1DOTb0b1 = b0b1.dot(b0b1);
            
            // Compute parameter values for points on A-line
            //  and B-line that give shortest vector.
            double lambda   = (a0b0DOTb0b1 * a0a1DOTb0b1 - a0b0.dot(a0a1) * b0b1DOTb0b1) / (a0a1DOTb0b1 * a0a1DOTb0b1 - a0a1.dot(a0a1) - b0b1DOTb0b1);
            double mu       = (lambda * a0a1DOTb0b1 - a0b0DOTb0b1) / b0b1DOTb0b1;
            
            // Get the right vector
            lambda = lambda < 0 ? 0 : lambda > 1 ? 1 : lambda;
            mu = mu < 0 ? 0 : mu > 1 ? 1 : mu;
            return b0.plus(b1.scale(mu)).minus(a1.scale(lambda));
        } else {
            throw new IllegalArgumentException("shortestTickDistance: body with ID "+(a==null ? aID : bID)+" doesn't exist on map.");
        }
    }
    
    /**
     * Get the shortest vector from one body to another whose projection onto
     * the origin body's front vector is in the direction of the front vector.
     * @param aID the ID of the origin body.
     * @param bID the ID of the destination body.
     * @return
     */
    public Vector shortestForwardPath(int aID, int bID) {
        Body a, b;
        if ( (a = get(aID)) != null    &&    (b = get(bID)) != null) {
            Vector aPos = a.getPosition();
            Vector[] bPos = getAllPositions(b.getPosition());
            
//            Vector[] path = new Vector[bPos.length];
//            for (int i = 0; i < path.length; i++) {
//                path[i] = bPos[i].minus(aPos);
//            }
//            
//            boolean[] inFront = new boolean[path.length];
//            for (int i = 0; i < inFront.length; i++) {
//                inFront[i] = path[i].dot(a.getFrontVector()) > 0;
//            }
//            
//            Vector shortest = dimensions;
//            for (int i = 0; i < path.length; i++) {
//                if (inFront[i] && path[i].length() < shortest.length()) {
//                    shortest = path[i];
//                }
//            }
            
            Vector frontVector = a.getFrontVector();
            Vector shortest = dimensions;
            for (int i = 0; i < bPos.length; i++) {
                Vector path = bPos[i].minus(aPos);
                if ( path.dot(frontVector) > 0    &&    path.length() < shortest.length() ) {
                    shortest = path;
                }
            }
            
            return shortest;
        } else {
            throw new IllegalArgumentException("shortestTickDistance: body with ID "+(a==null ? aID : bID)+" doesn't exist on map.");
        }
    }
    
    /**
     * Determine whether two bodies on this map overlap / are touching.
     * @param aID the ID of one body involved
     * @param bID the ID of the other body involved
     * @return `true` if and only if the specified bodies are overlapping
     */
    public boolean overlaps(int aID, int bID) {
        return this.shortestPath(aID, bID).length() < get(aID).getRadius() + get(bID).getRadius();
    }
    
    /**
     * Given a position vector that lies within the bounds of this
     * map, get an array containing the 27 vectors corresponding to
     * that position vector that reside in the primary map space, as
     * well as all its direct neighbours.
     * @param position the position vector of the body that is within the bounds of this map.
     * @return an array with the position vectors for each of the 27 instances.
     */
    public Vector[] getAllPositions(Vector position) {
        // Get the central position vector.
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();
        
        Vector[] positions = new Vector[] {
            position.plus(new Vector(-x, -y, -z)),  // -z   // -y   // -x
            position.plus(new Vector( 0, -y, -z)),                  //  0
            position.plus(new Vector( x, -y, -z)),                  //  x
            
            position.plus(new Vector(-x,  0, -z)),          //  0
            position.plus(new Vector( 0,  0, -z)),
            position.plus(new Vector( x,  0, -z)),
            
            position.plus(new Vector(-x,  y, -z)),          //  y
            position.plus(new Vector( 0,  y, -z)),
            position.plus(new Vector( x,  y, -z)),
            
            
            position.plus(new Vector(-x, -y,  0)),  //  0
            position.plus(new Vector( 0, -y,  0)),
            position.plus(new Vector( x, -y,  0)),
            
            position.plus(new Vector(-x,  0,  0)),
            position.plus(new Vector( 0,  0,  0)),
            position.plus(new Vector( x,  0,  0)),
            
            position.plus(new Vector(-x,  y,  0)),
            position.plus(new Vector( 0,  y,  0)),
            position.plus(new Vector( x,  y,  0)),
            
            
            position.plus(new Vector(-x, -y,  z)),  //  z 
            position.plus(new Vector( 0, -y,  z)),
            position.plus(new Vector( x, -y,  z)),
            
            position.plus(new Vector(-x,  0,  z)),
            position.plus(new Vector( 0,  0,  z)),
            position.plus(new Vector( x,  0,  z)),
            
            position.plus(new Vector(-x,  y,  z)),
            position.plus(new Vector( 0,  y,  z)),
            position.plus(new Vector( x,  y,  z)),
        };
        return positions;
        
        // Succinct version
//        Vector bPosInBounds = b.getPosition();
//        
//        Vector[] bPos = new Vector[27];
//        double mapX = dimensions.getX();
//        double mapY = dimensions.getY();
//        double mapZ = dimensions.getZ();
//        for(int i = 0; i < bPos.length; i++) {
//            int x =  (i      % 3) - 1;
//            int y = ((i / 3) % 3) - 1;
//            int z = ((i / 9) % 3) - 1;
//            
//            bPos[i] = bPosInBounds.plus(new Vector( x*mapX, y*mapY, z*mapZ ));
//        }
    }
    
// Evolution
    
    /**
     * Update the state of the map.
     */
    public void update() {
        // Get rid of destroyed bodies and expired bullets,
        // normalise each body's position vector,
        // and update their states.
        for(Body b : bodies()) {
            if (b.isDestroyed()) {
                this.remove(b.getID());
            }
            else {
                b.setPosition(this.normalisePosition(b.getPosition()));
                b.update();
            }
        }
        
        // Make bots do their thing.
        for(AbstractBot b : bots()) {
            b.update();
        }
        
        // Make touching bodies rebound.
        for (int aID : bodyIDs()) {
            // Store values pertaining to A that are used more than once,
            // in an effort to optimise this procedure.
            Body a = get(aID);
            double aRadius = a.getRadius();
            double aMass = a.getMass();
            
            for (int bID : bodyIDs()) {
                Body b = get(bID);
                Vector lineOfAction = shortestPath(aID, bID);
                
                // If A and B are touching, then they rebound.
                if (aID < bID && lineOfAction.length() < aRadius + b.getRadius()) {
                    // a.destroy();
                    // b.destroy();
                	
                	if(a instanceof Ship) {
                		Ship s = (Ship) a;
                		
                		if(b instanceof Bullet) {
                			Bullet bullet = (Bullet) b;
                			s.takeDamage(bullet.getShipDamage(), bullet.getShieldDamage());
                			
                			// Destroy the bullet now that it has collided
                			b.destroy();
                		} else if (b instanceof Asteroid) {
                			s.takeDamage(Asteroid.DAMAGE_TO_SHIP, Asteroid.DAMAGE_TO_SHIP);
                			
                			// Destroy the asteroid
                			b.destroy();
                		} else if (b instanceof Ship) {
                			s.takeDamage(Ship.DAMAGE_TO_OTHER_SHIPS, Ship.DAMAGE_TO_OTHER_SHIPS);
                			
                			// Make sure to damage the other ship as well
                			Ship s2 = (Ship) b;
                			((Ship) b).takeDamage(Ship.DAMAGE_TO_OTHER_SHIPS, Ship.DAMAGE_TO_OTHER_SHIPS);
                			
                			if(s2.getResource(Resource.Type.HEALTH).get() <= 0) {
                				s2.destroy();
                			}
                		}
                		
                		if(s.getResource(Resource.Type.HEALTH).get() <= 0) {
                			s.destroy();
                		}
                		
                	}
                    
                    
                    
//                    // Components of velocities that lie on the line of action
//                    Vector aVel = a.getVelocity().proj(lineOfAction);
//                    Vector bVel = b.getVelocity().proj(lineOfAction);
//                    
//                    // Store mass of B as it is used several times
//                    double bMass = b.getMass();
//                    double recipMassSum = 1/(aMass + bMass);
//                    
//                    // Elastic collision between A and B
//                    a.setVelocity(aVel.scale(aMass - bMass).plus(bVel.scale(2*bMass)).scale(recipMassSum));
//                    b.setVelocity(bVel.scale(bMass - aMass).plus(aVel.scale(2*aMass)).scale(recipMassSum));
                }
            }
        }
    }
}
