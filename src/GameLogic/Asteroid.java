package GameLogic;

import java.util.Random;
import Physics.*;
import Geometry.*;

/**
 * Class which represents an asteroid in the game
 * @author Ivan Panchev
 *
 */
public class Asteroid extends Body {
	public static final double DENSITY     = 10;   // kg / m^3
	public static final double MIN_RADIUS  = 0.2;  // m
	public static final double MAX_RADIUS  = 1.0;  // m
	
	/**
	 * Construct an instance with the given parameters.
	 * @param radius the radius, in meters.
	 * @param position the position vector, in meters.
	 * @param orientation the orientation, described as a rotation from the default orientation.
	 */
	public Asteroid(double radius, Vector position, Rotation orientation) {
		super(
	        4/3 * Math.PI * radius * radius * radius * DENSITY,    // mass of a sphere with given radius
	        radius,
	        position,
	        orientation
        );
	}
	
/// RANDOM ASTEROID
	private static Random r = new Random();
	
	public static Asteroid random(Vector dimensions) {
	    return new Asteroid(
            MIN_RADIUS + r.nextDouble() * (MIN_RADIUS - MAX_RADIUS),
            new Vector(
                r.nextDouble() * dimensions.getX(),
                r.nextDouble() * dimensions.getY(),
                r.nextDouble() * dimensions.getZ()
            ),
            new Rotation(
                r.nextDouble() * 2*Math.PI,
                r.nextDouble() *   Math.PI,
                r.nextDouble() * 2*Math.PI
            )
        );
	}
}
