package Graphics;
import java.awt.Color;
import java.util.Random;

import Geometry.Vector;

/**
 * Creates an Asteroid Model
 * @author Dominic
 *
 */
public class AsteroidModel {
	
	private double x, y, z, size;
	private Point[] vertices = new Point[12];
	private int[][] sides = {	{0, 1, 2},
								{0, 1, 3},
								{0, 2, 3},
								{1, 2, 3}};
	private Color[] colors = new Color[20];
	private Vector xVec, yVec, zVec;
	
	public AsteroidModel(Vector v, double size, Vector xVec, Vector yVec, Vector zVec){
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.xVec = xVec;
		this.yVec = yVec;
		this.zVec = zVec;
		
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
			vertices[i] = new Point((r.nextDouble() - 0.5) * size + x, (r.nextDouble() - 0.5) * size + y, (r.nextDouble() - 0.5) * size + z);
			vertices[i] = Matrix.multiplyPoint(Matrix.getR(xVec, yVec, zVec), vertices[i]);
			vertices[i] = new Point(vertices[i].x + x, vertices[i].y + y, vertices[i].z + z);
		}
	}
}
