package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

public class Laser{
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[6];
	private int[][] sides = {{0, 1, 2},
							 {3, 5, 4},
							 {0, 3, 4, 1},
							 {0, 2, 5, 3},
							 {1, 4, 5, 2}};
	private Color[] colors = new Color[5];
	private Vector xVec, yVec, zVec;
	private Rotation orientation;
	
	public Laser(Vector v, double size, Rotation orientation) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < 5; i++){
			colors[i] = Color.RED;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(0, size/2, -size/8);
		vertices[1] = new Vector(size/8, size/2, size/8);
		vertices[2] = new Vector(-size/8, size/2, size/8);
		vertices[3] = new Vector(0, -size/2, -size/8);
		vertices[4] = new Vector(size/8, -size/2, size/8);
		vertices[5] = new Vector(-size/8, -size/2, size/8);
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
	
}
