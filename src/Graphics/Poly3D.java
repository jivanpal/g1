package Graphics;
import java.awt.Color;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.Global;
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
	public boolean draw = true;
	
	
	/**
	 * @param x Array of all x-coordinates of the object's vertices
	 * @param y Array of all y-coordinates of the object's vertices
	 * @param z Array of all z-coordinates of the object's vertices
	 * @param c Colour of the object
	 */
	public Poly3D(double[] x, double[] y, double[] z, Color c){
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		createPolygon();
		avgDistance = getDistance();
	}

	/**
	 * Creates a new PolygonObj
	 */
	public void createPolygon(){
		poly = new PolygonObj(new double[x.length], new double[x.length], c);
	}
	
	/**
	 * Updates the position of the polygon according to the current camera vectors
	 */
	public void update(){
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		int notDrawCount = 0;
		for(int i = 0; i < x.length; i++){
			Vector p = new Vector(x[i], y[i], z[i]);
			calcPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, p);
			newX[i] = calcPos.x + Global.SCREEN_WIDTH/2;
			newY[i] = calcPos.y + Global.SCREEN_HEIGHT/2;
			
			if(calcPos.z < 0){
				draw = false;
				notDrawCount++;
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
		double angle = Math.acos(lightPlane.v3.length() * lightPlane.v3.length()/Screen.lightDir.length());
		
		poly.light = 0.3 + 1 - Math.sqrt(Math.abs(Math.toDegrees(angle)/180));
//		System.out.println("Radians: " + angle);
//		System.out.println("Degrees: " + (Math.toDegrees(angle)/180));
//		System.out.println("Abs: " + (Math.abs(Math.toDegrees(angle)/180)));
//		System.out.println("Sqrt: " + Math.sqrt(Math.abs(Math.toDegrees(angle)/180)));
		
		if(poly.light > 1){
			poly.light = 1;
		}
		else if(poly.light < 0.3){
			poly.light = 0.3;
		}
		
		double fadeIn = (avgDistance - MapContainer.MAP_SIZE * 0.9) / 100;
		
		if(fadeIn < 0){
			fadeIn = 1;
		}
		else if(fadeIn < 1 && fadeIn >= 0){
			fadeIn = 1 - fadeIn;
		}
		else{
			fadeIn = 0;
		}
		
		poly.light *= fadeIn;
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