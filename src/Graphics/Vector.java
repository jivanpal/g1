package Graphics;

public class Vector {
	public double x, y, z;

	public Vector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		double length = Math.sqrt(x*x + y*y + z*z);
		if(length > 0){
			this.x /= length;
			this.y /= length;
			this.z /= length;
		}
		else{
			this.x = 0;
			this.y = 0;
			this.z = 0;
		}
	}
	
	public Vector crossProduct(Vector v){
		Vector cross = new Vector(
				y * v.z - z * v.y,
				z * v.x - x * v.z,
				x * v.y - y * v.x);
		return cross;
	}
}
