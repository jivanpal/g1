package Graphics;
import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;


/**
 * Creates an icosahedron object and adds it to the objects to be displayed on screen
 * @author Dominic
 *
 */
public class Icosahedron{
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[12];
	private int[][] sides = {{0, 8, 4},
								{0, 5, 10},
								{2, 4, 9},
								{2 ,11 ,5},
								{1, 6, 8},
								{1, 10, 7},
								{3, 9, 6},
								{3, 7, 11},
								{0, 10, 8},
								{1, 8, 10},
								{2, 9, 11},
								{3, 11, 9},
								{4, 2, 0},
								{5, 0, 2},
								{6, 1, 3},
								{7, 3, 1},
								{8, 6, 4},
								{9, 4, 6},
								{10, 5, 7},
								{11, 7, 5}};
	private Color[] colors = new Color[20];
	private double gr = 1.618;
	private Vector xVec, yVec, zVec;
	private Rotation orientation;
	
	/**
	 * Creates the new icosahedron, and adds it to the screen
	 * @param v x, y and z coordinates of its position
	 * @param size Size of the object
	 */
	public Icosahedron(Vector v, double size, Rotation orientation) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		gr *= size;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < sides.length; i++){
			colors[i] = Color.BLUE;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(gr, size, 0);
		vertices[1] = new Vector(-gr, size, 0);
		vertices[2] = new Vector(gr, -size, 0);
		vertices[3] = new Vector(-gr, -size, 0);
		vertices[4] = new Vector(size, 0, gr);
		vertices[5] = new Vector(size, 0, -gr);
		vertices[6] = new Vector(-size, 0, gr);
		vertices[7] = new Vector(-size, 0, -gr);
		vertices[8] = new Vector(0, gr, size);
		vertices[9] = new Vector(0, -gr, size);
		vertices[10] = new Vector(0, gr, -size);
		vertices[11] = new Vector(0, -gr, -size);
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
	
}
