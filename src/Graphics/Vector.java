package Graphics;

/**
 * Representation of a 3D vector
 * @author Dominic
 *
 */
public class Vector {
	public double x, y, z;
	public double h = 1;

	/**
	 * Creates a new vector from given x, y and z distances
	 * @param x The x distance
	 * @param y The y distance
	 * @param z The z distance
	 */
	public Vector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	/**
	 * Normalises the vector (changes it to a length of one)
	 */
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
	
	/**
	 * Calculates the cross product of two vectors
	 * @param v The vector to cross with
	 * @return The resulting vector
	 */
	public Vector crossProduct(Vector v){
		Vector cross = new Vector(
				y * v.z - z * v.y,
				z * v.x - x * v.z,
				x * v.y - y * v.x);
		return cross;
	}
	
	/**
	 * Calculates the new vector from two vectors added together
	 * @param w The vector to add to
	 * @return The new vector
	 */
	public Vector vectorPlusVector(Vector w){
		Vector u = new Vector(x + w.x, y + w.y, z + w.z);
		return u;
	}
	
	/**
	 * Calculates the new vector when this vector is subtracted by another vector
	 * @param w The vector to subtract from this one
	 * @return The new vector
	 */
	public Vector vectorMinusVector(Vector w){
		Vector u = new Vector(x - w.x, y - w.y, z - w.z);
		return u;
	}
	
	/**
	 * Rotates the vector around the x axis
	 * @param angle The angle to rotate by
	 * @return The new vector
	 */
	public Vector rotateX(double angle){
		double[][] m = Matrix.getRotationMatrixX(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	/**
	 * Rotates the vector around the y axis
	 * @param angle The angle to rotate by
	 * @return The new vector
	 */
	public Vector rotateY(double angle){
		double[][] m = Matrix.getRotationMatrixY(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	/**
	 * Rotates the vector around the z axis
	 * @param angle The angle to rotate by
	 * @return The new vector
	 */
	public Vector rotateZ(double angle){
		double[][] m = Matrix.getRotationMatrixZ(angle);
		double[][] n = Matrix.multiply(m, new double[][]{{x, y, z, h}});
		return new Vector(n[0][0], n[0][1], n[0][2]);
	}
	
	/**
	 * Scales the vector by the given scale amounts
	 * @param sx Scale factor in the x direction
	 * @param sy Scale factor in the y direction
	 * @param sz Scale factor in the z direction
	 * @return The new vector
	 */
	public Vector scale(double sx, double sy, double sz){
		double[][] m = Matrix.getScalingMatrix(sx, sy, sz);
		double[][] n = Matrix.multiplyVector2(m, this);
		return new Vector(n[0][0], n[1][0], n[2][0]);
	}

	/**
	 * Perform the dot product between this vector and a given point
	 * @param p The point to perform dot product with
	 * @return The resulting answer
	 */
	public double dotProduct(Point p) {
		return x * p.x + y * p.y + z * p.z;
	}
}
