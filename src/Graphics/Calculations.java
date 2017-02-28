package Graphics;

import Geometry.Vector;

/**
 * Contains calculations required to calculate the position of points in camera space
 * @author Dominic
 *
 */
public class Calculations {
	public static double t = 0;
	public static Vector w1, w2, viewVec, rotateVec, dirVec, planeVec1, planeVec2;
	public static Plane p;
	public static Point calcFocusPos;
	
	/**
	 * Calculates the position of a point in camera space from a given global coordinate
	 * @param viewFrom The position of the camera
	 * @param viewTo The position the camera is looking at
	 * @param p2 The position of the point to be transformed
	 * @return
	 */
	public static Point calcPos(Vector viewFrom, Vector viewTo, Vector p2){
		Vector pInCamera = Matrix.multiplyVector(Screen.CM, p2);
//		pInCamera = Matrix.multiplyVector(Matrix.getPper(10), pInCamera);
		return new Point((pInCamera.getX()*5000)/pInCamera.getZ(), (pInCamera.getY()*5000)/pInCamera.getZ(), 5000);
	}
	
	private static Vector getRotationVector(Point viewFrom, Point viewTo){
		double dx = Math.abs(viewFrom.x - viewTo.x);
		double dy = Math.abs(viewFrom.y - viewTo.y);
		double xRotate, yRotate;
		xRotate = dy/(dx + dy);
		yRotate = dx/(dx + dy);
		if(viewFrom.y > viewTo.y){
			xRotate = -xRotate;
		}
		if(viewFrom.x < viewTo.x){
			yRotate = -yRotate;
		}
		Vector v = new Vector(xRotate, yRotate, 0);
		return v;
	}
	
	
//	//Leftover code, currently not doing very much but scared to get rid of it
//	public static void setInfo(){
//		viewVec = Screen.viewTo.minus(Screen.viewFrom);
//		dirVec = new Vector(1, 1, 1);
//		planeVec1 = viewVec.cross(dirVec);
//		planeVec2 = viewVec.cross(planeVec1);
//		p = new Plane(planeVec1, planeVec2, Screen.viewTo);
//		
//		rotateVec = Calculations.getRotationVector(Screen.viewFrom, Screen.viewTo);
//		w1 = viewVec.crossProduct(rotateVec);
//		w2 = viewVec.crossProduct(w1);
//		
//		calcFocusPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, Screen.viewTo);
//	}
}
