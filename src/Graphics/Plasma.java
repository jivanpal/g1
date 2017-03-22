package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * Creates a graphical model of a plasma projectile
 * @author Dominic
 *
 */
public class Plasma{
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[6];
	private int[][] sides = {{0, 1, 2},
							 {0, 2, 3},
							 {0, 3, 4},
							 {0, 4, 1},
							 {1, 5, 2},
							 {2, 5, 3},
							 {3, 5, 4},
							 {4, 5, 1}};
	private Color[] colors = new Color[8];
	private Rotation orientation;
	
	
	/**
	 * Creates a new plasma projectile
	 * @param v The position of the projectile
	 * @param size The size of the projectile
	 * @param orientation The orientation of the projectile
	 */
	public Plasma(Vector v, double size, Rotation orientation) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < sides.length; i++){
			colors[i] = Color.CYAN;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(0, -size/2, 0);
		vertices[1] = new Vector(-size/2, 0, size/2);
		vertices[2] = new Vector(-size/2, 0, -size/2);
		vertices[3] = new Vector(size/2, 0, -size/2);
		vertices[4] = new Vector(size/2, 0, size/2);
		vertices[5] = new Vector(0, size/2, 0);
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
	
}
