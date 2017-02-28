package Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

import Geometry.Vector;

/**
 * Creates a collection of polygons that resemble a 3D shape
 * @author Dominic
 *
 */
public class Poly3D {
	private Color c;
	public double[] x, y, z, newX, newY;
	private Point calcPos;
	PolygonObj poly;
	public double avgDistance;
	private boolean draw = true;
	private BufferedImage img;
	private boolean imgPresent;
	
	
	/**
	 * @param x Array of all x-coordinates of the object's vertices
	 * @param y Array of all y-coordinates of the object's vertices
	 * @param z Array of all z-coordinates of the object's vertices
	 * @param c Colour of the object
	 */
	public Poly3D(double[] x, double[] y, double[] z, Color c){
//		Screen.nPoly3D++;
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		imgPresent = false;
		createPolygon();
	}
	
	public Poly3D(double[] x, double[] y, double[] z, BufferedImage img) {
		this.img = img;
		this.x = x;
		this.y = y;
		this.z = z;
		imgPresent = true;
		createPolygon();
	}

	/**
	 * Creates a new PolygonObj
	 */
	public void createPolygon(){
		if(!imgPresent){
			poly = new PolygonObj(new double[x.length], new double[x.length], c);
		}
		else{
			poly = new PolygonObj(new double[x.length], new double[x.length], img);
		}
	}
	
	/**
	 * Updates the position of the polygon according to the current camera vectors
	 */
	public void update(){
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for(int i = 0; i < x.length; i++){
			Vector p = new Vector(x[i], y[i], z[i]);
			calcPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, p);
			newX[i] = calcPos.x + GameEngine.screenSize.getWidth()/2;
			newY[i] = calcPos.y + GameEngine.screenSize.getHeight()/2;
			if(calcPos.z < 0 || Calculations.t < 0){
				draw = false;
			}
		}
		
		lighting();
		
		poly.draw = draw;
		poly.update(newX, newY);
		avgDistance = getDistance();
	}
	
	/**
	 * Calculates the shading on each side of the polygon
	 */
	private void lighting() {
		Plane lightPlane = new Plane(this);
		double angle = Math.acos(((lightPlane.v3.getX() * Screen.lightDir.getZ()) +
				(lightPlane.v3.getY() * Screen.lightDir.getY()) + (lightPlane.v3.getZ() * Screen.lightDir.getZ()))
				/(Math.sqrt(Screen.lightDir.getX() * Screen.lightDir.getX() + Screen.lightDir.getY() * Screen.lightDir.getY() + Screen.lightDir.getZ() * Screen.lightDir.getZ())));
		
		poly.light = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle)/180);
		
		if(poly.light > 1){
			poly.light = 1;
		}
		if(poly.light < 0){
			poly.light = 0;
		}
	}

	/**
	 * Calculates the average distance to the object from the camera position
	 * @return Average distance from the camera position
	 */
	public double getDistance(){
		double total = 0;
		for(int i = 0; i < x.length; i++){
			total += getDistanceToP(i);
		}
		return total / x.length;
	}
	
	/**
	 * Calculates the distance from a vertex to the camera position
	 * @param i The index of the vertex in the x, y and z arrays
	 * @return Distance from a vertex to the camera
	 */
	private double getDistanceToP(int i){
		return Math.sqrt((Screen.viewFrom.getX() - x[i]) * (Screen.viewFrom.getX() - x[i])
						+ (Screen.viewFrom.getY() - y[i]) * (Screen.viewFrom.getY() - y[i])
						+ (-Screen.viewFrom.getZ() + z[i]) * (-Screen.viewFrom.getZ() + z[i]));
	}
}