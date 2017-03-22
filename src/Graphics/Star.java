package Graphics;

import Geometry.Vector;
import Physics.Body;

/**
 * A model of a star
 * @author Dominic
 *
 */
public class Star extends Body{
	private Vector position;
	
	/**
	 * Creates a new star
	 * @param x X-coordinate of the star
	 * @param y Y-coordinate of the star
	 * @param z Z-coordinate of the star
	 */
	public Star(int x, int y, int z){
		position = new Vector(x, y, z);
	}
	
	/* (non-Javadoc)
	 * @see Physics.Body#getPosition()
	 */
	public Vector getPosition(){
		return position;
	}
}
