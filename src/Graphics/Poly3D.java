package Graphics;
import java.awt.Color;

public class Poly3D {
	private Color c;
	public double[] x, y, z, calcPos, newX, newY;
	PolygonObj poly;
	public double avgDistance;
	private boolean draw = true;
	
	
	public Poly3D(double[] x, double[] y, double[] z, Color c){
//		Screen.nPoly3D++;
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		createPolygon();
	}
	
	public void createPolygon(){
		poly = new PolygonObj(new double[x.length], new double[x.length], c, Screen.poly3Ds.size());
	}
	
	public void update(){
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for(int i = 0; i < x.length; i++){
			Point p = new Point(x[i], y[i], z[i]);
			calcPos = Calculations.calcPos(Screen.viewFrom, Screen.viewTo, p);
//			newX[i] = (GameEngine.screenSize.getWidth()/2 - Calculations.calcFocusPos[0]) + calcPos[0];
//			newY[i] = (GameEngine.screenSize.getHeight()/2 - Calculations.calcFocusPos[1]) + calcPos[1];
			newX[i] = calcPos[0] + GameEngine.screenSize.getWidth()/2;
			newY[i] = calcPos[1] + GameEngine.screenSize.getHeight()/2;
//			System.out.println(newX[i] + ", " + newY[i]);
			if(Calculations.t < 0){
				draw = false;
			}
		}
		
		lighting();
		
		poly.draw = draw;
		poly.update(newX, newY);
		avgDistance = getDistance();
	}
	
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

	public double getDistance(){
		double total = 0;
		for(int i = 0; i < x.length; i++){
			total += getDistanceToP(i);
		}
		return total / x.length;
	}
	
	private double getDistanceToP(int i){
		return Math.sqrt((Screen.viewFrom.x - x[i]) * (Screen.viewFrom.x - x[i])
						+ (Screen.viewFrom.y - y[i]) * (Screen.viewFrom.y - y[i])
						+ (Screen.viewFrom.z - z[i]) * (Screen.viewFrom.z - z[i]));
	}
}