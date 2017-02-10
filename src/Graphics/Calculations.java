package Graphics;

public class Calculations {
	public static double t = 0;
	public static Vector w1, w2, viewVec, rotateVec, dirVec, planeVec1, planeVec2;
	public static Plane p;
	public static Point calcFocusPos;
	
	public static Point calcPos(Point viewFrom, Point viewTo, Point point){
		Point pInCamera = Matrix.multiplyPoint(Screen.CM, point);
		return new Point((pInCamera.x*1000)/pInCamera.z, (pInCamera.y*1000)/pInCamera.z, pInCamera.z * 1000);
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
	
	
	//Leftover code, currently not doing very much but scared to get rid of it
	public static void setInfo(){
		viewVec = Screen.viewTo.pointMinusPoint(Screen.viewFrom);
		dirVec = new Vector(1, 1, 1);
		planeVec1 = viewVec.crossProduct(dirVec);
		planeVec2 = viewVec.crossProduct(planeVec1);
		p = new Plane(planeVec1, planeVec2, Screen.viewTo);
		
		rotateVec = Calculations.getRotationVector(Screen.viewFrom, Screen.viewTo);
		w1 = viewVec.crossProduct(rotateVec);
		w2 = viewVec.crossProduct(w1);
		
		calcFocusPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, Screen.viewTo);
	}
}
