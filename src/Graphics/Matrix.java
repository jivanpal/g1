package Graphics;

public class Matrix {
	static int[][] identityMatrix = new int[][]{
		{1, 0, 0, 0},
		{0, 1, 0, 0},
		{0, 0, 1, 0},
		{0, 0, 0, 1}
	};
	
	public static int[][] getIdentityMatrix(){
		return identityMatrix;
	}
	
	public static double[][] getTranslationMatrix(double x, double y, double z){
		return new double[][]{
			{1, 0, 0, x},
			{0, 1, 0, y},
			{0, 0, 1, z},
			{0, 0, 0, 1}
		};
	}
	
	public static double[][] getScalingMatrix(double x, double y, double z){
		return new double[][]{
			{x, 0, 0, 0},
			{0, y, 0, 0},
			{0, 0, z, 0},
			{0, 0, 0, 1}
		};
	}
	
	public static double[][] getRotationMatrixX(double angle){
		return new double[][]{
			{1, 0, 0, 0},
			{0, Math.cos(angle), -Math.sin(angle), 0},
			{0, Math.sin(angle), Math.cos(angle), 0},
			{0, 0, 0, 1}
		};
	}
	
	public static double[][] getRotationMatrixY(double angle){
		return new double[][]{
			{Math.cos(angle), 0, Math.sin(angle), 0},
			{0, 1, 0, 0},
			{-Math.sin(angle), 0, Math.cos(angle), 0},
			{0, 0, 0, 1}
		};
	}
	
	public static double[][] getRotationMatrixZ(double angle){
		return new double[][]{
			{Math.cos(angle), -Math.sin(angle), 0, 0},
			{Math.sin(angle), Math.cos(angle), 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 1}
		};
	}
	
	public static double[][] getRotationMatrix(double angle, Vector v){
		double[][] m = new double[4][4];
		m[0][0] = Math.cos(angle) + v.x * v.x * (1 - Math.cos(angle));
		m[0][1] = v.y * v.y * (1 - Math.cos(angle)) + v.z * Math.sin(angle);
		m[0][2] = v.z * v.z * (1 - Math.cos(angle)) - v.y * Math.sin(angle);
		m[0][3] = 0;
		
		m[1][0] = v.x * v.y * (1 - Math.cos(angle)) - v.z * Math.sin(angle);
		m[1][1] = Math.cos(angle) + v.y * v.y * (1 - Math.cos(angle));
		m[1][2] = v.z * v.y * (1 - Math.cos(angle)) + v.x * Math.sin(angle);
		m[1][3] = 0;
		
		m[2][0] = v.x * v.z * (1 - Math.cos(angle)) + v.y * Math.sin(angle);
		m[2][1] = v.y * v.z * (1 - Math.cos(angle)) - v.x * Math.sin(angle);
		m[2][2] = Math.cos(angle) + v.z * v.z * (1 - Math.cos(angle));
		m[2][3] = 0;
		
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		
		return m;
	}
	
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
	
	public static double[][] multiplyVector(double[][] m1, Vector v){
		double[][] m2 = new double[][] {{v.x}, {v.y}, {v.z}, {v.h}};
		return multiply(m1, m2);
	}
	
	public static Vector multiplyVector2(double[][] m1, Vector v){
		double[][] m2 = multiplyVector(m1, v);
		return new Vector(m2[0][0], m2[1][0], m2[2][0]);
	}
	
	public static Point multiplyPoint(Point p, double[][] m1){
		double[][] m2 = new double[][] {{p.x}, {p.y}, {p.z}, {p.h}};
		double[][] m3 = multiply(m1, m2);
		return new Point(m3[0][0], m3[1][0], m3[2][0]);
	}
	
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
	
	public static double[][] getTM(Point p){
		return new double[][]{	{1, 0, 0, 0-p.x},
								{0, 1, 0, 0-p.y},
								{0, 0, 1, 0-p.z},
								{0, 0, 0, 1}};
	}
	
	public static double[][] getR(Vector V, Vector U, Vector N){
		double lengthV = Math.sqrt(V.x * V.x + V.y * V.y + V.z * V.z);
		double lengthU = Math.sqrt(U.x * U.x + U.y * U.y + U.z * U.z);
		double lengthN = Math.sqrt(N.x * N.x + N.y * N.y + N.z * N.z);
		
		return new double[][] { {V.x/lengthV, V.y/lengthV, V.z/lengthV, 0},
								{U.x/lengthU, U.y/lengthU, U.z/lengthU, 0},
								{N.x/lengthN, N.y/lengthN, N.z/lengthN, 0},
								{0, 0, 0, 1}};
	}
	
	public static double[][] getSM(){
		return new double[][] { {-1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 0, 1}};
	}
	
	public static double[][] getPper(double fov){
		return new double[][] { {1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 1/fov, 0}};
	}
	
	public static double[][] getCM(Point p, Vector V, Vector U, Vector N, double fov){
		double[][] m = Matrix.multiply(getPper(fov), getSM());
		m = Matrix.multiply(m, getR(V, U, N));
		m = Matrix.multiply(m, getTM(p));
		return m;
	}
	
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















