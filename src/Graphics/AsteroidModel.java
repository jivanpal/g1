package Graphics;
import java.awt.Color;
import java.util.Random;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * Creates an Asteroid Model
 * @author Dominic
 *
 */
public class AsteroidModel {
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[12];
	private int[][] sides = {	{0, 1, 2},
								{0, 1, 3},
								{0, 2, 3},
								{1, 2, 3}};
	private Color[] colors = new Color[20];
	private Vector xVec, yVec, zVec;
	private Rotation orientation;
	
	public AsteroidModel(Vector v, double size, Rotation orientation){
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < 4; i++){
			colors[i] = Color.RED;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}
	
	private void createPoints(){
		Random r = new Random();
		
		for(int i = 0; i < 4; i++){
			vertices[i] = new Vector((r.nextDouble() - 0.5) * size + x, (r.nextDouble() - 0.5) * size + y, (r.nextDouble() - 0.5) * size + z);
			orientation.apply(vertices[i]);
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
}
