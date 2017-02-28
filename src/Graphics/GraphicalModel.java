package Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

import Geometry.Vector;


/**
 * Creates Poly3D objects to represent a 3D shape from its given vertex positions and faces
 * @author Dominic
 *
 */
public class GraphicalModel {
	
	private Vector[] vertices;
	private int[][] sides;
	private Poly3D[] polys;
	private Color[] colors;
	private BufferedImage img;
	private boolean imgPresent;
	
	/**
	 * @param vertices An array of vertices of the object
	 * @param sides An array of the sides of the objects, using integers to represent the vertices from the vertex array
	 * @param colors The colours of each side
	 */
	public GraphicalModel(Vector[] vertices, int[][] sides, Color[] colors){
		this.vertices = vertices;
		this.sides = sides;
		this.colors = colors;
		polys = new Poly3D[sides.length];
		imgPresent = false;
	}
	
	public GraphicalModel(Vector[] vertices, int[][] sides, BufferedImage img){
		this.vertices = vertices;
		this.sides = sides;
		this.img = img;
		polys = new Poly3D[sides.length];
		imgPresent = true;
	}
	
	/**
	 * Creates the object as an array of Poly3D objects
	 */
	public void create(){
		for(int i = 0; i < sides.length; i++){
			double[] xs = new double[sides[i].length];
			double[] ys = new double[sides[i].length];
			double[] zs = new double[sides[i].length];
			for(int j = 0; j < sides[i].length; j++){
//				System.out.println("i: " + i + " j: " + j + " vertex: " + sides[i][j]);
				
				xs[j] = vertices[sides[i][j]].getX();
				ys[j] = vertices[sides[i][j]].getY();
				zs[j] = vertices[sides[i][j]].getZ();
			}
			if(!imgPresent){
				polys[i] = new Poly3D(xs, ys, zs, colors[i]);
			}
			else{
				polys[i] = new Poly3D(xs, ys, zs, img);
			}
		}
		for(int i = 0; i < polys.length; i++){
			Screen.poly3Ds.add(polys[i]);
		}
	}
}
