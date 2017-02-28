package GameLogic;

import java.util.ArrayList;
import Geometry.*;
import Physics.*;

/**
 * A class to describe an instance of the game map, which is
 * a looping map, akin to those in games like Pac-Man, but in
 * 3D rather than simply 2D.
 * @author jivan
 */
public class Map extends ArrayList<Body> {
/// FIELDS
    private Vector dimensions;
    private ArrayList<Bot> bots = new ArrayList<Bot>();
    
/// CONSTRUCTORS
    
    /**
     * Construct a map with the given dimensions
     * @param   x   The map's width / size in the x-dimension.
     * @param   y   The map's length / size in the y-dimension.
     * @param   z   The map's height / size in the z-dimension.
     */
    public Map(int x, int y, int z) {
        this(new Vector(x,y,z));
    }
    
    /**
     * Construct a map with the given dimensions.
     * @param   dimensions  A vector whose components describe the map's
     *              dimensions in each axis direction.
     */
    public Map(Vector dimensions) {
        this.dimensions = dimensions;
    }
    
/// INSTANCE METHODS
    
// Getters
    
    /**
     * Get the dimensions of the map.
     */
    public Vector getDimensions() {
    	return dimensions;
    }
    
    public ArrayList<Bot> getBotList() {
        return bots;
    }
    
// Other methods
    
    /**
     * Given a position vector with any components, convert it to the
     * smallest vector with positive components that represents the same
     * position on the map.
     * 
     * For example, if the map has dimensions (2,5,7), then applying this method
     * to the position vector (5, 6, 13) returns the vector (1, 1, 6), and applying
     * it to (-4, 2, -24) returns (0, 2, 3).
     * 
     * @param   position    The position vector to be normalised.
     * @return  the normalised position vector; the position vector put within the bounds of the map.
     */
    public Vector normalise(Vector position) {
        return position.modulo(dimensions);
    }
    
    /**
     * Get the direction vector representing the shortest path from one body
     * on this map to another on this map.
     * @param   a   The origin body.
     * @param   b   The destination body.
     * @return  the shortest vector from <i>a</i> to <i>b</i>. Note that this
     *      function is anti-commutative; for all <i>a</i> and <i>b</i>, we have
     *      shortestPath(<i>a</i>, <i>b</i>) = -shortestPath(<i>b</i>, <i>a</i>).
     */
    public Vector shortestPath(Body a, Body b) {
        if (this.contains(a)) {
            if (this.contains(b)) {
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
                throw new IllegalArgumentException(
                    "The second body specified does not reside on this map."
                + "\nThe body in question is '"+b+"'."
                );
            }
        } else {
            throw new IllegalArgumentException(
                "The first body specified does not reside on this map."
            + "\nThe body in question is '"+a+"'."
            );
        }
    }
    
    /**
     * Given a position vector, get an array containing the 27 vectors
     * corresponding to that position vector that reside in the primary
     * map space, as well as all its direct neighbours.
     * @param   position    The position vector.
     * @return  an array with the position vectors for each of the 27 instances.
     */
    public Vector[] getAllPositions(Vector position) {
        // Get the central position vector.
        position = position.modulo(dimensions);
        
        double x = dimensions.getX();
        double y = dimensions.getY();
        double z = dimensions.getZ();
        
        Vector[] positions = new Vector[] {
            position.plus(new Vector(-x, -y, -z)),
            position.plus(new Vector( 0, -y, -z)),
            position.plus(new Vector( x, -y, -z)),
            
            position.plus(new Vector(-x,  0, -z)),
            position.plus(new Vector( 0,  0, -z)),
            position.plus(new Vector( x,  0, -z)),
            
            position.plus(new Vector(-x,  y, -z)),
            position.plus(new Vector( 0,  y, -z)),
            position.plus(new Vector( x,  y, -z)),
            
            
            position.plus(new Vector(-x, -y,  0)),
            position.plus(new Vector( 0, -y,  0)),
            position.plus(new Vector( x, -y,  0)),
            
            position.plus(new Vector(-x,  0,  0)),
            position.plus(new Vector( 0,  0,  0)),
            position.plus(new Vector( x,  0,  0)),
            
            position.plus(new Vector(-x,  y,  0)),
            position.plus(new Vector( 0,  y,  0)),
            position.plus(new Vector( x,  y,  0)),
            
            position.plus(new Vector(-x, -y,  z)),
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
    }
    
// Evolution
    
    /**
     * Update the state of the map.
     */
    public void update() {
        // Get rid of destroyed bodies.
        for(int i = size() - 1; i >= 0; i--) {
            if( get(i).isDestroyed() ) {
                remove(i);
            }
        }
        
        // Normalise each body's position vector and update its state.
        for (Body b : this) {
            b.setPosition(this.normalise(b.getPosition()));
            b.update();
        }
        
        // Make bots do their thing.
        for (Bot b : bots) {
            b.update();
        }
        
        // Make touching bodies rebound.
//        for (Body a : this) {
//            for (Body b : this) {
//                if (a.isTouching(b)) {
//                    a.rebound(b);
//                }
//            }
//        }
    }
}
