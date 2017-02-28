package Graphics;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import Geometry.Rotation;
import Geometry.Vector;

public class Skybox {
	
	private double x, y, z, size;
	private Vector[] vertices = new Vector[8];
	private int[][] sides = {{0, 1, 2, 3},
							 {0, 3, 7, 4},
							 {0, 4, 5, 1},
							 {2, 6, 7, 3},
							 {1, 5, 6, 2},
							 {4, 7, 6, 5}};
	private BufferedImage img;
	
	
	public Skybox(Vector v, double size, BufferedImage img) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size / 2;
		this.img = img;
		
		createPoints();
		
		GraphicalModel g = new GraphicalModel(vertices, sides, img);
		g.create();
	}
	
	public void createPoints(){
		vertices[0] = new Vector(x - size, y + size, z + size);
		vertices[1] = new Vector(x - size, y - size, z + size);
		vertices[2] = new Vector(x + size, y + size, z + size);
		vertices[3] = new Vector(x + size, y - size, z + size);
		vertices[4] = new Vector(x - size, y + size, z - size);
		vertices[5] = new Vector(x - size, y - size, z - size);
		vertices[6] = new Vector(x + size, y + size, z - size);
		vertices[7] = new Vector(x + size, y - size, z - size);
	}
}
