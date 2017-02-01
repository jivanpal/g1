package Graphics;

public class Calculations {
	public static double t = 0;
	public static Vector w1, w2, viewVec, rotateVec, dirVec, planeVec1, planeVec2;
	public static Plane p;
	public static double[] calcFocusPos = new double[2];
	
	public static double[] calcPos(double[] viewFrom, double[] viewTo, double x, double y, double z){
		double[] projectionPlane = getProjection(viewFrom, viewTo, x, y, z, p);
		double[] drawPlane = getDraw(projectionPlane[0], projectionPlane[1], projectionPlane[2]);
		return drawPlane;
	}
	

	private static double[] getProjection(double[] viewFrom, double[] viewTo, double x, double y, double z, Plane p2) {
		Vector viewToPoint = new Vector(x - viewFrom[0], y - viewFrom[1], z - viewFrom[2]);
		t = (p2.v3.x * p2.p[0] + p2.v3.y * p2.p[1] + p2.v3.z * p2.p[2]
				- (p2.v3.x * viewFrom[0] + p2.v3.y * viewFrom[1] + p2.v3.z * viewFrom[2]))
				/ (p2.v3.x * viewToPoint.x + p2.v3.y * viewToPoint.y + p2.v3.z * viewToPoint.z);
		x = viewFrom[0] + viewToPoint.x * t;
		y = viewFrom[1] + viewToPoint.y * t;
		z = viewFrom[2] + viewToPoint.z * t;
		return new double[] {x, y, z};
	}

	private static double[] getDraw(double d, double e, double f) {
		double drawX = w2.x * d + w2.y * e + w2.z * f;
		double drawY = w1.x * d + w1.y * e + w1.z * f;
		return new double[]{drawX, drawY};
	}
	
	private static Vector getRotationVector(double[] viewFrom, double[] viewTo){
		double dx = Math.abs(viewFrom[0] - viewTo[0]);
		double dy = Math.abs(viewFrom[1] - viewTo[1]);
		double xRotate, yRotate;
		xRotate = dy/(dx + dy);
		yRotate = dx/(dx + dy);
		if(viewFrom[1] > viewTo[1]){
			xRotate = -xRotate;
		}
		if(viewFrom[0] < viewTo[0]){
			yRotate = -yRotate;
		}
		Vector v = new Vector(xRotate, yRotate, 0);
		return v;
	}
	
	public static void setInfo(){
		viewVec = new Vector(Screen.viewTo[0] - Screen.viewFrom[0], Screen.viewTo[1] - Screen.viewFrom[1], Screen.viewTo[2] - Screen.viewFrom[2]);
		dirVec = new Vector(1, 1, 1);
		planeVec1 = viewVec.crossProduct(dirVec);
		planeVec2 = viewVec.crossProduct(planeVec1);
		p = new Plane(planeVec1, planeVec2, Screen.viewTo);
		
		rotateVec = Calculations.getRotationVector(Screen.viewFrom, Screen.viewTo);
		w1 = viewVec.crossProduct(rotateVec);
		w2 = viewVec.crossProduct(w1);
		
		calcFocusPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, Screen.viewTo[0], Screen.viewTo[1], Screen.viewTo[2]);
	}
}
