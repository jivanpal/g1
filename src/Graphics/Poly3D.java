package Graphics;
import java.awt.Color;

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
		createPolygon();
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
		for(int i = 0; i < x.length; i++){
			Point p = new Point(x[i], y[i], z[i]);
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
		double angle = Math.acos(((lightPlane.v3.x * Screen.lightDir.z) +
				(lightPlane.v3.y * Screen.lightDir.y) + (lightPlane.v3.z * Screen.lightDir.z))
				/(Math.sqrt(Screen.lightDir.x * Screen.lightDir.x + Screen.lightDir.y * Screen.lightDir.y + Screen.lightDir.z * Screen.lightDir.z)));
		
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
		return Math.sqrt((Screen.viewFrom.x - x[i]) * (Screen.viewFrom.x - x[i])
						+ (Screen.viewFrom.y - y[i]) * (Screen.viewFrom.y - y[i])
						+ (-Screen.viewFrom.z + z[i]) * (-Screen.viewFrom.z + z[i]));
	}
}