package Graphics;

public class Vector {
	public double x, y, z;
	public double h = 1;

	public Vector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	public void normalise(){
		double length = Math.sqrt(x*x + y*y + z*z);
		if(length > 0){
			x /= length;
			y /= length;
			z /= length;
		}
		else{
			x = 0;
			y = 0;
			z = 0;
		}
	}
	
	public Vector crossProduct(Vector v){
		Vector cross = new Vector(
				y * v.z - z * v.y,
				z * v.x - x * v.z,
				x * v.y - y * v.x);
		return cross;
	}
	
	public Vector vectorPlusVector(Vector w){
		Vector u = new Vector(x + w.x, y + w.y, z + w.z);
		return u;
	}
	
	public Vector vectorMinusVector(Vector w){
		Vector u = new Vector(x - w.x, y - w.y, z - w.z);
		return u;
	}
	
	public Vector rotateX(double angle){
		double[][] m = Matrix.getRotationMatrixX(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	public Vector rotateY(double angle){
		double[][] m = Matrix.getRotationMatrixY(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	public Vector rotateZ(double angle){
		double[][] m = Matrix.getRotationMatrixZ(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	public Vector scale(double sx, double sy, double sz){
		double[][] m = Matrix.getScalingMatrix(sx, sy, sz);
		double[][] n = Matrix.multiplyVector(m, this);
		return new Vector(n[0][0], n[1][0], n[2][0]);
	}

	public double dotProduct(Point p) {
		return x * p.x + y * p.y + z * p.z;
	}
}
