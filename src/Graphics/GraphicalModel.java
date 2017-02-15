package Graphics;
import java.awt.Color;


public class GraphicalModel {
	
	private Point[] vertices;
	private int[][] sides;
	private Poly3D[] polys;
	private Color[] colors;
	
	public GraphicalModel(Point[] vertices, int[][] sides, Color[] colors){
		this.vertices = vertices;
		this.sides = sides;
		this.colors = colors;
		polys = new Poly3D[sides.length];
	}
	
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
