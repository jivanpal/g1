package GameLogic;

import Physics.*;
import Geometry.*;

/**
 * Class which represents an asteroid in the game
 * @author Jivan
 *
 */
public class Asteroid extends Body {
	public static final int MASS = 10;
	public static final int RADIUS = 2;
	
	public Asteroid(Vector position, Rotation orientation) {
		// Set parameters
		super(MASS, RADIUS, position, orientation);
	}
}
