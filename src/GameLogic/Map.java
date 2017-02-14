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
public class Map {
/// FIELDS
    private Vector dimensions;
    private ArrayList<Body> mapList;
    
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
    
    /**
     * Get an element from the map.
     */
    public Body get(int index) {
        return mapList.get(index);
    }
    
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
     * Add a body to the map.
     * @param b
     */
    public void add(Body b) {
        mapList.add(b);
    }
    
    /**
     * Update the state of the map.
     */
    public void update() {
        // Get rid of destroyed bodies
        for(int i = mapList.size() - 1; i >= 0; i--) {
            if(!mapList.get(i).exists()) {
                mapList.remove(i);
            }
        }
        
        // Normalise each body's position vector
        for (Body b : mapList) {
            b.setPosition(this.normalise(b.getPosition()));
        }
        
        // Update each body's state
        for (Body b : mapList) {
            b.update();
        }
    }
}
