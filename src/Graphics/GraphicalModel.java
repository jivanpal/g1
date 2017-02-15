package Graphics;
import java.awt.Color;


/**
 * Creates Poly3D objects to represent a 3D shape from its given vertex positions and faces
 * @author Dominic
 *
 */
public class GraphicalModel {
	
	private Point[] vertices;
	private int[][] sides;
	private Poly3D[] polys;
	private Color[] colors;
	
	/**
	 * @param vertices An array of vertices of the object
	 * @param sides An array of the sides of the objects, using integers to represent the vertices from the vertex array
	 * @param colors The colours of each side
	 */
	public GraphicalModel(Point[] vertices, int[][] sides, Color[] colors){
		this.vertices = vertices;
		this.sides = sides;
		this.colors = colors;
		polys = new Poly3D[sides.length];
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
				xs[j] = vertices[sides[i][j]].x;
				ys[j] = vertices[sides[i][j]].y;
				zs[j] = vertices[sides[i][j]].z;
			}
			polys[i] = new Poly3D(xs, ys, zs, colors[i]);
		}
		for(int i = 0; i < polys.length; i++){
			Screen.poly3Ds.add(polys[i]);
		}
	}
}
