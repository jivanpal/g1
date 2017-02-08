package Graphics;

public class Calculations {
	public static double t = 0;
	public static Vector w1, w2, viewVec, rotateVec, dirVec, planeVec1, planeVec2;
	public static Plane p;
	public static double[] calcFocusPos = new double[2];
	
	public static double[] calcPos(Point viewFrom, Point viewTo, Point point){
//		Point projectionPlane = getProjection(viewFrom, viewTo, point, p);
//		double[] drawPlane = getDraw(projectionPlane);
//		double[] drawPlane = getDraw(point);
//		return drawPlane;
		
		Point pInCamera = Matrix.multiplyPoint(point, Screen.CM);
		
		return new double[] {(pInCamera.x/pInCamera.h)*1000, (pInCamera.y/pInCamera.h)*1000};
	}
	

	private static Point getProjection(Point viewFrom, Point viewTo, Point point, Plane p2) {
		Vector viewToPoint = point.pointMinusPoint(viewFrom);
		t = (p2.v3.x * p2.p.x + p2.v3.y * p2.p.y + p2.v3.z * p2.p.z
				- (p2.v3.x * viewFrom.x + p2.v3.y * viewFrom.y + p2.v3.z * viewFrom.z))
				/ (p2.v3.x * viewToPoint.x + p2.v3.y * viewToPoint.y + p2.v3.z * viewToPoint.z);
		viewToPoint = viewToPoint.scale(t, t, t);
		Point projPlane = viewFrom.pointPlusVector(viewToPoint);
		return projPlane;
	}

	private static double[] getDraw(Point point) {
		double drawX = w2.x * point.x + w2.y * point.y + w2.z * point.z;
		double drawY = w1.x * point.x + w1.y * point.y + w1.z * point.z;
		return new double[]{drawX * 2000, drawY * 2000};
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
