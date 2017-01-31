package Graphics;

public class Plane {
	public Vector v1, v2, v3;
	public double[] p = new double[3];
	
	public Plane(Poly3D p3d){
		p[0] = p3d.x[0];
		p[1] = p3d.y[0];
		p[2] = p3d.z[0];
		
		v1 = new Vector(p3d.x[1] - p3d.x[0], p3d.y[1] - p3d.y[0], p3d.z[1] - p3d.z[0]);
		v2 = new Vector(p3d.x[2] - p3d.x[0], p3d.y[2] - p3d.y[0], p3d.z[2] - p3d.z[0]);
		v3 = v1.crossProduct(v2);
	}
	
	public Plane(Vector v1, Vector v2, double[] p){
		this.p = p;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v1.crossProduct(v2);
	}
}
