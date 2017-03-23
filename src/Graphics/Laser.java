package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * A graphical representation of a laser bullet
 * @author Dominic
 *
 */
public class Laser{
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[16];
	private int[][] sides = {{0, 1, 2, 3, 4, 5, 6, 7},
							 {8, 9, 10, 11, 12, 13, 14, 15},
							 {0, 8, 9, 1},
							 {1, 9, 10, 2},
							 {2, 10, 11, 3},
							 {3, 11, 12, 4},
							 {4, 12, 13, 5},
							 {5, 13, 14, 6},
							 {6, 14, 15, 7},
							 {7, 15, 8, 0}};
	private Color[] colors = new Color[10];
	private Rotation orientation;
	
	/**
	 * Creates a new laser bullet
	 * @param v The position of the bullet
	 * @param size The size of the bullet
	 * @param orientation The orientation of the bullet
	 */
	public Laser(Vector v, double size, Rotation orientation) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < 10; i++){
			colors[i] = Color.GREEN;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(-size/16, size/2, size/8);
		vertices[1] = new Vector(-size/8, size/2, size/16);
		vertices[2] = new Vector(-size/8, size/2, -size/16);
		vertices[3] = new Vector(-size/16, size/2, -size/8);
		vertices[4] = new Vector(size/16, size/2, -size/8);
		vertices[5] = new Vector(size/8, size/2, -size/16);
		vertices[6] = new Vector(size/8, size/2, size/16);
		vertices[7] = new Vector(size/16, size/2, size/8);
		vertices[8] = new Vector(-size/16, -size/2, size/8);
		vertices[9] = new Vector(-size/8, -size/2, size/16);
		vertices[10] = new Vector(-size/8, -size/2, -size/16);
		vertices[11] = new Vector(-size/16, -size/2, -size/8);
		vertices[12] = new Vector(size/16, -size/2, -size/8);
		vertices[13] = new Vector(size/8, -size/2, -size/16);
		vertices[14] = new Vector(size/8, -size/2, size/16);
		vertices[15] = new Vector(size/16, -size/2, size/8);
		
		
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
	
}
