package Graphics;

import Geometry.Vector;

/**
 * Class to access different matrices (represented by double[][]) and calculations involving matrices
 * @author Dominic
 *
 */
public class Matrix {
	
	public static int[][] identityMatrix = new int[][]{
		{1, 0, 0, 0},
		{0, 1, 0, 0},
		{0, 0, 1, 0},
		{0, 0, 0, 1}
	};
	
	/**
	 * @return The identity matrix
	 */
	public static int[][] getIdentityMatrix(){
		return identityMatrix;
	}
	
	/**
	 * Creates a translation matrix for given x, y and z values
	 * @param x Transformation in the x-direction
	 * @param y Transformation in the y-direction
	 * @param z Transformation in the z-direction
	 * @return Returns the translation matrix
	 */
	public static double[][] getTranslationMatrix(double x, double y, double z){
		return new double[][]{
			{1, 0, 0, x},
			{0, 1, 0, y},
			{0, 0, 1, z},
			{0, 0, 0, 1}
		};
	}
	
	/**
	 * Creates a scaling matrix for given x, y and z values
	 * @param x The scale factor in the x direction
	 * @param y The scale factor in the y direction
	 * @param z The scale factor in the z direction
	 * @return Returns the scaling matrix
	 */
	public static double[][] getScalingMatrix(double x, double y, double z){
		return new double[][]{
			{x, 0, 0, 0},
			{0, y, 0, 0},
			{0, 0, z, 0},
			{0, 0, 0, 1}
		};
	}
	
	/**
	 * Creates a rotation matrix around the x axis and the origin
	 * @param angle The angle of rotation
	 * @return Returns the rotation matrix
	 */
	public static double[][] getRotationMatrixX(double angle){
		return new double[][]{
			{1, 0, 0, 0},
			{0, Math.cos(angle), -Math.sin(angle), 0},
			{0, Math.sin(angle), Math.cos(angle), 0},
			{0, 0, 0, 1}
		};
	}
	
	/**
	 * Creates a rotation matrix around the y axis and the origin
	 * @param angle The angle of rotation
	 * @return Returns the rotation matrix
	 */
	public static double[][] getRotationMatrixY(double angle){
		return new double[][]{
			{Math.cos(angle), 0, Math.sin(angle), 0},
			{0, 1, 0, 0},
			{-Math.sin(angle), 0, Math.cos(angle), 0},
			{0, 0, 0, 1}
		};
	}
	
	/**
	 * Creates a rotation matrix around the z axis and the origin
	 * @param angle The angle of rotation
	 * @return Returns the rotation matrix
	 */
	public static double[][] getRotationMatrixZ(double angle){
		return new double[][]{
			{Math.cos(angle), -Math.sin(angle), 0, 0},
			{Math.sin(angle), Math.cos(angle), 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 1}
		};
	}
	
	/**
	 * Creates a rotation matrix around a given axis and the origin
	 * @param angle The angle of rotation
	 * @param v The vector you wish to rotate around
	 * @return The rotation matrix
	 */
	public static double[][] getRotationMatrix(double angle, Vector v){
		double[][] m = new double[4][4];
		m[0][0] = Math.cos(angle) + v.getX() * v.getX() * (1 - Math.cos(angle));
		m[0][1] = v.getY() * v.getY() * (1 - Math.cos(angle)) + v.getZ() * Math.sin(angle);
		m[0][2] = v.getZ() * v.getZ() * (1 - Math.cos(angle)) - v.getY() * Math.sin(angle);
		m[0][3] = 0;
		
		m[1][0] = v.getX() * v.getY() * (1 - Math.cos(angle)) - v.getZ() * Math.sin(angle);
		m[1][1] = Math.cos(angle) + v.getY() * v.getY() * (1 - Math.cos(angle));
		m[1][2] = v.getZ() * v.getY() * (1 - Math.cos(angle)) + v.getX() * Math.sin(angle);
		m[1][3] = 0;
		
		m[2][0] = v.getX() * v.getZ() * (1 - Math.cos(angle)) + v.getY() * Math.sin(angle);
		m[2][1] = v.getY() * v.getZ() * (1 - Math.cos(angle)) - v.getX() * Math.sin(angle);
		m[2][2] = Math.cos(angle) + v.getZ() * v.getZ() * (1 - Math.cos(angle));
		m[2][3] = 0;
		
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return m;
	}
	
//	//TODO: Fix Rotation around an arbitary axis for a given point in space
//	public static double[][] getRotationMatrix(double angle, Vector v, Point p){
//		double[][] m = Matrix.multiply(getTranslationMatrix(p.x, p.y, p.z), getRotationMatrixX(angle));
//		m = Matrix.multiply(m, getRotationMatrixY(angle));
//		
//		return m;
//	}
	
	/**
	 * Creates the camera matrix from its current position, view position and U vector
	 * @param viewFrom The position of the camera
	 * @param viewTo The point the camera is looking at
	 * @param U The Up vector of the camera
	 * @return The camera matrix
	 */
	public static double[][] getCameraMatrix(Vector viewFrom, Vector viewTo, Vector U){
		Vector N = viewFrom.minus(viewTo);
		N.normalise();
		
		U = U.cross(N);
		U.normalise();
		
		Vector V = N.cross(U);
		V.normalise();
		
		double tx = U.dot(viewFrom);
		double ty = V.dot(viewFrom);
		double tz = N.dot(viewFrom);
		
		double[][] m = {{U.getX(), U.getY(), U.getZ(), tx},
						{V.getX(), V.getY(), V.getZ(), ty},
						{N.getX(), N.getY(), N.getZ(), tz},
						{0,   0,   0,   1 }};
		return m;
	}
	
	/**
	 * Returns the product of two matrices
	 * @param m1 The first matrix to be multiplied
	 * @param m2 The second matrix to be multiplies
	 * @return The product of the two matrices
	 */
	public static double[][] multiply(double[][] m1, double[][] m2){
		if(m1[0].length == m2.length){
			double[][] ans = new double[m1.length][m2[0].length];
			for(int i = 0; i < m1.length; i++){
				for(int j = 0; j < m2[0].length; j++){
					double total = 0;
					for(int n = 0; n < m1[0].length; n++){
						total += m1[i][n] * m2[n][j];
					}
					ans[i][j] = total;
				}
			}
			return ans;
		}
		else{
			return new double[][]{};
		}
	}
	
	/**
	 * Returns the matrix that results from a vector being multiplied by a matrix
	 * @param m1 The matrix to be multiplied
	 * @param v The vector to be multiplied
	 * @return The resulting matrix
	 */
	public static double[][] multiplyVector2(double[][] m1, Vector v){
		double[][] m2 = new double[][] {{v.getX()}, {v.getY()}, {v.getZ()}, {1}};
		return multiply(m1, m2);
	}
	
	/**
	 * Returns the vector that results from a matrix being multiplied by a vector
	 * @param m1 The matrix to be multiplied
	 * @param v The vector to be multiplied
	 * @return The resulting vector
	 */
	public static Vector multiplyVector(double[][] m1, Vector v){
		double[][] m2 = multiplyVector2(m1, v);
		return new Vector(m2[0][0], m2[1][0], m2[2][0]);
	}
	
	/**
	 * Returns the point that results from a matrix being multiplied by a point
	 * @param m1 The matrix to be multiplied
	 * @param p The point to be multiplied
	 * @return The resulting point
	 */
	public static Point multiplyPoint(double[][] m1, Point p){
		double[][] m2 = new double[][] {{p.x}, {p.y}, {p.z}, {p.h}};
		double[][] m3 = multiply(m1, m2);
		return new Point(m3[0][0], m3[1][0], m3[2][0]);
	}
	
	/**
	 * Calculates the inverse of a given matrix
	 * @param m Matrix to be inverted
	 * @return Inverse of the given matrix
	 */
	public static double[][] inverse(double[][] m){
		int n = m.length;
		double[][] a = new double[n][n];
		double[][] b = new double[n][n];
		int[] index = new int[n];
		
		for(int i = 0; i < n; i++){
			b[i][i] = 1;
		}
		
		gaussian(m, index);
		
		for(int i = 0; i < n-1; i++){
			for(int j = i+1; j < n; j++){
				for(int k = 0; k < n; k++){
					b[index[j]][k] -= m[index[j]][i] * b[index[i]][k];
				}
			}
		}
		
		for(int i = 0; i < n; i++){
			a[n-1][i] = b[index[n-1]][i] / m[index[n-1]][n-1];
			for(int j = n-2; j >= 0; j--){
				a[j][i] = b[index[j]][i];
				for(int k = j+1; k < n; k++){
					a[j][i] -= m[index[j]][k]*a[k][i];
				}
				a[j][i] /= m[index[j]][j];
			}
		}
		return a;
	}
	
	private static void gaussian(double[][] m, int index[]){
		int n = index.length;
		double[] c = new double[n];
		
		for(int i = 0; i < n; i++){
			index[i] = i;
		}
		
		for(int i = 0; i < n; i++){
			double c1 = 0;
			for(int j = 0; j < n; j++){
				double c0 = Math.abs(m[i][j]);
				if(c0 > c1){
					c1 = c0;
				}
			}
			c[i] = c1;
		}
		
		int k = 0;
		for(int j = 0; j < n-1; j++){
			double pi1 = 0;
			for(int i = j; i < n; i++){
				double pi0 = Math.abs(m[index[i]][j]);
				pi0 /= c[index[i]];
				if(pi0 > pi1){
					pi1 = pi0;
					k = i;
				}
			}
			
			int temp = index[j];
			index[j] = index[k];
			index[k] = temp;
			for(int i = j+1; i < n; i++){
				double p = m[index[i]][j] / m[index[j]][j];
				m[index[i]][j] = p;
				for(int l = j+1; l < n; l++){
					m[index[i]][l] -= p * m[index[j]][l];
				}
			}
		}
		
//		return m;
	}
	
	/**
	 * Creates the translation matrix for the camera
	 * @param viewFrom The position of the camera
	 * @return Camera translation matrix
	 */
	private static double[][] getTM(Vector viewFrom){
		return new double[][]{	{1, 0, 0, 0-viewFrom.getX()},
								{0, 1, 0, 0-viewFrom.getY()},
								{0, 0, 1, 0-viewFrom.getZ()},
								{0, 0, 0, 1}};
	}
	
	/**
	 * Creates the rotation matrix to align the camera with the global xyz axes
	 * @param V Camera V vector
	 * @param U Camera U vector
	 * @param N Camera N vector
	 * @return Camera rotation matrix
	 */
	public static double[][] getR(Vector V, Vector U, Vector N){
		double lengthV = Math.sqrt(V.getX() * V.getX() + V.getY() * V.getY() + V.getZ() * V.getZ());
		double lengthU = Math.sqrt(U.getX() * U.getX() + U.getY() * U.getY() + U.getZ() * U.getZ());
		double lengthN = Math.sqrt(N.getX() * N.getX() + N.getY() * N.getY() + N.getZ() * N.getZ());
		
		return new double[][] { {V.getX()/lengthV, V.getY()/lengthV, V.getZ()/lengthV, 0},
								{U.getX()/lengthU, U.getY()/lengthU, U.getZ()/lengthU, 0},
								{N.getX()/lengthN, N.getY()/lengthN, N.getZ()/lengthN, 0},
								{0, 0, 0, 1}};
	}
	
	/**
	 * Creates a matrix to convert from the left handed coordinate system to the right handed coordinate system
	 * @return Camera conversion matrix
	 */
	private static double[][] getSM(){
		return new double[][] { {-1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 0, 1}};
	}
	
	/**
	 * Creates a perspective projection matrix
	 * @param fov The distance from the camera to the projection plane
	 * @return
	 */
	public static double[][] getPper(double fov){
		return new double[][] { {1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 1/fov, 0}};
	}
	
	/**
	 * Creates the matrix to convert from camera space to global coordinates
	 * @param viewFrom The position of the camera
	 * @param V The camera's V vector
	 * @param U The camera's U vector
	 * @param N The camera's N vector
	 * @param fov The distance from the camera to the projection plane
	 * @return The cameraToWorld conversion matrix
	 */
	public static double[][] getCM(Vector viewFrom, Vector V, Vector U, Vector N, double fov){
		
//		double[][] m = multiply(getTM(viewFrom), getR(U, V, N));
//		m = multiply(m, getSM());
		double[][] m = multiply(getSM(), getR(U, V, N));
		m = multiply(m, getTM(viewFrom));
//		m = multiply(m, getPper(fov));
		return m;
	}
	
	/**
	 * Prints out a given matrix
	 * @param m The matrix to be printed
	 */
	public static void printMatrix(double[][] m){
		System.out.println("-------");
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[0].length; j++){
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------");
	}
}















