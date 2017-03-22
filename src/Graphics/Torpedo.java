package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

public class Torpedo{
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[25];
	private int[][] sides = {{0, 6, 1},
							 {0, 1, 2},
							 {0, 2, 3},
							 {0, 3, 4},
							 {0, 4, 5},
							 {0, 5, 6},
							 {1, 6, 7, 12},
							 {1, 12, 11, 2},
							 {2, 11, 10, 3},
							 {3, 10, 9, 4},
							 {4, 9, 8, 5},
							 {5, 8, 7, 6},
							 {7, 8, 9, 10, 11, 12},
							 {13, 14, 15},
							 {13, 15, 16},
							 {14, 16, 15},
							 {13, 16, 14},
							 {17, 18, 19},
							 {17, 19, 20},
							 {18, 20 ,19},
							 {17, 20, 18},
							 {21, 22, 23},
							 {21, 23, 24},
							 {22, 24, 23},
							 {21, 24, 22}};
	private Color[] colors = new Color[sides.length];
	private Rotation orientation;
	private double colourFactor;
	
	
	public Torpedo(Vector v, double size, Rotation orientation, double timeToLive) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		if(timeToLive > 2){
			colourFactor = 1;
		}
		else if(timeToLive <= 0){
			colourFactor = 0;
		}
		else{
			colourFactor = timeToLive/2;
		}
		
		createPoints();
		
		for(int i = 0; i < sides.length; i++){
			colors[i] = new Color((int)(Color.GRAY.getRed() * colourFactor), (int)(Color.GRAY.getGreen() * colourFactor), (int)(Color.GRAY.getBlue() * colourFactor));
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(0, size*11/10, 0);
		vertices[1] = new Vector(size/10, size, Math.sqrt(3)*size/10);
		vertices[2] = new Vector(size/5, size, 0);
		vertices[3] = new Vector(size/10, size, -Math.sqrt(3)*size/10);
		vertices[4] = new Vector(-size/10, size, -Math.sqrt(3)*size/10);
		vertices[5] = new Vector(-size/5, size, 0);
		vertices[6] = new Vector(-size/10, size, Math.sqrt(3)*size/10);
		
		vertices[7] = new Vector(-size/20, -size, Math.sqrt(3)*size/20);
		vertices[8] = new Vector(-size/10, -size, 0);
		vertices[9] = new Vector(-size/20, -size, -Math.sqrt(3)*size/20);
		vertices[10] = new Vector(size/20, -size, -Math.sqrt(3)*size/20);
		vertices[11] = new Vector(size/10, -size, 0);
		vertices[12] = new Vector(size/20, -size, Math.sqrt(3)*size/20);
		
		vertices[13] = new Vector(0, -size/2, 3*Math.sqrt(3)*size/40);
		vertices[14] = new Vector(size*6/100, -size*4/5, 3*Math.sqrt(3)*size/50);
		vertices[15] = new Vector(-size*6/100, -size*4/5, 3*Math.sqrt(3)*size/50);
		vertices[16] = new Vector(0, -size, Math.sqrt(3)*size/10);
		
		vertices[17] = new Vector(size*9/80, -size/2, -3*Math.sqrt(3)*size/80);
		vertices[18] = new Vector(size*3/100, -size*4/5, -3*Math.sqrt(3)*size/50);
		vertices[19] = new Vector(size*12/100, -size*4/5, 0);
		vertices[20] = new Vector(size*3/20, -size, -Math.sqrt(3)*size/20);
		
		vertices[21] = new Vector(-size*9/80, -size/2, -3*Math.sqrt(3)*size/80);
		vertices[22] = new Vector(-size*12/100, -size*4/5, 0);
		vertices[23] = new Vector(-size*3/100, -size*4/5, -3*Math.sqrt(3)*size/50);
		vertices[24] = new Vector(-size*3/20, -size, -Math.sqrt(3)*size/20);
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
	
}
